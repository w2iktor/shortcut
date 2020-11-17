package pl.symentis.shorturl.domain;

import com.devskiller.jfairy.Fairy;

import java.time.LocalDateTime;

public class FakeDateTimeExpiryPolicyBuilder {
    private LocalDateTime validUntil;
    private static Fairy fairy = Fairy.builder()
            .build();

    public FakeDateTimeExpiryPolicyBuilder withRandomValidUntil(){
        validUntil = fairy
                .dateProducer()
                .randomDateBetweenYears(1950, 2100);
        return this;
    }

    public FakeDateTimeExpiryPolicyBuilder withFutureValidUntil(){
        validUntil = fairy
                .dateProducer()
                .randomDateInTheFuture();
        return this;
    }

    public FakeDateTimeExpiryPolicyBuilder withPastValidUntil(){
        validUntil = fairy
                .dateProducer()
                .randomDateInThePast(50);
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
