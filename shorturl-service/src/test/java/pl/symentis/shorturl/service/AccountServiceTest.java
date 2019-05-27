package pl.symentis.shorturl.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pl.symentis.shorturl.domain.AccountAssert.assertThat;


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

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.mongodb.MongoClient;

import pl.symentis.shorturl.dao.AccountRepository;
import pl.symentis.shorturl.domain.Account;
import pl.symentis.shorturl.domain.AccountBuilder;

import javax.validation.ValidationException;
import java.util.Optional;

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
  
  Fairy fairy = Fairy.create();
  
  @Autowired
  AccountsService sut;  

  @Autowired
  AccountRepository repo;
  
  @Test
  void create_account() {
    // given
    Person person = fairy.person();
    Account expected = new Account(
        person.getFirstName(),
        person.getEmail(),
        "taxnumber",
        1);

    // when
    Account actual = sut.createAccount(expected);

    // then
    assertThat(actual)
      .hasName(person.getFirstName())
      .hasEmail(person.getEmail())
      .hasTaxnumber("taxnumber")
      .hasMaxShortcuts(1);
    
    // when 
    Optional<Account> account= sut.getAccount(expected.getName());
    
    // then
    assertThat(actual)
    .hasName(person.getFirstName())
    .hasEmail(person.getEmail())
    .hasTaxnumber("taxnumber")
    .hasMaxShortcuts(1);
  }
  
  @Test
  void dont_create_account() {
    // given
    Account expected0 = new Account(
        "name1",
        "mail",
        "taxnumber",
        1);
    Account expected1 = new Account(
        "name1",
        "innemail",
        "innytaxnumber",
        1);

    // when & then
    sut.createAccount(expected0);

    assertThatThrownBy(() -> sut.createAccount(expected1))
      .isInstanceOf(DuplicateAccountException.class)
      .hasMessage("a chuj");
    
  }

  @Test
  void account_name_should_not_be_null_nor_empty(){
    // given
    Account accountWithoutName = AccountBuilder.accountBuilder()
        .withEmail("aaa@gmail.com")
        .withMaxShortcuts(1)
        .withTaxnumber("taxnumber")
        .withName(null)
        .build();

    // when & then
    assertThatThrownBy( () -> sut.createAccount(accountWithoutName))
        .isInstanceOf(ValidationException.class)
        .hasMessageContaining("Name in account cannot be empty");
  }

  @Test
  void get_account_for_non_existing_account_name_returns_empty_optional(){
    // when
    Optional<Account> result = sut.getAccount("NON_EXISTING_NAME");

    // then
    Assertions.assertThat(result)
            .isEmpty();
  }

  @Test
  void get_account_s(){
    // given
    Person person = fairy.person();
    Account expected = new Account(
        person.getFirstName(),
        person.getEmail(),
        "taxnumber",
        1);
    repo.save( expected );

    // when

    Optional<Account> account = sut.getAccount(expected.getName());
    
    assertThat(account.get())
    .hasName(person.getFirstName())
    .hasEmail(person.getEmail())
    .hasTaxnumber("taxnumber")
    .hasMaxShortcuts(1);

  }
}
