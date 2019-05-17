package pl.symentis.shorturl.service;

import static java.util.Collections.emptyList;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

import pl.symentis.shorturl.api.CreateAccountRequest;
import pl.symentis.shorturl.dao.AccountRepository;
import pl.symentis.shorturl.domain.Account;

@Component
public class AccountsService {
	
	private final AccountRepository accountRepository;
	
	@Autowired
	public AccountsService(AccountRepository accountRepository) {
		super();
		this.accountRepository = accountRepository;
	}

	public Optional<Account> createAccount(CreateAccountRequest createAccount) {
		Account account = new Account(
				createAccount.getName(),
				createAccount.getEmail(),
				createAccount.getTaxnumber(),
				createAccount.getMaxShortcuts(), 
				emptyList());
		return Optional.ofNullable(accountRepository.save(account));
	}

	public Optional<Account> getAccount(String id) {
		return accountRepository.findById(id);
	}

	public ImmutableList<Account> getAccounts() {
		return ImmutableList.copyOf(accountRepository.findAll());
	}

}
