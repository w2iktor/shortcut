package pl.symentis.shorturl.api;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateShortcutRequest {

	private final URL url;
	private final ExpiryPolicy expiry;

	@JsonCreator
	public CreateShortcutRequest(
			@JsonProperty("url") URL url,
			@JsonProperty("expiry") ExpiryPolicy expiry) {
		super();
		this.url = url;
		this.expiry = expiry;
	}

	public URL getUrl() {
		return url;
	}

	public ExpiryPolicy getExpiry() {
		return expiry;
	}

}
