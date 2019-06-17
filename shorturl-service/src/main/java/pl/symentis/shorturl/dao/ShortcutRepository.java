package pl.symentis.shorturl.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import pl.symentis.shorturl.domain.Shortcut;

@Component
public interface ShortcutRepository extends MongoRepository<Shortcut, String>, CustomizedShortcutRepository{

}
