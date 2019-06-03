package pl.symentis.shorturl.service;

import com.mongodb.MongoClient;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.symentis.shorturl.api.ExpiryPolicyData;
import pl.symentis.shorturl.dao.AccountRepository;
import pl.symentis.shorturl.dao.ShortcutRepository;
import pl.symentis.shorturl.domain.*;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static pl.symentis.shorturl.domain.FakeAccountBuilder.fakeAccountBuilder;
import static pl.symentis.shorturl.domain.FakeExpiryPolicyBuilder.fakeExpiryPolicyBuilder;
import static pl.symentis.shorturl.integration.assertions.ExtendedShortcutAssert.assertThat;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("integration")
class ShortcutsServiceTest {

    @Container
    public static GenericContainer<?> mongo = new GenericContainer<>("mongo:3.4-xenial")
            .withExposedPorts(27017)
            .waitingFor(Wait.forListeningPort());

    @TestConfiguration
    public static class MongoOverrides {
        @Bean
        public MongoClient mongoClient() {
            Integer mongoPort = mongo.getMappedPort(27017);
            return new MongoClient("localhost", mongoPort);
        }
    }

    @Autowired
    ShortcutsService sut;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ShortcutRepository shortcutRepository;

    @Test
    void shorcut_without_expiration_policy_get_accounts_default() throws NoSuchAlgorithmException {
        // given
        Account account = saveRandomAccount();
        ExpiryPolicyData expirationPolicy = null;

        // when
        String generatedShortcut = sut.generate(account.getName(), FakeUrl.generateRandomUrl(), expirationPolicy);

        // then
        Optional<Shortcut> actualShortcut = shortcutRepository
                .findByShortcut(generatedShortcut);

        Assertions.assertThat(actualShortcut)
                .isNotEmpty();

        assertThat(actualShortcut.get())
                .hasExpiryPolicySameAs(account.getDefaultExpiryPolicy());
    }

    @NotNull
    private Account saveRandomAccount() {
        Account account = fakeAccountBuilder()
                .build();
        accountRepository.save(account);
        return account;
    }

}