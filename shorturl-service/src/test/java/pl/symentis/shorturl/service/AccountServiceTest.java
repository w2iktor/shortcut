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
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.symentis.shorturl.domain.FakeAccountBuilder.fakeAccountBuilder;
import static pl.symentis.shorturl.integration.assertions.ExtendedAccountAssert.assertThat;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class AccountServiceTest {

    @Container
    public static GenericContainer<?> mongo = new GenericContainer<>("mongo:3.4-xenial")
        .withExposedPorts(27017)
        .waitingFor(Wait.forListeningPort()); // !!!

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
            .withName("My account")
            .withEmail("me@mydomain.com")
            .withMaxShortcuts(10)
            .build();

        // when
        Account actual = sut.createAccount(expected);

        // then
        assertThat(actual)
            .hasName("My account")
            .hasEmail("me@mydomain.com")
            .hasMaxShortcuts(10);
    }

    @Test
    void newly_created_account_does_not_have_shortcuts(){
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
    void cannot_create_account_with_duplicated_name() {
        // given
        String duplicateName = "duplicate name";
        Account firstAccount = fakeAccountBuilder()
            .withName(duplicateName)
            .build();
        sut.createAccount(firstAccount);
        Account accountWithSameName = fakeAccountBuilder()
            .withName(duplicateName)
            .build();

        // when
        Throwable throwable = catchThrowable(() -> sut.createAccount(accountWithSameName));

        // then
        Assertions.assertThat(throwable)
            .isInstanceOf(DuplicateAccountException.class)
            .hasMessage("Account with name: 'duplicate name' already exists");
    }
    @Test
    void cannot_create_account_with_non_positive_value_of_max_shortcuts() {
        // given
        Account account = fakeAccountBuilder()
            .withMaxShortcuts(0)
            .build();

        // when
        Throwable throwable = catchThrowable(() -> sut.createAccount(account));

        // then
        Assertions.assertThat(throwable)
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("Max shortcuts have to be a positive number");
    }

    @Test
    void account_name_should_not_be_null_nor_empty() {
        // given
        Account accountWithoutName = fakeAccountBuilder()
            .withName(null)
            .build();

        // when
        Throwable throwable = catchThrowable(() -> sut.createAccount(accountWithoutName));

        // then
        Assertions.assertThat(throwable)
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
    void created_account_has_properly_mapped_properties(){
        // given
        Account expected = fakeAccountBuilder()
            .build();

        // when
        sut.createAccount(expected);

        // then
        Account savedAccount = accountRepository
                .findById(expected.getName())
                .orElseThrow(() -> new RuntimeException("Missing account in repository"));
        assertThat(savedAccount)
            .isSameAs(expected);
    }

    @Test
    void get_account_which_was_removed_returns_empty_optional(){ // E2E scenario
        // given
        Account expected = fakeAccountBuilder()
            .build();
        accountRepository.save(expected);

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
    void delete_existing_account_removes_it_from_system(){
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
