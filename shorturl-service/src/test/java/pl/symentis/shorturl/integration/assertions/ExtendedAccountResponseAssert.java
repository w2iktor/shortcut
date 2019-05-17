package pl.symentis.shorturl.integration.assertions;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import pl.symentis.shorturl.api.AbstractGetAccountResponseAssert;
import pl.symentis.shorturl.api.GetAccountResponse;

public class ExtendedAccountResponseAssert extends AbstractGetAccountResponseAssert<ExtendedAccountResponseAssert, GetAccountResponse>{
    protected ExtendedAccountResponseAssert(GetAccountResponse actual) {
        super(actual, ExtendedAccountResponseAssert.class);
    }

    public static ExtendedAccountResponseAssert assertThat(GetAccountResponse actual) {
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
