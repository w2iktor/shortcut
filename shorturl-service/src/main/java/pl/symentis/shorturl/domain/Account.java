package pl.symentis.shorturl.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="accounts")
public class Account {

	@Id
	private String name;
	private String email;
	private String taxnumber;
	private long maxShortcuts;
	private long currentShortcuts;
	private List<Shortcut> shortcuts;
	
	public Account() {
		
	}
	
	public Account(String name, String email, String taxnumber, long maxShortcuts, List<Shortcut> shortcuts) {
		super();
		this.name = name;
		this.email = email;
		this.taxnumber = taxnumber;
		this.maxShortcuts = maxShortcuts;
		this.shortcuts = shortcuts;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTaxnumber() {
		return taxnumber;
	}

	public void setTaxnumber(String taxnumber) {
		this.taxnumber = taxnumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public long getMaxShortcuts() {
		return maxShortcuts;
	}

	public long getCurrentShortcuts() {
		return currentShortcuts;
	}

	public List<Shortcut> getShortcuts() {
		return shortcuts;
	}

	public void setShortcuts(List<Shortcut> shortcuts) {
		this.shortcuts = shortcuts;
	}
	
}
