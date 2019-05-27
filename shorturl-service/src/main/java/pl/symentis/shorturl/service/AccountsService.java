package pl.symentis.shorturl.service;


import java.util.Optional;
import java.util.stream.DoubleStream;

import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

import org.springframework.validation.annotation.Validated;
import pl.symentis.shorturl.dao.AccountRepository;
import pl.symentis.shorturl.domain.Account;
import pl.symentis.shorturl.domain.AccountBuilder;

@Component
@Validated
public class AccountsService {

	private final AccountRepository accountRepository;

	@Autowired
	public AccountsService(AccountRepository accountRepository) {
		super();
		this.accountRepository = accountRepository;
	}

	public Account createAccount(Account account) {
	  try {
      return accountRepository.insert(account);
    } catch (DuplicateKeyException e) {
      throw new DuplicateAccountException("a chuj", e);
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
