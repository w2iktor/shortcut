package pl.symentis.shorturl.api;

import java.util.List;

public final class ShortcutStatsResponseBuilder {
    private int clicks;
    private List<Stats> agents;
    private List<Stats> oses;

    private ShortcutStatsResponseBuilder() {
    }

    public static ShortcutStatsResponseBuilder shortcutStatsResponseBuilder() {
        return new ShortcutStatsResponseBuilder();
    }

    public ShortcutStatsResponseBuilder withClicks(int clicks) {
        this.clicks = clicks;
        return this;
    }

    public ShortcutStatsResponseBuilder withAgents(List<Stats> agents) {
        this.agents = agents;
        return this;
    }

    public ShortcutStatsResponseBuilder withOses(List<Stats> oses) {
        this.oses = oses;
        return this;
    }

    public ShortcutStatsResponse build() {
        return new ShortcutStatsResponse(clicks, agents, oses);
    }
}
