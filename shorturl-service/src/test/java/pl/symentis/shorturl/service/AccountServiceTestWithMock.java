package pl.symentis.shorturl.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import pl.symentis.shorturl.dao.AccountRepository;
import pl.symentis.shorturl.domain.Account;
import pl.symentis.shorturl.domain.ExpiryPolicy;
import pl.symentis.shorturl.domain.FakeExpiryPolicyBuilder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static pl.symentis.shorturl.domain.AccountAssert.assertThat;
import static pl.symentis.shorturl.domain.FakeExpiryPolicyBuilder.fakeExpiryPolicyBuilder;

public class AccountServiceTestWithMock {
  
  @Autowired
  AccountsService sut;
  private AccountRepository repo;
  private ExpiryPolicy defaultExpiryPolicy;

  @BeforeEach
  void setUp() {
    repo = Mockito.mock(AccountRepository.class);
    sut = new AccountsService(repo);
    defaultExpiryPolicy = fakeExpiryPolicyBuilder().withRandomExipiryPolicy().build();
  }
  
  @Test
  void create_account() {
    // given
    Account expected = new Account(
        "name",
        "mail",
        "taxnumber",
        1, defaultExpiryPolicy);
    when(repo.insert(expected)).thenReturn(expected);
    // when
    Account actual = sut.createAccount(expected);
    assertThat(actual)
      .hasName("name")
      .hasEmail("mail")
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
        1, defaultExpiryPolicy);
    Account expected1 = new Account(
        "name1",
        "innemail",
        "innytaxnumber",
        1, defaultExpiryPolicy);
    when(repo.insert(expected0)).thenReturn(expected0);
    when(repo.insert(expected1)).thenThrow(new DuplicateAccountException("a chuj", new Throwable()));
    // when
    sut.createAccount(expected0);

    assertThatThrownBy(() -> sut.createAccount(expected1))
      .isInstanceOf(DuplicateAccountException.class)
      .hasMessage("a chuj");
    
  }
}
