package pl.symentis.shorturl.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class PaymentCallbackRequest
{
    
    private final UUID uuid;
    private final PaymentStatus status;
    
    @JsonCreator
    public PaymentCallbackRequest( @JsonProperty("uuid") UUID uuid, @JsonProperty("status") PaymentStatus status )
    {
        super();
        this.uuid = uuid;
        this.status = status;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public PaymentStatus getStatus()
    {
        return status;
    }
    
}
