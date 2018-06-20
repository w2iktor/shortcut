package pl.symentis.shorturl.service;

import static pl.symentis.shorturl.domain.ShortcutStatsBuilder.shortcutStatsBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.symentis.shorturl.dao.ClickRepository;
import pl.symentis.shorturl.dao.StatsSummary;
import pl.symentis.shorturl.domain.ShortcutStats;

@Service
public class ShortcutStatsService {

    private final ClickRepository clickRepository;

    @Autowired
    ShortcutStatsService(ClickRepository clickRepository){
        this.clickRepository = clickRepository;
    }

    public ShortcutStats getStats(String shortcut){
        StatsSummary agentDetails = clickRepository.getAgentDetails(shortcut);
        return shortcutStatsBuilder()
                .withClicks(clickRepository.countClicks(shortcut))
                .withAgents(agentDetails.statsCounters)
                .build();
    }
}
