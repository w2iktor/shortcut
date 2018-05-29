package pl.symentis.shorturl.api;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateShortcutRequest {

	private final URL url;
	private final ExpiryPolicyData expiry;

	@JsonCreator
	public CreateShortcutRequest(
			@JsonProperty("url") URL url,
			@JsonProperty("expiry") ExpiryPolicyData expiry) {
		super();
		this.url = url;
		this.expiry = expiry;
	}

	public URL getUrl() {
		return url;
	}

	public ExpiryPolicyData getExpiry() {
		return expiry;
	}

}
