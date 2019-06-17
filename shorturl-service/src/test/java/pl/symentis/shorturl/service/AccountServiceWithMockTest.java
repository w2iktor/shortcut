package pl.symentis.shorturl.service;

import org.assertj.core.api.Assertions;
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
    void create_account_returns_properly_mapped_account() {
        // given

        Account actual = null;

        // then
        assertThat(actual)
                .hasName("name")
                .hasEmail("mail")
                .hasTaxnumber("taxnumber")
                .hasMaxShortcuts(1);
    }

    @Test
    void cannot_create_duplicated_account() {
        // then
        Exception throwable = null;
        Assertions.assertThat(throwable)
                .isInstanceOf(DuplicateAccountException.class)
                .hasMessage("BlaBlaBla");

    }
}
