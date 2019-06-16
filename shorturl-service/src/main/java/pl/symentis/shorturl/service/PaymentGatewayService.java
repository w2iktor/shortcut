package pl.symentis.shorturl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentGatewayService{
        
    private static final Logger LOG = LoggerFactory.getLogger( PaymentGatewayService.class );
    
    private final PaymentGatewayProperties properties;
    
    private final RestTemplate restTemplate;
    private Map<UUID,PaymentStatus> payments = new ConcurrentHashMap<>();

    @Autowired
    public PaymentGatewayService( RestTemplate restTemplate, PaymentGatewayProperties properties )
    {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    // wołane przez payment gateway
    public void completeRecharge(PaymentCallbackRequest callbackRequest) {
        LOG.info( "callback request {}", callbackRequest );
        payments.put(callbackRequest.getUuid(), callbackRequest.getStatus());
    }

    // wołane przez Kowalskiego
    public PaymentResponse recharge( PaymentRequest paymentRequest ) throws RestClientException, URISyntaxException
    {
        return restTemplate.postForObject( properties.getUrl().toURI(), paymentRequest, PaymentResponse.class );
    }

    public PaymentStatus paymentStatus( UUID uuid )
    {
        return payments.get( uuid );
    }
    
}
