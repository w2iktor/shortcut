package pl.symentis.shorturl.service;

import java.net.URL;

public class DecodeShortcutRequest {
    private final String agent;
    private final String ipAddress;
    private final URL referer;
    private final String shortcut;

    DecodeShortcutRequest(String agent, String ipAddress, URL referer, String shortcut) {
        this.agent = agent;
        this.ipAddress = ipAddress;
        this.referer = referer;
        this.shortcut = shortcut;
    }

    public String getAgent() {
        return agent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public URL getReferer() {
        return referer;
    }

    public String getShortcut() {
        return shortcut;
    }
}
