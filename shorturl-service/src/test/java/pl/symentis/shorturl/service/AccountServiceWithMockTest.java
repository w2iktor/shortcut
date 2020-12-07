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
        // given
        Account expected = AccountBuilder.accountBuilder()
            .withName("account's name")
            .withEmail("email@domain.com")
            .withMaxShortcuts(1)
            .withTaxnumber("taxnumber")
            .build();
        when(repo.insert(expected)).thenReturn(expected);
        // when
        Account actual = sut.createAccount(expected);
        assertThat(actual)
            .hasName("account's name")
            .hasEmail("email@domain.com")
            .hasTaxnumber("taxnumber")
            .hasMaxShortcuts(1);
    }

    @Test
    void create_account_with_duplicated_name_throws_duplicate_account_exception() {
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
        if (!savedAccounts.add(argument.getName())) {
            throw new DuplicateAccountException("BlaBlaBla", new Throwable());
        }
        return argument;
    }
}
