package pl.symentis.shorturl.domain;

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

    public FakeDateTimeExpiryPolicyBuilder withDateTimePolicy(){
        return new FakeDateTimeExpiryPolicyBuilder();
    }

    public FakeRedirectExpiryPolicyBuilder withRedirectPolicy(){
        return new FakeRedirectExpiryPolicyBuilder();
    }

    public ExpiryPolicy build(){
        return expiryPolicy;
    }
}

