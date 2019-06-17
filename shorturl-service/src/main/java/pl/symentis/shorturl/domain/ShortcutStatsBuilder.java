package pl.symentis.shorturl.domain;

import pl.symentis.shorturl.dao.StatsCounter;

import java.util.List;

public final class ShortcutStatsBuilder {
    private int clicks;
    private List<StatsCounter> agents;

    private ShortcutStatsBuilder() {
    }

    public static ShortcutStatsBuilder shortcutStatsBuilder() {
        return new ShortcutStatsBuilder();
    }

    public ShortcutStatsBuilder withClicks(int clicks) {
        this.clicks = clicks;
        return this;
    }

    public ShortcutStatsBuilder withAgents(List<StatsCounter> agents) {
        this.agents = agents;
        return this;
    }

    public ShortcutStats build() {
        return new ShortcutStats(clicks, agents);
    }
}
