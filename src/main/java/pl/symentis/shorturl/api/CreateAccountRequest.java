package pl.symentis.shorturl.api;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateAccountRequest {
	
	
	@NotEmpty
	private final String name;
	@Email
	private final String email;
	@NotEmpty
	private final String taxnumber;
	
	@JsonCreator
	public CreateAccountRequest(
			@JsonProperty("name") String name, 
			@JsonProperty("email") String email, 
			@JsonProperty("taxnumber") String taxnumber) {
		super();
		this.name = name;
		this.email = email;
		this.taxnumber = taxnumber;
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
	
}
