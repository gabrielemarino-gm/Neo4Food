package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Field;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

public class AggregationMongoDAO extends BaseMongo{

    public boolean setAvgPrices(){
//        Aggregation avg price per restaurant
        MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");
        Bson unwind = unwind("$dishes");
        Bson sort = sort(descending("_id"));
//        Bson addFields = addFields(new Field("dishPrice", TODOUBLE("$dishes.price")));
        Bson match = match(gt("dishPrice", 0));
        Bson group = group("$_id", avg("avgPrice", "$dishPrice"));
        Bson project = project(fields(include("_id", "avgPrice")));

        try{
            List<Document> result = collection.aggregate(
                                        Arrays.asList(
                                                unwind,
                                                sort,
//                                                addFields,
                                                match,
                                                group,
                                                project)
                                    ).into(new ArrayList<>());

            for (Document d : result){
//                Modifica price_range (transazione?)
                System.out.println(d);
                break;
            }
        }catch (MongoException e){
            System.err.println(e);
        }
        return false;
    }
}

