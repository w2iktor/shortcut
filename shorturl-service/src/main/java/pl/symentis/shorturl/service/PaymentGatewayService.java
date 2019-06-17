package pl.symentis.shorturl.service;

import org.springframework.web.client.RestClientException;

import java.net.URISyntaxException;
import java.util.UUID;

public interface PaymentGatewayService{

    // wołane przez payment gateway
    void completeRecharge(PaymentCallbackRequest callbackRequest);

    // wołane przez Kowalskiego
    PaymentResponse recharge(PaymentRequest paymentRequest) throws RestClientException, URISyntaxException;

    PaymentStatus paymentStatus(UUID uuid);
}
