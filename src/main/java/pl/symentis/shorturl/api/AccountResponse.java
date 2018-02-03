package pl.symentis.shorturl.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import pl.symentis.shorturl.domain.Account;

public class AccountResponse {
	
	private final String name;
	private final String email;
	private final String taxnumber;
	
	@JsonCreator
	public AccountResponse(
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
	
	public static AccountResponse valueOf(Account account) {
		return new AccountResponse(account.getName(),account.getEmail(),account.getTaxnumber());
	}

}
