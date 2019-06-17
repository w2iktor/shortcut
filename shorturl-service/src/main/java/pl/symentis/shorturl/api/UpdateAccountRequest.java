package pl.symentis.shorturl.api;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateAccountRequest {

	@Range(min = 1)
	private final long maxShortcuts;

	@Email
	private final String email;

	@NotEmpty
	private final String taxnumber;

	@NotNull
	private final ExpiryPolicyData expiry;

	public UpdateAccountRequest(
			@JsonProperty("maxShortcuts") long maxShortcuts, 
			@JsonProperty("email") String email, 
			@JsonProperty("taxnumber") String taxnumber, 
			@JsonProperty("expiry") ExpiryPolicyData expiry) {
		super();
		this.maxShortcuts = maxShortcuts;
		this.email = email;
		this.taxnumber = taxnumber;
		this.expiry = expiry;
	}
	
}
