package pl.symentis.shorturl.service;

import com.mongodb.MongoClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
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
import static pl.symentis.shorturl.domain.FakeShortcutBuilder.fakeShortcutBuilder;
import static pl.symentis.shorturl.integration.assertions.ExtendedShortcutAssert.assertThat;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
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
    void generated_shortcut_without_expiration_policy_get_accounts_default() throws NoSuchAlgorithmException {
        // given
        Account account = saveRandomAccount();
    }

    @Test
    void decoding_shortcut_increment_its_decode_counter() {
        // given
        Account account = saveRandomAccount();
    }

    @ParameterizedTest
    @MethodSource(value = "pl.symentis.shorturl.service.ParametrizedTests#expired_policies")
    void decoding_expired_shortcut_returns_empty_response(ExpiryPolicy expiredShortcutPolicy){
        // given
        Account account = saveRandomAccount();
    }

    private Account saveRandomAccount() {
        Account account = fakeAccountBuilder()
                .build();
        accountRepository.save(account);
        return account;
    }

}