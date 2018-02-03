package pl.symentis.shorturl.domain;

public class Account {

	private String name;
	private String email;
	private String taxnumber;
	
	public Account(String name, String email, String taxnumber) {
		super();
		this.name = name;
		this.email = email;
		this.taxnumber = taxnumber;
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
	
}
