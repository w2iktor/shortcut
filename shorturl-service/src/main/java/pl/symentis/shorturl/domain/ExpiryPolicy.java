package pl.symentis.shorturl.domain;

public interface ExpiryPolicy {
	
	boolean isValidShortcut(Shortcut shortcut);

}
