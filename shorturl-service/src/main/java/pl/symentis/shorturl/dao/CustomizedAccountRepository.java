package pl.symentis.shorturl.dao;

import pl.symentis.shorturl.domain.Account;

public interface CustomizedAccountRepository {
	
	Account findAccountWithShortcut(String accountName, String shortcut);

}
