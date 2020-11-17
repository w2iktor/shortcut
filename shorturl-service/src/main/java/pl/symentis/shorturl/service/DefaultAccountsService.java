package pl.symentis.shorturl.service;

import com.google.common.collect.ImmutableList;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.symentis.shorturl.dao.AccountRepository;
import pl.symentis.shorturl.domain.Account;

import javax.validation.Valid;
import java.util.Optional;

import static java.text.MessageFormat.format;

@Service
@Validated
public class DefaultAccountsService implements AccountsService {

    private final AccountRepository accountRepository;

    @Autowired
    public DefaultAccountsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(@Valid Account account) {
        try {
            return accountRepository.insert(account);
        } catch (DuplicateKeyException e) {
            throw new DuplicateAccountException(format("Account with name: ''{0}'' already exists", account.getName()), e);
        }
    }

    @Override
    public Optional<Account> getAccount(String id) {
        return accountRepository.findById(id);
    }

    @Override
    public ImmutableList<Account> getAccounts() {
        return ImmutableList.copyOf(accountRepository.findAll());
    }

    @Override
    public boolean removeAccount(String id) {
        DeleteResult deleteResult = accountRepository.delete(id);
        return deleteResult.getDeletedCount() > 0;
    }
}
