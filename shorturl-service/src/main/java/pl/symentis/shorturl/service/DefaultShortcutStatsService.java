package pl.symentis.shorturl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.symentis.shorturl.dao.ClickRepository;
import pl.symentis.shorturl.dao.StatsSummary;
import pl.symentis.shorturl.domain.ShortcutStats;

import static pl.symentis.shorturl.domain.ShortcutStatsBuilder.shortcutStatsBuilder;

@Service
public class DefaultShortcutStatsService implements ShortcutStatsService{
    private final ClickRepository clickRepository;

    @Autowired
    DefaultShortcutStatsService(ClickRepository clickRepository){
        this.clickRepository = clickRepository;
    }

    @Override
    public ShortcutStats getStats(String shortcut){
        StatsSummary agentDetails = clickRepository.getAgentDetails(shortcut);
        return shortcutStatsBuilder()
                .withClicks(clickRepository.countClicks(shortcut))
                .withAgents(agentDetails.statsCounters)
                .build();
    }
}
