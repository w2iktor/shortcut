package pl.symentis.shorturl.domain;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.company.Company;

import java.util.Random;

public class FakeAccountBuilder {
    Fairy fairy = Fairy.create();
    AccountBuilder accountBuilder;

    private FakeAccountBuilder(){
        Company company = fairy.company();
        accountBuilder = AccountBuilder.accountBuilder()
            .withEmail(company.getEmail())
            .withName(company.getName())
            .withTaxnumber(company.getVatIdentificationNumber())
            .withMaxShortcuts(new Random().nextInt(100) + 1 );   //hence min value for 'maxShortcuts' is 1 and minimum value of 'nextInt' is 0, we have to add '1' to result
    }

    public static FakeAccountBuilder fakeAccountBuilder() {
        return new FakeAccountBuilder();
    }

    public FakeAccountBuilder withName(String name) {
        accountBuilder.withName(name);
        return this;
    }

    public FakeAccountBuilder withEmail(String email) {
        accountBuilder.withEmail(email);
        return this;
    }

    public FakeAccountBuilder withTaxnumber(String taxnumber) {
        accountBuilder.withTaxnumber(taxnumber);
        return this;
    }

    public FakeAccountBuilder withMaxShortcuts(long maxShortcuts) {
        accountBuilder.withMaxShortcuts(maxShortcuts);
        return this;
    }

    public Account build() {
        return accountBuilder.build();
    }
}
