package pl.symentis.shorturl.domain;

import java.net.URL;

public class Shortcut {

	private String shortcut;
	private URL url;
	private ExpiryPolicy expiryPolicy;

	public Shortcut(String shortcut, URL url, ExpiryPolicy expiryPolicy) {
		this.shortcut = shortcut;
		this.url = url;
		this.expiryPolicy = expiryPolicy;
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

}