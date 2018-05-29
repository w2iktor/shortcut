package pl.symentis.shorturl.domain;

import java.net.URL;

public class Shortcut {

	private URL url;
	private ExpiryPolicy expiryPolicy;

	public Shortcut( URL url, ExpiryPolicy expiryPolicy) {
		this.url = url;
		this.expiryPolicy = expiryPolicy;
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