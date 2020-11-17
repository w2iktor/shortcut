package pl.symentis.shorturl.domain;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.company.Company;

import java.util.Random;

import static pl.symentis.shorturl.domain.FakeExpiryPolicyBuilder.fakeExpiryPolicyBuilder;

public class FakeAccountBuilder extends AccountBuilder{
    Fairy fairy = Fairy.create();

    FakeAccountBuilder(){
        Company company = fairy.company();
        withEmail(company.getEmail())
            .withName(company.getName())
            .withTaxnumber(company.getVatIdentificationNumber())
            .withDefaultExpiryPolicy(fakeExpiryPolicyBuilder()
                .withRandomExipiryPolicy()
                .build())
            .withMaxShortcuts(new Random().nextInt(100) + 1 );   // hence min value for 'maxShortcuts' is 1
                                                                        // and minimum value of 'nextInt' is 0,
                                                                        // we have to add '1' to result
    }
    public static FakeAccountBuilder fakeAccountBuilder() {
        return new FakeAccountBuilder();
    }
}
