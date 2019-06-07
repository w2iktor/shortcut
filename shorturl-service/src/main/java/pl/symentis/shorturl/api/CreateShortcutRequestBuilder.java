package pl.symentis.shorturl.api;

import java.net.URL;

public final class CreateShortcutRequestBuilder {
    private URL url;
    private ExpiryPolicyData expiry;

    private CreateShortcutRequestBuilder() {
    }

    public static CreateShortcutRequestBuilder createShortcutRequestBuilder() {
        return new CreateShortcutRequestBuilder();
    }

    public CreateShortcutRequestBuilder withUrl(URL url) {
        this.url = url;
        return this;
    }

    public CreateShortcutRequestBuilder withExpiry(ExpiryPolicyData expiry) {
        this.expiry = expiry;
        return this;
    }

    public CreateShortcutRequest build() {
        return new CreateShortcutRequest(url, expiry);
    }
}
