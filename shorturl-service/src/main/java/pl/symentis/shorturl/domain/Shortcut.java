package pl.symentis.shorturl.domain;

import java.net.URL;

import org.springframework.data.mongodb.core.mapping.Field;

public class Shortcut {

	private String shortcut;
	private URL url;
	private ExpiryPolicy expiryPolicy;
	@Field("counter")
	private long decodeCounter;

	Shortcut(String shortcut, URL url, ExpiryPolicy expiryPolicy) {
		this.shortcut = shortcut;
		this.url = url;
		this.expiryPolicy = expiryPolicy;
		this.decodeCounter = 0L;
	}

	public String getShortcut() {
		return shortcut;
	}

	public URL getUrl() {
		return url;
	}

	public ExpiryPolicy getExpiryPolicy() {
		return expiryPolicy;
	}

	public long getDecodeCounter() {
		return decodeCounter;
	}

	public void incrementDecodeCounter() {
		this.decodeCounter++;
	}
	
	

}