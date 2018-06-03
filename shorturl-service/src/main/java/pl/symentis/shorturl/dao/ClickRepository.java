package pl.symentis.shorturl.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Component;
import pl.symentis.shorturl.domain.Click;

@Component
public interface ClickRepository extends MongoRepository<Click, ObjectId>, CustomizedClickRepository{

}
