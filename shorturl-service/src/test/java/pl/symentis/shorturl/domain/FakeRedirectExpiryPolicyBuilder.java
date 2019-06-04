package pl.symentis.shorturl.domain;

import java.util.Random;

public class FakeRedirectExpiryPolicyBuilder {
    private long maxRedirections;

    public FakeRedirectExpiryPolicyBuilder withRandomMaxRedirections(){
        this.maxRedirections = new Random().nextInt(100) + 1; // we add 1 to make sure that policy will not expired at first use
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
