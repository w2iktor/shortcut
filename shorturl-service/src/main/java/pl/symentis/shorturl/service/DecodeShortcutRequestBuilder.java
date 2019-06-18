package pl.symentis.shorturl.service;

import com.google.common.base.Strings;

import java.net.MalformedURLException;
import java.net.URL;

public final class DecodeShortcutRequestBuilder {
    private String agent;
    private String ipAddress;
    private URL referer;
    private String shortcut;

    private DecodeShortcutRequestBuilder() {
    }

    public static DecodeShortcutRequestBuilder decodeShortcutRequestBuilder() {
        return new DecodeShortcutRequestBuilder();
    }

    public DecodeShortcutRequestBuilder withAgent(String agent) {
        this.agent = agent;
        return this;
    }

    public DecodeShortcutRequestBuilder withIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public DecodeShortcutRequestBuilder withReferer(String referer) {
        if(!Strings.isNullOrEmpty(referer)) {
            try {
                this.referer = new URL(referer);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }

    public DecodeShortcutRequestBuilder withReferer(URL referer) {
        this.referer = referer;
        return this;
    }

    public DecodeShortcutRequestBuilder withShortcut(String shortcut) {
        this.shortcut = shortcut;
        return this;
    }

    public DecodeShortcutRequest build() {
        return new DecodeShortcutRequest(agent, ipAddress, referer, shortcut);
    }
}
