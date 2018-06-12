package pl.symentis.shorturl.domain;

import org.bson.types.ObjectId;

import java.net.URL;
import java.time.LocalDateTime;

public final class ClickBuilder {
	
    private ObjectId id;
    private String shortcut;
    private LocalDateTime localDateTime;
    private String ipAddress;
    private String os;
    private String agent;
    private URL referer;

    private ClickBuilder() {
    }

    public static ClickBuilder clickBuilder() {
        return new ClickBuilder();
    }

    public ClickBuilder withId(ObjectId id) {
        this.id = id;
        return this;
    }

    public ClickBuilder withShortcut(String shortcut) {
        this.shortcut = shortcut;
        return this;
    }

    public ClickBuilder withLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
        return this;
    }

    public ClickBuilder withIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public ClickBuilder withOs(String os) {
        this.os = os;
        return this;
    }

    public ClickBuilder withAgent(String agent) {
        this.agent = agent;
        return this;
    }

    public ClickBuilder withReferer(URL referer) {
        this.referer = referer;
        return this;
    }

    public Click build() {
        return new Click(id, shortcut, localDateTime, ipAddress, os, agent, referer);
    }
}
