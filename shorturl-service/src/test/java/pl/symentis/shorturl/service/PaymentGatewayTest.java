package pl.symentis.shorturl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import spark.Spark;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Currency;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static spark.Spark.post;

public class PaymentGatewayTest{
    
    private ScheduledExecutorService scheduledExecutor;
    
    // system under tests
    private PaymentGateway paymentGateway;

    @BeforeEach
    public void setUp() throws Exception {
        
        ObjectMapper objectMapper = new ObjectMapper();

        RestTemplate restTemplate = new RestTemplateBuilder().messageConverters( new MappingJackson2HttpMessageConverter( objectMapper ) ).build();
        
        paymentGateway = new PaymentGateway(restTemplate, new URL("http://localhost:9090/gateway/payments" ));

        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        
        Spark.port( 9090 );
        
        // zawinąc w builder'a
        post( "/gateway/payments", ( request, response ) -> {
            PaymentRequest paymentRequest = objectMapper.readValue( request.body(), PaymentRequest.class );
            PaymentResponse paymentId = new PaymentResponse( UUID.randomUUID(), PaymentStatus.OK );

            scheduledExecutor.schedule( () ->{
                try
                {
                    restTemplate.postForEntity( paymentRequest.getCallbackUrl().toURI(), new PaymentCallbackRequest(paymentId.getUuid(),PaymentStatus.SUCCESS), Void.class );
                }
                catch ( RestClientException | URISyntaxException e )
                {
                    e.printStackTrace();
                }
            }, 1, TimeUnit.SECONDS );
            
            response.type( MediaType.APPLICATION_JSON_VALUE );
            return paymentId;
        }, objectMapper::writeValueAsString);
        
        post("/payments/callback", (request, response) -> {
            PaymentCallbackRequest callbackRequest = objectMapper.readValue( request.body(), PaymentCallbackRequest.class );
            paymentGateway.completeRecharge( callbackRequest );
            return "OK";
        });

    }
    
    @AfterEach
    public void tearDown() throws InterruptedException {
        Spark.awaitStop();
        scheduledExecutor.shutdown();
        scheduledExecutor.awaitTermination( 5, TimeUnit.SECONDS );
    }
    
    @Test
    void rechargeAndCallbackPayment() throws MalformedURLException, RestClientException, URISyntaxException {
        PaymentRequest paymentRequest = new PaymentRequest( 
                "random@random.org", 
                BigDecimal.valueOf( 50 ), 
                Currency.getInstance( "PLN" ), 
                new URL("http://localhost:9090/payments/callback" )  );
        PaymentResponse paymentResponse = paymentGateway.recharge( paymentRequest  );
        
        await().atMost( 5, TimeUnit.SECONDS ).until( () -> paymentGateway.paymentStatus(paymentResponse.getUuid()) == PaymentStatus.SUCCESS );
    }
    
}
