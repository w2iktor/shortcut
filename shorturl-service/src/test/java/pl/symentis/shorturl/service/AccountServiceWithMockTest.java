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

  @BeforeEach
  void setUp() {
  }
  
  @Test
  void account_returned_after_creation_has_properly_mapped_properties() {
  }
  
  @Test
  void try_of_creating_account_with_existing_name_throws_duplicated_account_exception() {
    // use Answer mechanism to emulate real repository behaviour
  }
}
