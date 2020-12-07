package pl.symentis.shorturl.integration;

import pl.symentis.shorturl.dao.AccountRepository;
import pl.symentis.shorturl.domain.Account;
import pl.symentis.shorturl.domain.Shortcut;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

class TestAccountProvider {
    private AccountRepository accountRepository;
    private Account account;
    private List<Shortcut> shortcuts = new ArrayList<>();

    TestAccountProvider(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public static TestAccountProvider testAccountProvider(AccountRepository repository) {
        return new TestAccountProvider(repository);
    }

    public TestAccountProvider withAccount(Account account) {
        this.account = account;
        return this;
    }

    public TestAccountProvider withShortcuts(Shortcut... shortcut) {
        this.shortcuts.addAll(asList(shortcut));
        return this;
    }

    public Account save() {
        account.addShortcuts(shortcuts);
        return accountRepository.insert(account);
    }
}
