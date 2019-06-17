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
            .build();
    }

    @Test
    void get_of_newly_created_account_does_not_have_shortcuts(){
        Account result = null;

        // then
        assertThat(result)
                .hasNoShortcuts();
    }

    @Test
    void cannot_create_duplicated_account() {
        Throwable throwable = null;

        // when
        Assertions.assertThat(throwable)
            .isInstanceOf(DuplicateAccountException.class)
            .hasMessage("Account with name: 'duplicate name' already exists");
    }
    @Test
    void cannot_create_account_with_non_positive_value_of_max_shortcuts() {
    }

    @Test
    void account_name_should_not_be_null_nor_empty() {
    }

    @Test
    void get_account_for_non_existing_account_name_returns_empty_optional() {
    }

    @Test
    void get_account_for_existing_account_name_returns_object_with_all_properties_properly_mapped() {

        // then
        assertThat(null)
            .isSameAs(null);
    }

    @Test
    void get_account_which_was_removed_returns_empty_optional(){

        // then
        Optional<Account> result = null;
        Assertions.assertThat(result)
            .isEmpty();
    }

    @Test
    void delete_non_existing_account_returns_false(){
    }

    @Test
    void delete_existing_account_removes_it_from_system(){
        // given
        Account expected = fakeAccountBuilder()
            .build();
        accountRepository.save(expected);
    }
}
