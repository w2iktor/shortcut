package pl.symentis.shorturl.dao;

import com.mongodb.client.result.DeleteResult;
import pl.symentis.shorturl.domain.Account;

public interface CustomizedAccountRepository {
	
	Account findAccountWithShortcut(String accountName, String shortcut);

	DeleteResult delete(String accountName);
}
