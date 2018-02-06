package pl.symentis.shorturl.api;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
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
	private final ExpiryPolicy expiry;

	public UpdateAccountRequest(
			@JsonProperty("maxShortcuts") long maxShortcuts, 
			@JsonProperty("email") String email, 
			@JsonProperty("taxnumber") String taxnumber, 
			@JsonProperty("expiry") ExpiryPolicy expiry) {
		super();
		this.maxShortcuts = maxShortcuts;
		this.email = email;
		this.taxnumber = taxnumber;
		this.expiry = expiry;
	}
	
	

}
