package pl.symentis.shorturl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentGateway{
        
    private static final Logger LOG = LoggerFactory.getLogger( PaymentGateway.class );
    
    private final RestTemplate restTemplate;
    private final URL url;

    private Map<UUID,PaymentStatus> payments = new ConcurrentHashMap<>();

    @Autowired
    public PaymentGateway( RestTemplate restTemplate, URL url )
    {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    // wołane przez payment gateway
    void completeRecharge(PaymentCallbackRequest callbackRequest) {
        LOG.info( "callback request {}", callbackRequest );
        payments.put(callbackRequest.getUuid(), callbackRequest.getStatus());
    }

    // wołane przez Kowalskiego
    public PaymentResponse recharge( PaymentRequest paymentRequest ) throws RestClientException, URISyntaxException
    {
        return restTemplate.postForObject( url.toURI(), paymentRequest, PaymentResponse.class );
    }

    public PaymentStatus paymentStatus( UUID uuid )
    {
        return payments.get( uuid );
    }
    
}
