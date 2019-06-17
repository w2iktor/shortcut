package pl.symentis.shorturl.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class PaymentResponse
{
    private final UUID uuid;
    private final PaymentStatus status;

    @JsonCreator
    public PaymentResponse( @JsonProperty( "uuid" ) UUID uuid, @JsonProperty( "status" ) PaymentStatus status )
    {
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
