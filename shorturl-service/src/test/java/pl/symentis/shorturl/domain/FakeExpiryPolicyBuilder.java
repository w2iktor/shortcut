package pl.symentis.shorturl.domain;

import java.time.LocalDateTime;
import java.util.Random;

public class FakeExpiryPolicyBuilder {
    private ExpiryPolicy expiryPolicy;

    private FakeExpiryPolicyBuilder(){}

    public static FakeExpiryPolicyBuilder fakeExpiryPolicyBuilder(){
        return new FakeExpiryPolicyBuilder();
    }

    public FakeExpiryPolicyBuilder withRandomExipiryPolicy(){
        if (new Random().nextBoolean()){
            this.expiryPolicy = new FakeDateTimeExpiryPolicyBuilder()
                .withRandomValidUntil()
                .build();
        } else {
            this.expiryPolicy = new FakeRedirectExpiryPolicyBuilder()
                .withRandomMaxRedirections()
                .build();
        }
        return this;
    }

    public FakeRedirectExpiryPolicyBuilder withRedirectPolicy(){
        return new FakeRedirectExpiryPolicyBuilder();
    }

    public ExpiryPolicy build(){
        return expiryPolicy;
    }
}

class FakeRedirectExpiryPolicyBuilder {
    private long maxRedirections;

    public FakeRedirectExpiryPolicyBuilder withRandomMaxRedirections(){
        this.maxRedirections = new Random().nextInt(100) + 1; //minimum value for this field is '1'
        return this;
    }

    public FakeRedirectExpiryPolicyBuilder withMaxRedirections(long maxRedirections){
        this.maxRedirections = maxRedirections;
        return this;
    }

    public ExpiryPolicy build(){
        return new RedirectsExpiryPolicy(maxRedirections);
    }
}

class FakeDateTimeExpiryPolicyBuilder {
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
