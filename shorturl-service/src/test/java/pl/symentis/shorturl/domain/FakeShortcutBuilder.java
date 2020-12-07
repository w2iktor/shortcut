package pl.symentis.shorturl.domain;

import com.devskiller.jfairy.Fairy;
import org.apache.commons.lang3.RandomStringUtils;

import java.net.MalformedURLException;
import java.net.URL;

import static pl.symentis.shorturl.domain.FakeExpiryPolicyBuilder.fakeExpiryPolicyBuilder;
import static pl.symentis.shorturl.domain.ShortcutBuilder.shortcutBuilder;

public class FakeShortcutBuilder {
    private Fairy fairy = Fairy.create();
    private ShortcutBuilder shortcutBuilder;

    private FakeShortcutBuilder(){
        try {
            shortcutBuilder = shortcutBuilder()
                    .withShortcut(RandomStringUtils.randomAlphanumeric(16))
                    .withUrl(new URL(fairy.networkProducer().url(false)))
                    .withExpiryPolicy(fakeExpiryPolicyBuilder()
                            .withRandomExipiryPolicy()
                            .build());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static FakeShortcutBuilder fakeShortcutBuilder(){
        return new FakeShortcutBuilder();
    }

    public FakeShortcutBuilder withShortcut(String shortcut) {
        this.shortcutBuilder.withShortcut(shortcut);
        return this;
    }

    public FakeShortcutBuilder withUrl(URL url) {
        this.shortcutBuilder.withUrl(url);
        return this;
    }

    public FakeShortcutBuilder withUrl(String url) {
        try {
            this.shortcutBuilder.withUrl(new URL(url));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public FakeShortcutBuilder withExpiryPolicy(ExpiryPolicy expiryPolicy) {
        this.shortcutBuilder.withExpiryPolicy(expiryPolicy);
        return this;
    }

    public Shortcut build() {
        return shortcutBuilder.build();
    }

    public FakeShortcutBuilder withValidExpiryPolicy() {
        ExpiryPolicy validExpiryPolicy = fakeExpiryPolicyBuilder()
                .withRedirectPolicy()
                .withMaxRedirections(100)
                .build();
        this.shortcutBuilder.withExpiryPolicy(validExpiryPolicy);
        return this;
    }
}
