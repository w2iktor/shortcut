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

public interface AccountsService {
    Account createAccount(@Valid Account account);

    Optional<Account> getAccount(String id);

    ImmutableList<Account> getAccounts();

    boolean removeAccount(String id);
}
