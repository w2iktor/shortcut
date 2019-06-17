package pl.symentis.shorturl.domain;

import java.time.LocalDateTime;

public class FakeDateTimeExpiryPolicyBuilder {
    private LocalDateTime validUntil;

    public FakeDateTimeExpiryPolicyBuilder withRandomValidUntil(){
        validUntil = RandomDateTimeFactory.generateRandomDateTime();
        return this;
    }

    public FakeDateTimeExpiryPolicyBuilder withFutureValidUntil(){
        validUntil = RandomDateTimeFactory.generateFutureDateTime();
        return this;
    }

    public FakeDateTimeExpiryPolicyBuilder withPastValidUntil(){
        validUntil = RandomDateTimeFactory.generatePastDateTime();
        return this;
    }

    public FakeDateTimeExpiryPolicyBuilder withValidUntil(LocalDateTime validUntil){
        this.validUntil = validUntil;
        return this;
    }

    public ExpiryPolicy build(){
        return new DateTimeExpiryPolicy(validUntil);
    }
}
