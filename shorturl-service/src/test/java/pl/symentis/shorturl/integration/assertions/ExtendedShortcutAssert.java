package pl.symentis.shorturl.integration.assertions;

import org.assertj.core.util.Objects;
import pl.symentis.shorturl.domain.*;

public class ExtendedShortcutAssert extends AbstractShortcutAssert<ExtendedShortcutAssert, Shortcut> {

    private static final String GENERAL_ERROR_MESSAGE = "\nExpecting expiryPolicy of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    private ExtendedShortcutAssert(Shortcut actual) {
        super(actual, ExtendedShortcutAssert.class);
    }

    public static ExtendedShortcutAssert assertThat(Shortcut actual){
        return new ExtendedShortcutAssert(actual);
    }

    public ExtendedShortcutAssert hasExpiryPolicySameAs(ExpiryPolicy expected){
        isNotNull();

        // null safe check
        ExpiryPolicy actualExpiryPolicy = actual.getExpiryPolicy();
        if (!Objects.areEqual(actualExpiryPolicy.getClass(), expected.getClass())) {
            failWithMessage(GENERAL_ERROR_MESSAGE, actual, expected.getClass().getSimpleName(), actualExpiryPolicy.getClass().getSimpleName());
        } else if(expected instanceof RedirectsExpiryPolicy){
            return hasRedirectExpiryPolicySameAs((RedirectsExpiryPolicy)actualExpiryPolicy);
        } else if(expected instanceof DateTimeExpiryPolicy){
            return hasDateTimeExpiryPolicySameAs((DateTimeExpiryPolicy) actualExpiryPolicy);
        } else {
            ExpiryPolicyAssert.assertThat(actualExpiryPolicy)
                    .isEqualToComparingFieldByFieldRecursively(expected);
        }
        return this;
    }

    private ExtendedShortcutAssert hasRedirectExpiryPolicySameAs(RedirectsExpiryPolicy expected) {
        isNotNull();

        ExpiryPolicy actualExpiryPolicy = actual.getExpiryPolicy();
        if (actualExpiryPolicy instanceof RedirectsExpiryPolicy){
            RedirectsExpiryPolicyAssert.assertThat((RedirectsExpiryPolicy) actualExpiryPolicy)
                    .hasMax(expected.getMax());
        } else {
            failWithMessage(GENERAL_ERROR_MESSAGE, actual, RedirectsExpiryPolicy.class.getSimpleName(), actualExpiryPolicy.getClass().getSimpleName());
        }
        return this;
    }

    private ExtendedShortcutAssert hasDateTimeExpiryPolicySameAs(DateTimeExpiryPolicy expected) {
        isNotNull();

        ExpiryPolicy actualExpiryPolicy = actual.getExpiryPolicy();
        if (actualExpiryPolicy instanceof DateTimeExpiryPolicy){
            DateTimeExpiryPolicyAssert.assertThat((DateTimeExpiryPolicy) actualExpiryPolicy)
                    .hasValidUntil(expected.getValidUntil());
        } else {
            failWithMessage(GENERAL_ERROR_MESSAGE, actual, DateTimeExpiryPolicy.class.getSimpleName(), actualExpiryPolicy.getClass().getSimpleName());
        }
        return this;
    }
}
