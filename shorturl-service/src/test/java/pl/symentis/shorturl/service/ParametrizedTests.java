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
    @ParameterizedTest(name = "[{index}] {0} is an expired policy")
    @MethodSource(value = "expired_policies")
    void shortcut_with_already_expired_policy_is_expired(ExpiryPolicy expiredShortcutPolicy) {
        // when
        Shortcut shortcut = fakeShortcutBuilder()
            .withExpiryPolicy(expiredShortcutPolicy)
            .build();

        // then
        ExtendedShortcutAssert.assertThat(shortcut)
            .hasExpired();
    }

    private static Stream<ExpiryPolicy> expired_policies() {
        return Stream.of(fakeExpiryPolicyBuilder()
                .withRedirectPolicy()
                .withMaxRedirections(-1)
                .build(),
            fakeExpiryPolicyBuilder()
                .withDateTimePolicy()
                .withPastValidUntil()
                .build()
        );
    }
}
