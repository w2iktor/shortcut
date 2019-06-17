package pl.symentis.shorturl.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateShortcutValidityRequest {

	@NotNull
	private final ExpiryPolicyData expiry;

	@JsonCreator
	public UpdateShortcutValidityRequest(@JsonProperty("expiry") ExpiryPolicyData expiry) {
		this.expiry = expiry;
	}

	public ExpiryPolicyData getExpiry() {
		return expiry;
	}

}
