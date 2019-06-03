package pl.symentis.shorturl.domain;

import java.net.URL;

public final class ShortcutBuilder {
    private String shortcut;
    private URL url;
    private ExpiryPolicy expiryPolicy;

    private ShortcutBuilder() {
    }

    public static ShortcutBuilder shortcutBuilder() {
        return new ShortcutBuilder();
    }

    public ShortcutBuilder withShortcut(String shortcut) {
        this.shortcut = shortcut;
        return this;
    }

    public ShortcutBuilder withUrl(URL url) {
        this.url = url;
        return this;
    }

    public ShortcutBuilder withExpiryPolicy(ExpiryPolicy expiryPolicy) {
        this.expiryPolicy = expiryPolicy;
        return this;
    }

    public Shortcut build() {
        return new Shortcut(shortcut, url, expiryPolicy);
    }
}
