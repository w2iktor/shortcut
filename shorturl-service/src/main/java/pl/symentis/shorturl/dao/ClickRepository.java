package pl.symentis.shorturl.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import pl.symentis.shorturl.domain.Click;

public interface ClickRepository extends MongoRepository<Click, ObjectId>{

}
