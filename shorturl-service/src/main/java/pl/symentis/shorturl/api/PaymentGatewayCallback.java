package pl.symentis.shorturl.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.symentis.shorturl.service.PaymentCallbackRequest;
import pl.symentis.shorturl.service.PaymentGatewayService;

@RestController
public class PaymentGatewayCallback
{
    
    private final PaymentGatewayService paymentGatewayService;

    @Autowired
    public PaymentGatewayCallback( PaymentGatewayService paymentGateway )
    {
        super();
        this.paymentGatewayService = paymentGateway;
    }

    @PostMapping
    public void completeRecharge(PaymentCallbackRequest request) {
        paymentGatewayService.completeRecharge( request );
    }
    
}
