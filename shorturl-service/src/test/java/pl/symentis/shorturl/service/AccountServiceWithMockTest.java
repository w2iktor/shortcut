package pl.symentis.shorturl.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import pl.symentis.shorturl.dao.AccountRepository;
import pl.symentis.shorturl.domain.Account;
import pl.symentis.shorturl.domain.ExpiryPolicy;
import pl.symentis.shorturl.domain.FakeAccountBuilder;
import pl.symentis.shorturl.domain.FakeExpiryPolicyBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static pl.symentis.shorturl.domain.AccountAssert.assertThat;
import static pl.symentis.shorturl.domain.FakeAccountBuilder.fakeAccountBuilder;
import static pl.symentis.shorturl.domain.FakeExpiryPolicyBuilder.fakeExpiryPolicyBuilder;

public class AccountServiceWithMockTest {
  
  AccountsService sut;
  private AccountRepository repo;
  private ExpiryPolicy defaultExpiryPolicy;

  @BeforeEach
  void setUp() {
    repo = Mockito.mock(AccountRepository.class);
    sut = new DefaultAccountsService(repo);
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
    Account duplicatedAccount = fakeAccountBuilder()
            .withName("duplicated account")
            .build();
    given(repo.insert(Mockito.any(Account.class)))
            .willAnswer(new AccountAnswer());

    // when
    sut.createAccount(duplicatedAccount);
    // and
    Throwable throwable = catchThrowable(() -> sut.createAccount(duplicatedAccount));

    // then
    Assertions.assertThat(throwable)
      .isInstanceOf(DuplicateAccountException.class)
      .hasMessage("BlaBlaBla");
    
  }
}

class AccountAnswer implements Answer<Account> {
  private Set<String> savedAccounts = new HashSet<>();

  @Override
  public Account answer(InvocationOnMock invocationOnMock) throws Throwable {
    Account argument = invocationOnMock.getArgument(0);
    if(!savedAccounts
            .add(argument.getName())){
      throw new DuplicateAccountException("BlaBlaBla", new Throwable());
    }
    return argument;
  }
}
