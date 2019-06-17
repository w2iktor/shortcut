package pl.symentis.shorturl.api;

public final class StatsBuilder {
    private String id;
    private int total;

    private StatsBuilder() {
    }

    public static StatsBuilder statsBuilder() {
        return new StatsBuilder();
    }

    public StatsBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public StatsBuilder withTotal(int total) {
        this.total = total;
        return this;
    }

    public Stats build() {
        return new Stats(id, total);
    }
}
