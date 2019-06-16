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

import static spark.Spark.post;

public class PaymentGatewayMock
{
    static PaymentGatewayMock create( Consumer<PaymentCallbackRequest> gatewayCallback )
    {
        return new PaymentGatewayMock( gatewayCallback );
    }

    private final Map<PaymentRequest,PaymentGatewayMockBuilder.PaymentResponseAndCallback> interactions;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ScheduledExecutorService scheduledExecutor;
    private final Consumer<PaymentCallbackRequest> gatewayCallback;

    public PaymentGatewayMock( Consumer<PaymentCallbackRequest> gatewayCallback )
    {
        this.gatewayCallback = gatewayCallback;
        this.objectMapper = new ObjectMapper();
        this.restTemplate = new RestTemplateBuilder()
                .messageConverters( new MappingJackson2HttpMessageConverter( objectMapper ) ).build();
        this.interactions = new HashMap<PaymentRequest,PaymentGatewayMockBuilder.PaymentResponseAndCallback>();
        this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    PaymentGatewayMock start()
    {
        Spark.port( 9090 );
        // payment gateway
        post( "/gateway/payments", ( request, response ) -> {
            PaymentRequest paymentRequest = objectMapper.readValue( request.body(), PaymentRequest.class );
            PaymentGatewayMockBuilder.PaymentResponseAndCallback interaction = interactions.get( paymentRequest );
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

    public PaymentGatewayMockBuilder.PaymentInteractionBuilder when( PaymentRequest paymentRequest )
    {
        return new PaymentGatewayMockBuilder(interactions).when( paymentRequest );
    }

    public void shutdown() throws InterruptedException
    {
        Spark.awaitStop();
        scheduledExecutor.shutdown();
        scheduledExecutor.awaitTermination( 5, TimeUnit.SECONDS );
    }
}
