package pl.symentis.shorturl.dao;

import pl.symentis.shorturl.domain.Shortcut;

public interface CustomizedShortcutRepository {
	
	void addShortcut(String accountName, String shortcut, Shortcut value);

}
