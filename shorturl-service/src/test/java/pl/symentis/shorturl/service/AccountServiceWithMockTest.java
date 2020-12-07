package pl.symentis.shorturl.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import pl.symentis.shorturl.dao.AccountRepository;
import pl.symentis.shorturl.domain.Account;
import pl.symentis.shorturl.domain.AccountBuilder;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static pl.symentis.shorturl.domain.AccountAssert.assertThat;
import static pl.symentis.shorturl.domain.FakeAccountBuilder.fakeAccountBuilder;

public class AccountServiceWithMockTest {

    AccountsService sut;
    private AccountRepository repo;

    @BeforeEach
    void setUp() {
        repo = Mockito.mock(AccountRepository.class);
        sut = new DefaultAccountsService(repo);
    }

    @Test
    void create_account_action_returns_properly_mapped_entity() {
        // using mock emulate inserting entity into DB
    }

    @Test
    void create_account_with_duplicated_name_throws_duplicate_account_exception() {
        // use Answers mechanism to emulate exceptions during inserting duplicated entity

    }
}
