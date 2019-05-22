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
	public Account findAccountWithShortcut(String accountName, String shortcut) {
		return mongoTemplate.findOne(
				new Query(where("shortcuts").elemMatch(where("shortcut").is(shortcut))), 
				Account.class);		
	}

	@Override
	public DeleteResult delete(String accountName){
		return mongoTemplate.remove( new Query(where("name").is(accountName)), Account.class);
	}

}
