package pl.symentis.shorturl.domain;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import pl.symentis.shorturl.api.CreateAccountRequest;

@Component
public class AccountsService {
	
	private final ConcurrentHashMap<String,Account> accounts = new ConcurrentHashMap<>();
	
	public Optional<Account> createAccount(CreateAccountRequest createAccount) {
		Account account = new Account(createAccount.getName(),createAccount.getEmail(),createAccount.getTaxnumber());
		if(accounts.putIfAbsent(createAccount.getName(), account)==null) {
			return Optional.of(account);
		}
		return Optional.empty();
	}

	public Optional<Account> getAccount(String id) {
		return Optional.ofNullable(accounts.get(id));
	}

}
