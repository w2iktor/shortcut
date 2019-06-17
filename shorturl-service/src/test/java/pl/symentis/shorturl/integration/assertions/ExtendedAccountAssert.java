package pl.symentis.shorturl.integration.assertions;

import pl.symentis.shorturl.domain.AbstractAccountAssert;
import pl.symentis.shorturl.domain.Account;

public class ExtendedAccountAssert  {

    private ExtendedAccountAssert(Account actual) {
    }

    public static ExtendedAccountAssert assertThat(Account actual){
        return new ExtendedAccountAssert(actual);
    }

    public ExtendedAccountAssert isSameAs(Account expected){
        return this;
    }
}
