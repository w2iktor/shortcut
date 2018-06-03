package pl.symentis.shorturl.api;

public class Stats {
    private String id;
    private int total;

    public Stats(String id, int total) {
        this.id = id;
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public String getId() {
        return id;
    }
}
