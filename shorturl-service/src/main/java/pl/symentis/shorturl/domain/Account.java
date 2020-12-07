package pl.symentis.shorturl.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@TypeAlias("account")
@Document(collection="accounts")
public class Account {

	@Id
	@NotBlank(message = "Name in account cannot be empty")
	private String name;
	private String email;
	private String taxnumber;
	@Min(value = 1, message = "Max shortcuts have to be a positive number")
	private long maxShortcuts;
	private List<Shortcut> shortcuts = new ArrayList<>();
	private ExpiryPolicy defaultExpiryPolicy;
	
	private Account() {
	}
	
	public Account(String name, String email, String taxnumber, long maxShortcuts, ExpiryPolicy defaultExpiryPolicy) {
		this.name = name;
		this.email = email;
		this.taxnumber = taxnumber;
		this.maxShortcuts = maxShortcuts;
		this.defaultExpiryPolicy = defaultExpiryPolicy;
	}

	public String getName() {
		return name;
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

	public ExpiryPolicy getDefaultExpiryPolicy() {
		return defaultExpiryPolicy;
	}

	public void setDefaultExpiryPolicy(ExpiryPolicy defaultExpiryPolicy) {
		this.defaultExpiryPolicy = defaultExpiryPolicy;
	}

	public long getMaxShortcuts() {
		return maxShortcuts;
	}

	public void setMaxShortcuts(long maxShortcuts) {
		this.maxShortcuts = maxShortcuts;
	}

	public long getCurrentShortcuts() {
		return shortcuts.size();
	}

	public List<Shortcut> getShortcuts() {
		return shortcuts;
	}

	public void setShortcuts(List<Shortcut> shortcuts) {
		this.shortcuts = shortcuts;
	}

	public void addShortcuts(List<Shortcut> shortcuts) {
		this.shortcuts.addAll(shortcuts);
	}

	public void addShortcut(Shortcut shortcut) {
		this.shortcuts.add(shortcut);
	}

}
