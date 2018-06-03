package pl.symentis.shorturl.dao;

import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Repository;
import pl.symentis.shorturl.domain.Click;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class ClickRepositoryImpl implements CustomizedClickRepository{
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ClickRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public int countClicks(String shortcut) {
        AggregationResults<StatsCounter> results = mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        match(where("shortcut").is(shortcut)),
                        group("shortcut")
                                .count().as("total")
                ),
                Click.class,
                StatsCounter.class
        );
        return results.getUniqueMappedResult().total;
    }

    @Override
    public StatsSummary getAgentDetails(String shortcut){
        AggregationResults<StatsSummary> results = mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        match(where("shortcut").is(shortcut)),
                        group("agent")
                                .count().as("total")
                                .first("shortcut")
                                .as("shortcut"),
                        group("shortcut")
                            .push(new BasicDBObject("_id", "$_id")
                                    .append("total", "$total"))
                                    .as("statsCounters"),
                        project("statsCounters")
                            .andExclude("_id")
                ),
                Click.class,
                StatsSummary.class
        );
        return results.getUniqueMappedResult();
    }

}
