package pl.symentis.shorturl.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateShortcutValidityRequest {

	@NotNull
	private final ExpiryPolicy expiry;

	@JsonCreator
	public UpdateShortcutValidityRequest(@JsonProperty("expiry") ExpiryPolicy expiry) {
		this.expiry = expiry;
	}

	public ExpiryPolicy getExpiry() {
		return expiry;
	}

}
