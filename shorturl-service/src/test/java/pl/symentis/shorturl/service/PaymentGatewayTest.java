package pl.symentis.shorturl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Currency;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class PaymentGatewayTest
{
    // system under tests
    private PaymentGateway paymentGateway;
    private PaymentGatewayServiceMock paymentGatewayServiceMock;

    @BeforeEach
    public void setUp() throws Exception
    {
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplateBuilder()
                .messageConverters( new MappingJackson2HttpMessageConverter( objectMapper ) ).build();
        paymentGateway = new PaymentGateway( restTemplate, new URL( "http://localhost:9090/gateway/payments" ) );
        paymentGatewayServiceMock = PaymentGatewayServiceMock.create( paymentGateway::completeRecharge ).start();
    }

    @AfterEach
    public void tearDown() throws InterruptedException
    {
        paymentGatewayServiceMock.shutdown();
    }

    @Test
    void rechargeAndCallbackPayment() throws MalformedURLException, RestClientException, URISyntaxException
    {
        PaymentRequest paymentRequest = new PaymentRequest( 
                "random@random.org", 
                BigDecimal.valueOf( 50 ),
                Currency.getInstance( "PLN" ), 
                new URL( "http://localhost:9090/payments/callback" ) );
        
        UUID randomUUID = UUID.randomUUID();
        
        paymentGatewayServiceMock.when( paymentRequest )
            .then( () -> new PaymentResponse( randomUUID, PaymentStatus.OK ) )
            .callback( () -> new PaymentCallbackRequest( randomUUID, PaymentStatus.SUCCESS ) );
        
        PaymentResponse paymentResponse = paymentGateway.recharge( paymentRequest );
        
        await().atMost( 5, TimeUnit.SECONDS )
                .until( () -> paymentGateway.paymentStatus( paymentResponse.getUuid() ) == PaymentStatus.SUCCESS );
    }
}
