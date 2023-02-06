package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.MongoException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Updates;
import it.unipi.lsmsd.neo4food.service.ServiceProvider;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.Converter;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class AggregationMongoDAO extends BaseMongo{

    public void setAvgPrices(){
//        Aggregation avg price per restaurant
        MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");
        //... {$unwind: "$dishes"},
        Bson unwind = new Document("$unwind", "$dishes");
        //... {$sort: {_id: 1}},
        Bson sort = new Document("$sort", new Document("_id", 1));
        //... {$addFields: {dishPrice: {$toDouble: "$dishes.price"}}},
        Bson addFields = new Document("$addFields", new Document("dishPrice", new Document("$toDouble", "$dishes.price")));
        //... {$match: {dishPrice: {$gt: 0}}}
        Bson match = new Document("$match", new Document("dishPrice", new Document("$gt",0)));
        //... {$group: {_id: "$_id", avg: {$avg:"$dishPrice"}}}])
        Bson group = new Document("$group", new Document("_id", "$_id").append("avg", new Document("$avg", "$dishPrice")));
        //... {$project: {rid: "$_id", avg: "$avg"}}
        Bson project = new Document("$project", new Document("_id", "$_id").append("avg","$avg"));


        try{
            List<Document> result = collection.aggregate(
                                        Arrays.asList(unwind, sort, addFields, match, group, project
                                        )
                                    ).into(new ArrayList<>());
            int count = 0;
            ClientSession session = getSession();
            try{

                for(Document d : result){

                    String rid = d.get("_id").toString();
                    Double range = ((Double) d.get("avg"));
                    String prange;

                    if (range < 5){
                        prange = "$";
                    }
                    else if(range < 12){
                        prange = "$$";
                    }
                    else if(range < 18){
                        prange = "$$$";
                    }else {
                        prange = "$$$$";
                    }

                    collection.updateOne(session,
                            eq("_id", new ObjectId(rid)),
                            Updates.set("price_range", prange));
                    count++;
                    if(count%1000 == 0){
                        System.out.println(count + " processed.");
                    }
                }
                session.commitTransaction();
                System.out.println("Processing of prices terminated");
            }catch (Exception e){
                session.abortTransaction();
                e.printStackTrace();
            }finally {
                session.close();
            }

        }catch (MongoException e){
            System.err.println(e);
        }
    }

    public void setAvgRate(){
        List<Document> list = ServiceProvider.getSupportService().getAvgRating();
        MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");
        ClientSession session = getSession();
        try{
            int count = 0;
            for(Document d : list) {

                String rid = d.get("rid").toString();
                Double mean = (Double) d.get("val");

                collection.updateOne(session,
                        eq("_id", new ObjectId(rid)),
                        Updates.set("score", mean));

                count++;

                if (count % 1000 == 0) {
                    System.out.println(count + " processed.");

                }
            }

            session.commitTransaction();
            System.out.println("Processing of scores terminated");
        }catch (Exception e){
            session.abortTransaction();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }
}

