package pl.symentis.shorturl.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import pl.symentis.shorturl.domain.Account;

@Component
public class AccountRepositoryImpl implements CustomizedAccountRepository {

	private final MongoTemplate mongoTemplate;
	
	@Autowired
	public AccountRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public boolean checkIfShortcutExists(String shortcut) {
		return mongoTemplate.count(
				new Query(where("shortcuts").elemMatch(where("shortcut").is(shortcut))), 
				Account.class) > 0;
	}

	@Override
	public DeleteResult delete(String accountName){
		return mongoTemplate.remove( new Query(where("name").is(accountName)), Account.class);
	}

}
