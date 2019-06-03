package pl.symentis.shorturl.dao;

import java.util.Optional;

import pl.symentis.shorturl.domain.Shortcut;

public interface CustomizedShortcutRepository {
	
	long addShortcut(String accountName, String shortcut, Shortcut value);

	Optional<Shortcut> findByShortcut(String shortcut);

}
