package pl.symentis.shorturl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static spark.Spark.post;

public class PaymentGatewayServiceMock
{
    static class PaymentResponseAndCallback
    {
        Supplier<PaymentResponse> paymentResponse;
        Supplier<PaymentCallbackRequest> paymentCallbackRequest;
    }

    public static class PaymentInteractionBuilder
    {
        private final Builder builder;
        private final PaymentRequest paymentRequest;

        public PaymentInteractionBuilder( Builder builder, PaymentRequest paymentRequest )
        {
            this.builder = builder;
            this.paymentRequest = paymentRequest;
        }

        public PaymentInteractionBuilder then( Supplier<PaymentResponse> response )
        {
            builder.addRequest( paymentRequest, response );
            return this;
        }

        public PaymentInteractionBuilder callback( Supplier<PaymentCallbackRequest> callback )
        {
            builder.addCallback( paymentRequest, callback );
            return this;
        }

        public Builder and()
        {
            return builder;
        }

    }

    public static class Builder
    {
        private final Map<PaymentRequest,PaymentResponseAndCallback> interactions;

        public Builder( Map<PaymentRequest,PaymentResponseAndCallback> interactions )
        {
            this.interactions = interactions;
        }

        public PaymentInteractionBuilder when( PaymentRequest paymentRequest )
        {
            return new PaymentInteractionBuilder( this, paymentRequest );
        }

        private void addCallback( PaymentRequest paymentRequest, Supplier<PaymentCallbackRequest> paymentCallbackRequest )
        {
            interactions.compute( paymentRequest, ( key, oldValue ) -> {
                if ( oldValue == null )
                {
                    PaymentResponseAndCallback paymentResponseAndCallback = new PaymentResponseAndCallback();
                    paymentResponseAndCallback.paymentCallbackRequest = paymentCallbackRequest;
                    return paymentResponseAndCallback;
                }
                else
                {
                    oldValue.paymentCallbackRequest = paymentCallbackRequest;
                    return oldValue;
                }
            } );
        }

        void addRequest( PaymentRequest paymentRequest, Supplier<PaymentResponse> paymenResponse )
        {
            interactions.compute( paymentRequest, ( key, oldValue ) -> {
                if ( oldValue == null )
                {
                    PaymentResponseAndCallback paymentResponseAndCallback = new PaymentResponseAndCallback();
                    paymentResponseAndCallback.paymentResponse = paymenResponse;
                    return paymentResponseAndCallback;
                }
                else
                {
                    oldValue.paymentResponse = paymenResponse;
                    return oldValue;
                }
            } );
        }
    }

    static PaymentGatewayServiceMock create( Consumer<PaymentCallbackRequest> gatewayCallback )
    {
        return new PaymentGatewayServiceMock( gatewayCallback );
    }

    private final Map<PaymentRequest,PaymentResponseAndCallback> interactions;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ScheduledExecutorService scheduledExecutor;
    private final Consumer<PaymentCallbackRequest> gatewayCallback;

    public PaymentGatewayServiceMock( Consumer<PaymentCallbackRequest> gatewayCallback )
    {
        this.gatewayCallback = gatewayCallback;
        this.objectMapper = new ObjectMapper();
        this.restTemplate = new RestTemplateBuilder()
                .messageConverters( new MappingJackson2HttpMessageConverter( objectMapper ) ).build();
        this.interactions = new HashMap<PaymentRequest,PaymentResponseAndCallback>();
        this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    PaymentGatewayServiceMock start()
    {
        Spark.port( 9090 );
        // payment gateway
        post( "/gateway/payments", ( request, response ) -> {
            PaymentRequest paymentRequest = objectMapper.readValue( request.body(), PaymentRequest.class );
            PaymentResponseAndCallback interaction = interactions.get( paymentRequest );
            if ( interaction == null || interaction.paymentResponse == null )
            {
                throw new IllegalStateException( String.format( "no recorded interaction for %s", paymentRequest ) );
            }
        
            if ( interaction.paymentCallbackRequest != null )
            {
                scheduledExecutor.schedule( () -> restTemplate.postForEntity( paymentRequest.getCallbackUrl().toURI(),
                        interaction.paymentCallbackRequest.get(), Void.class ), 1, TimeUnit.SECONDS );
            }
            
            response.type( MediaType.APPLICATION_JSON_VALUE );
            return interaction.paymentResponse.get();
        }, objectMapper::writeValueAsString );
        
        // payment gateway callback
        post( "/payments/callback", ( request, response ) -> {
            PaymentCallbackRequest callbackRequest = objectMapper.readValue( request.body(), PaymentCallbackRequest.class );
            gatewayCallback.accept( callbackRequest );
            return "OK";
        } );
        
        return this;
    }

    public PaymentInteractionBuilder when( PaymentRequest paymentRequest )
    {
        return new Builder(interactions).when( paymentRequest );
    }

    public void shutdown() throws InterruptedException
    {
        Spark.awaitStop();
        scheduledExecutor.shutdown();
        scheduledExecutor.awaitTermination( 5, TimeUnit.SECONDS );
    }
}
