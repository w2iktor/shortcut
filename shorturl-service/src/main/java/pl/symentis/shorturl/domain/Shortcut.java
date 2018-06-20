package pl.symentis.shorturl.domain;

import java.net.URL;

import org.springframework.data.mongodb.core.mapping.Field;

public class Shortcut {

	private String shortcut;
	private URL url;
	private ExpiryPolicy expiryPolicy;
	@Field("counter")
	private long decodeCounter;

	public Shortcut(String shortcut, URL url, ExpiryPolicy expiryPolicy, long decodeCounter) {
		this.shortcut = shortcut;
		this.url = url;
		this.expiryPolicy = expiryPolicy;
		this.decodeCounter = decodeCounter;
	}

	public String getShortcut() {
		return shortcut;
	}

	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public ExpiryPolicy getExpiryPolicy() {
		return expiryPolicy;
	}

	public void setExpiryPolicy(ExpiryPolicy expiryPolicy) {
		this.expiryPolicy = expiryPolicy;
	}

	public long getDecodeCounter() {
		return decodeCounter;
	}

	public void setDecodeCounter(long decodeCounter) {
		this.decodeCounter = decodeCounter;
	}
	
	

}