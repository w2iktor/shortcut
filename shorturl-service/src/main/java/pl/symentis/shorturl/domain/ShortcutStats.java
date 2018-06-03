package pl.symentis.shorturl.domain;

import pl.symentis.shorturl.dao.StatsCounter;

import java.util.List;

public class ShortcutStats {
    private int clicks;
    private List<StatsCounter> agents;

    public ShortcutStats(int clicks, List<StatsCounter> agents) {
        this.clicks = clicks;
        this.agents = agents;
    }

    public int getClicks() {
        return clicks;
    }

    public List<StatsCounter> getAgents() {
        return agents;
    }
}
