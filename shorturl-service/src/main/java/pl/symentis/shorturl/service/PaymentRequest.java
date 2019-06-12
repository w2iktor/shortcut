package pl.symentis.shorturl.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

public class PaymentRequest{
    
    private final String email;
    private final BigDecimal amount;
    private final Currency currency;
    private final URL callbackUrl;
    
    @JsonCreator
    public PaymentRequest( 
            @JsonProperty("email") String email, 
            @JsonProperty("amount") BigDecimal amount, 
            @JsonProperty("currency") Currency currency, 
            @JsonProperty("callbackUrl") URL callbackUrl ){
        super();
        this.email = email;
        this.amount = amount;
        this.currency = currency;
        this.callbackUrl = callbackUrl;
    }

    public String getEmail()
    {
        return email;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public Currency getCurrency()
    {
        return currency;
    }

    public URL getCallbackUrl()
    {
        return callbackUrl;
    }
        
}
