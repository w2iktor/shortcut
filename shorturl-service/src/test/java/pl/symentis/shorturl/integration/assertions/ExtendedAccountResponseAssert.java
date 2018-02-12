package pl.symentis.shorturl.integration.assertions;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import pl.symentis.shorturl.api.AbstractAccountResponseAssert;
import pl.symentis.shorturl.api.AccountResponse;

public class ExtendedAccountResponseAssert extends AbstractAccountResponseAssert<ExtendedAccountResponseAssert, AccountResponse>{
    protected ExtendedAccountResponseAssert(AccountResponse actual) {
        super(actual, ExtendedAccountResponseAssert.class);
    }

    public static ExtendedAccountResponseAssert assertThat(AccountResponse actual) {
        return new ExtendedAccountResponseAssert(actual);
    }

    public ExtendedAccountResponseAssert hasNoRegisteredShortcuts() {
        isNotNull();

        String assertjErrorMessage = "\nExpecting  currentShortcuts of:\n  <%s>\nto be: 0\nbut was:\n  <%s>";

        long actualCurrentShortcuts = actual.getCurrentShortcuts();
        if (actualCurrentShortcuts != 0) {
            String actualStringForm = ReflectionToStringBuilder.toString(actual);
            failWithMessage(assertjErrorMessage, actualStringForm, actualCurrentShortcuts);
        }

        return this;
    }
}
