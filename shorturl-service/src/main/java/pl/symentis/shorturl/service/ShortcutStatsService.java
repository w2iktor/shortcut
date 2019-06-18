package pl.symentis.shorturl.service;

import static pl.symentis.shorturl.domain.ShortcutStatsBuilder.shortcutStatsBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.symentis.shorturl.dao.ClickRepository;
import pl.symentis.shorturl.dao.StatsSummary;
import pl.symentis.shorturl.domain.ShortcutStats;

public interface ShortcutStatsService {


    ShortcutStats getStats(String shortcut);
}
