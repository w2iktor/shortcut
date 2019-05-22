package pl.symentis.shorturl.domain;

public final class AccountBuilder {
    private String name;
    private String email;
    private String taxnumber;
    private long maxShortcuts;

    private AccountBuilder() {
    }

    public static AccountBuilder accountBuilder() {
        return new AccountBuilder();
    }

    public AccountBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AccountBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public AccountBuilder withTaxnumber(String taxnumber) {
        this.taxnumber = taxnumber;
        return this;
    }

    public AccountBuilder withMaxShortcuts(long maxShortcuts) {
        this.maxShortcuts = maxShortcuts;
        return this;
    }

    public Account build() {
        return new Account(name, email, taxnumber, maxShortcuts);
    }
}
