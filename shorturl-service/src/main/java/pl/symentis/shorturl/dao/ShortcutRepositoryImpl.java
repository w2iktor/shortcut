package pl.symentis.shorturl.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import pl.symentis.shorturl.domain.Account;
import pl.symentis.shorturl.domain.Shortcut;

@Component
public class ShortcutRepositoryImpl implements CustomizedShortcutRepository{
	
	private final MongoTemplate mongoTemplate;
	
	@Autowired
	public ShortcutRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void addShortcut(String accountName, String shortcut, Shortcut value) {
		mongoTemplate.updateFirst(
				new Query(where("name").is(accountName)), 
				new Update().set("shortcuts."+shortcut, value), 
				Account.class);
	}

}
