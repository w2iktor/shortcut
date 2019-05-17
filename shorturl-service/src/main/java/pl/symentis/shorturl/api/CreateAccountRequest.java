package pl.symentis.shorturl.api;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateAccountRequest {
	
	@NotEmpty
	private final String name;
	
	@Email
	private final String email;
	
	@NotEmpty
	private final String taxnumber;
	
	@Range(min=1)
	private final long maxShortcuts;
	
	@JsonCreator
	public CreateAccountRequest(
			@JsonProperty("name") String name, 
			@JsonProperty("email") String email, 
			@JsonProperty("taxnumber") String taxnumber,
			@JsonProperty("maxShortcuts") long maxShortcuts) {
		super();
		this.name = name;
		this.email = email;
		this.taxnumber = taxnumber;
		this.maxShortcuts = maxShortcuts;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getTaxnumber() {
		return taxnumber;
	}

	public long getMaxShortcuts() {
		return maxShortcuts;
	}
	
}
