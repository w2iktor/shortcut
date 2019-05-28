package pl.symentis.shorturl.integration.assertions;

import pl.symentis.shorturl.domain.AbstractAccountAssert;
import pl.symentis.shorturl.domain.Account;

public class ExtendedAccountAssert extends AbstractAccountAssert<ExtendedAccountAssert, Account> {

    private ExtendedAccountAssert(Account actual) {
        super(actual, ExtendedAccountAssert.class);
    }

    public static ExtendedAccountAssert assertThat(Account actual){
        return new ExtendedAccountAssert(actual);
    }

    public ExtendedAccountAssert isSameAs(Account expected){
        return hasName(expected.getName())
            .hasEmail(expected.getEmail())
            .hasTaxnumber(expected.getTaxnumber())
            .hasMaxShortcuts(expected.getMaxShortcuts());
    }
}
