package pl.symentis.shorturl.service;


import com.google.common.collect.ImmutableList;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import pl.symentis.shorturl.dao.AccountRepository;
import pl.symentis.shorturl.domain.Account;

import javax.validation.Valid;
import java.util.Optional;

import static java.text.MessageFormat.format;

@Component
@Validated
public class AccountsService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountsService(AccountRepository accountRepository) {
        super();
        this.accountRepository = accountRepository;
    }

    public Account createAccount(@Valid Account account) {
        try {
            return accountRepository.insert(account);
        } catch (DuplicateKeyException e) {
            throw new DuplicateAccountException(format("Account with name: ''{0}'' already exists", account.getName()), e);
        }
    }

    public Optional<Account> getAccount(String id) {
        return accountRepository.findById(id);
    }

    public ImmutableList<Account> getAccounts() {
        return ImmutableList.copyOf(accountRepository.findAll());
    }

    public boolean removeAccount(String id) {
        DeleteResult deleteResult = accountRepository.delete(id);
        return deleteResult.getDeletedCount() > 0;
    }
}
