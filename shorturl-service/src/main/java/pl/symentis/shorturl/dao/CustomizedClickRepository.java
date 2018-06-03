package pl.symentis.shorturl.dao;

public interface CustomizedClickRepository {
    int countClicks(String shortcut);

    StatsSummary getAgentDetails(String shortcut);
}
