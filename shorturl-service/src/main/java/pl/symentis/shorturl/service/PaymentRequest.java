package pl.symentis.shorturl.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.Objects;

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

    @Override
    public int hashCode()
    {
        return Objects.hash( amount, callbackUrl, currency, email );
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        PaymentRequest other = (PaymentRequest) obj;
        return Objects.equals( amount, other.amount ) && Objects.equals( callbackUrl, other.callbackUrl )
                && Objects.equals( currency, other.currency ) && Objects.equals( email, other.email );
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "PaymentRequest [email=" ).append( email ).append( ", amount=" ).append( amount )
                .append( ", currency=" ).append( currency ).append( ", callbackUrl=" ).append( callbackUrl )
                .append( "]" );
        return builder.toString();
    }
    
    
        
}
