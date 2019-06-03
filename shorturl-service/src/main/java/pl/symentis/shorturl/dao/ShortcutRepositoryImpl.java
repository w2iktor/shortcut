package pl.symentis.shorturl.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.text.MessageFormat;
import java.util.Optional;

import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import pl.symentis.shorturl.domain.Account;
import pl.symentis.shorturl.domain.Shortcut;
import pl.symentis.shorturl.service.AccountDoesntExistException;

@Component
public class ShortcutRepositoryImpl implements CustomizedShortcutRepository {

	private final MongoTemplate mongoTemplate;

	@Autowired
	public ShortcutRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public long addShortcut(String accountName, String shortcut, Shortcut value) {
		UpdateResult updateResult = mongoTemplate.updateFirst(new Query(where("name").is(accountName)), new Update().push("shortcuts", value),
				Account.class);
		long modifiedCount = updateResult.getModifiedCount();
		if( modifiedCount == 0 ) {
			throw new AccountDoesntExistException(MessageFormat.format("Account with name: ''{0}'' doesn't exists", accountName));
		}
		return modifiedCount;
	}

	@Override
	public Optional<Shortcut> findByShortcut(String shortcut) {
		
		BasicQuery query = new BasicQuery(
				where("shortcuts.shortcut").is(shortcut).getCriteriaObject(), 
				where("shortcuts").elemMatch(where("shortcut").is(shortcut)).getCriteriaObject());
		
		Update update = new Update().inc("shortcuts.$.counter", 1);
		
		return Optional.ofNullable(
				mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Account.class ))
				.map(Account::getShortcuts)
				.map(s -> s.get(0));
	}
}
