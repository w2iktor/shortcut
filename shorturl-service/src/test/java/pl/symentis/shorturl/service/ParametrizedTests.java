package pl.symentis.shorturl.service;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pl.symentis.shorturl.domain.ExpiryPolicy;
import pl.symentis.shorturl.domain.Shortcut;
import pl.symentis.shorturl.integration.assertions.ExtendedShortcutAssert;

import java.util.stream.Stream;

import static pl.symentis.shorturl.domain.FakeExpiryPolicyBuilder.fakeExpiryPolicyBuilder;
import static pl.symentis.shorturl.domain.FakeShortcutBuilder.fakeShortcutBuilder;

public class ParametrizedTests {
    void shortcut_with_already_expired_policy_is_expired(ExpiryPolicy expiredShortcutPolicy) {
    }

    private static Stream<ExpiryPolicy> expired_policies() {
        return Stream.of(
                /* return expired date-time and redirection policies */
        );
    }
}
