package pl.symentis.shorturl.service;

import com.mongodb.MongoClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.symentis.shorturl.dao.AccountRepository;
import pl.symentis.shorturl.domain.Account;

import javax.validation.ValidationException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pl.symentis.shorturl.domain.FakeAccountBuilder.fakeAccountBuilder;
import static pl.symentis.shorturl.integration.assertions.ExtendedAccountAssert.assertThat;

@Testcontainers
@ExtendWith(SpringExtension.class)
// TODO show how to use profiles to not lunch tomcat and speed up tests, for your own good
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("integration")
public class AccountServiceTest {

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
    AccountsService sut;

    @Autowired
    AccountRepository accountRepository;

    @Test
    void create_account_returns_object_with_filled_all_properties() {
        // given
        Account expected = fakeAccountBuilder()
            .build();

        // when
        Account actual = sut.createAccount(expected);

        // then
        assertThat(actual)
            .hasName(expected.getName())
            .hasEmail(expected.getEmail())
            .hasTaxnumber(expected.getTaxnumber())
            .hasMaxShortcuts(expected.getMaxShortcuts());
    }

    @Test
    void newly_created_accoount_does_not_have_shortcuts(){
        // given
        Account account = fakeAccountBuilder()
                .build();
        accountRepository.save(account);

        // when
        Optional<Account> returnedAccount = sut.getAccount(account.getName());

        // then
        assertThat(returnedAccount.get())
                .hasNoShortcuts();
    }

    @Test
    void cannot_create_duplicated_account() {
        // given
        String duplicateName = "duplicate name";
        Account firstAccount = fakeAccountBuilder()
            .withName(duplicateName)
            .build();
        sut.createAccount(firstAccount);
        Account accountWithSameName = fakeAccountBuilder()
            .withName(duplicateName)
            .build();

        // when & then
        assertThatThrownBy(() -> sut.createAccount(accountWithSameName))
            .isInstanceOf(DuplicateAccountException.class)
            .hasMessage("Account with name: 'duplicate name' already exists");
    }
    @Test
    void cannot_create_account_with_non_positive_value_of_max_shortcuts() {
        // given
        Account account = fakeAccountBuilder()
            .withMaxShortcuts(0)
            .build();

        // when & then
        assertThatThrownBy(() -> sut.createAccount(account))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("Max shortcuts have to be a positive number");
    }

    @Test
    void account_name_should_not_be_null_nor_empty() {
        // given
        Account accountWithoutName = fakeAccountBuilder()
            .withName(null)
            .build();

        // when & then
        assertThatThrownBy(() -> sut.createAccount(accountWithoutName))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("Name in account cannot be empty");
    }

    @Test
    void get_account_for_non_existing_account_name_returns_empty_optional() {
        // when
        Optional<Account> result = sut.getAccount("NON_EXISTING_NAME");

        // then
        Assertions.assertThat(result)
            .isEmpty();
    }

    @Test
    void get_account_for_existing_account_name_returns_object_with_all_properties_properly_mapped() {
        // given
        Account expectedAccount = fakeAccountBuilder()
            .build();
        accountRepository.save(expectedAccount);

        // when
        Optional<Account> actualAccount = sut.getAccount(expectedAccount.getName());

        // then
        assertThat(actualAccount.get())
            .isSameAs(expectedAccount);
    }

    @Test
    void get_account_returns_previously_created_entity(){
        // given
        Account expected = fakeAccountBuilder()
            .build();

        // when
        Account createdAccount = sut.createAccount(expected);

        // then
        assertThat(createdAccount)
            .isNotNull();

        // when
        Optional<Account> actual = sut.getAccount(expected.getName());

        // then
        assertThat(actual.get())
            .isSameAs(expected);
    }

    @Test
    void get_account_which_was_removed_returns_empty_optional(){
        // given
        Account expected = fakeAccountBuilder()
            .build();
        accountRepository.save(expected);

        // when
        Optional<Account> savedAccount = sut.getAccount(expected.getName());

        // then
        assertThat(savedAccount.get())
            .isSameAs(expected);

        // when
        sut.removeAccount(expected.getName());
        // and
        Optional<Account> actual = sut.getAccount(expected.getName());

        // then
        Assertions.assertThat(actual)
            .isEmpty();
    }

    @Test
    void delete_non_existing_account_returns_false(){
        // when
        boolean removalResult = sut.removeAccount("NON_EXISTING_NAME");

        // then
        Assertions.assertThat(removalResult)
            .isFalse();
    }

    @Test
    void delete_existing_accout_removes_it_from_system(){
        // given
        Account expected = fakeAccountBuilder()
            .build();
        accountRepository.save(expected);

        // when
        boolean removalResult = sut.removeAccount(expected.getName());

        // then
        Assertions.assertThat(removalResult)
            .isTrue();
        // and
        Optional<Account> actual = accountRepository.findById(expected.getName());
        Assertions.assertThat(actual)
            .isEmpty();
    }
}
