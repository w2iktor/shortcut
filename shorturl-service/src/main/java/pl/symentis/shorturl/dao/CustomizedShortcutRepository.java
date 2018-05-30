package pl.symentis.shorturl.dao;

import java.util.Optional;

import pl.symentis.shorturl.domain.Shortcut;

public interface CustomizedShortcutRepository {
	
	void addShortcut(String accountName, String shortcut, Shortcut value);

	Optional<Shortcut> getURL(String shortcut);

}
