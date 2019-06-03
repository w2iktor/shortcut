package pl.symentis.shorturl.dao;

import com.mongodb.client.result.DeleteResult;

public interface CustomizedAccountRepository {
	
	boolean checkIfShortcutExists(String shortcut);

	DeleteResult delete(String accountName);
}
