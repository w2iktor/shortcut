package pl.symentis.shorturl.dao;

import java.util.Optional;

import pl.symentis.shorturl.domain.Shortcut;

public interface CustomizedShortcutRepository {
	
	long addShortcut(String accountName, Shortcut value);

	Optional<Shortcut> findByShortcut(String shortcut);

}
