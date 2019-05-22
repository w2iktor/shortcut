package pl.symentis.shorturl.domain;

import java.util.Collections;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@TypeAlias("account")
@Document(collection="accounts")
public class Account {

	@Id
	private String name;
	private String email;
	private String taxnumber;
	private long maxShortcuts;
	private List<Shortcut> shortcuts;
	
	public Account() {
		
	}
	
	Account(String name, String email, String taxnumber, long maxShortcuts) {
		super();
		this.name = name;
		this.email = email;
		this.taxnumber = taxnumber;
		this.maxShortcuts = maxShortcuts;
		this.shortcuts = Collections.emptyList();
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
