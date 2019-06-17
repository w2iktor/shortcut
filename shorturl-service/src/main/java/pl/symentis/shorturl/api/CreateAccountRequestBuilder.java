package pl.symentis.shorturl.api;

public final class CreateAccountRequestBuilder {
    private String name;
    private String email;
    private String taxnumber;
    private long maxShortcuts;

    private CreateAccountRequestBuilder() {
    }

    public static CreateAccountRequestBuilder createAccountRequestBuilder() {
        return new CreateAccountRequestBuilder();
    }

    public CreateAccountRequestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CreateAccountRequestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public CreateAccountRequestBuilder withTaxnumber(String taxnumber) {
        this.taxnumber = taxnumber;
        return this;
    }

    public CreateAccountRequestBuilder withMaxShortcuts(long maxShortcuts) {
        this.maxShortcuts = maxShortcuts;
        return this;
    }

    public CreateAccountRequest build() {
        return new CreateAccountRequest(name, email, taxnumber, maxShortcuts);
    }
}
