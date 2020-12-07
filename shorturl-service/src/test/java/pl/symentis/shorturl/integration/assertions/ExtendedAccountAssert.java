package pl.symentis.shorturl.integration.assertions;

import org.assertj.core.api.Assertions;
import pl.symentis.shorturl.domain.AbstractAccountAssert;
import pl.symentis.shorturl.domain.Account;
import pl.symentis.shorturl.domain.Shortcut;

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

    @Override
    public ExtendedAccountAssert hasOnlyShortcuts(Shortcut ... expectedShortcut) {
        Assertions.assertThat(actual.getShortcuts())
            .usingRecursiveFieldByFieldElementComparator()
            .containsOnly(expectedShortcut);
        return this;
    }
}
