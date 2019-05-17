package pl.symentis.shorturl.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import pl.symentis.shorturl.domain.Account;

public class GetAccountResponse {
	
	private final String name;
	private final String email;
	private final String taxnumber;
	private final long maxShortcuts;
	private final long currentShortcuts;
	
	@JsonCreator
	public GetAccountResponse(
			@JsonProperty("name") String name, 
			@JsonProperty("email") String email, 
			@JsonProperty("taxnumber") String taxnumber,	
			@JsonProperty("maxShortcuts") long maxShortcuts,
			@JsonProperty("currentShortcuts") long currentShortcuts) {
		super();
		this.name = name;
		this.email = email;
		this.taxnumber = taxnumber;
		this.maxShortcuts = maxShortcuts;
		this.currentShortcuts = currentShortcuts;
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
	
	public long getCurrentShortcuts() {
		return currentShortcuts;
	}
	public static GetAccountResponse valueOf(Account account) {
		return new GetAccountResponse(
				account.getName(),
				account.getEmail(),
				account.getTaxnumber(),
				account.getMaxShortcuts(),
				account.getCurrentShortcuts());
	}

}
