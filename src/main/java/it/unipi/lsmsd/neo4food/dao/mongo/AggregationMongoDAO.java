package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.MongoException;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import it.unipi.lsmsd.neo4food.dto.AnalyticsDTO;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.service.ServiceProvider;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.swing.text.DefaultEditorKit;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
                    Arrays.asList(unwind, sort, addFields, match, group, project)).into(new ArrayList<>());

            ClientSession session = getSession();
            try{
                List toWrite = new ArrayList();

                for(Document d : result){

                    String rid = d.get("_id").toString();
                    Double range = ((Double) d.get("avg"));
                    String prange = "";

                    if (range < 5){
                        prange = "$";
                    }
                    else if(range < 12){
                        prange = "$$";
                    }
                    else if(range < 18){
                        prange = "$$$";
                    } else {
                        prange = "$$$$";
                    }

                    toWrite.add(new UpdateOneModel<>(
                            new Document("_id", new ObjectId(rid)),
                            new Document("$set", new Document("price_range", prange))
                    ));
                }

                session.startTransaction();
                collection.bulkWrite(session, toWrite);
                session.commitTransaction();

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

        try{
            ClientSession session = getSession();
            try {
                List toWrite = new ArrayList<>();
                for (Document d : list) {

                    String rid = d.get("rid").toString();
                    Double mean = (Double) d.get("val");

                    toWrite.add(new UpdateOneModel<>(
                            new Document("_id", new ObjectId(rid)),
                            new Document("$set", new Document("score", mean))
                    ));

                }

                session.startTransaction();
                collection.bulkWrite(session, toWrite);
                session.commitTransaction();

            } catch (Exception e) {
                session.abortTransaction();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }catch (MongoException e){
            System.err.println(e);
        }
    }

    public ListDTO<AnalyticsDTO> getOrdersPerZip(){
        ListDTO<AnalyticsDTO> toReturn = new ListDTO<>();

        MongoCollection<Document> collection = getDatabase().getCollection("Orders");
        //... {$group: {_id: "$zipcode", sum: {$count:{}}}}])
        Bson group = new Document("$group", new Document("_id", "$zipcode").append("sum", new Document("$sum", 1)));
        //... {$sort: {sum: -1}},
        Bson sort = new Document("$sort", new Document("sum", -1));
        //... {$project: {zip: "$_id", count: "$sum"}}
        Bson project = new Document("$project", new Document("zip", "$_id").append("count","$sum"));


        try{
            List<Document> result = collection.aggregate(
                Arrays.asList(group, sort, project)).into(new ArrayList<>());
            List<AnalyticsDTO> toSet = new ArrayList<>();

            for(Document d : result){
                AnalyticsDTO toAppend = new AnalyticsDTO();

                toAppend.setZipcode(d.get("zip").toString());
                toAppend.setCount(d.getInteger("count"));

                toSet.add(toAppend);
            }

            toReturn.setList(toSet);
            toReturn.setItemCount(toSet.size());

        }catch (MongoException e){
            System.err.println(e);
        }

        return toReturn;
    }

    public ListDTO<AnalyticsDTO> getLastMonthProfits(){
        ListDTO<AnalyticsDTO> toReturn = new ListDTO<>();
        MongoCollection<Document> collection = getDatabase().getCollection("Orders");
        //... {$match: {creationDate: {$gtr: new Date(new Date() - 1000*60*60*24*30)}}}
        Bson match = new Document("$match", new Document("creationDate",
                    new Document("$gte", LocalDateTime.now().minusMonths(1))));
        //... {$group: {_id: "$restaurantId"", name: {$first:"$restaurant"}, gain: {$sum: "$total"}}}]
        Bson group = new Document("$group", new Document("_id", "$restaurantId")
                                .append("name",new Document("$first","$restaurant"))
                                .append("gain", new Document("$sum", "$total")));
        //... {$sort: {gain: -1}},
        Bson sort = new Document("$sort", new Document("gain", -1));
        //... {$project: {rest: "$name", gain: "$gain"}}
        Bson project = new Document("$project", new Document("rest", "$name").append("gain","$gain"));
        //... {$limit: 10}
        Bson limit = new Document("$limit", 10);

        try{
            List<Document> result = collection.aggregate(
                    Arrays.asList(match, group,sort,project, limit)).into(new ArrayList<>());
            List<AnalyticsDTO> toSet = new ArrayList<>();

            for(Document d : result){
                AnalyticsDTO toAppend = new AnalyticsDTO();

                toAppend.setRestaurant(d.get("rest").toString());
                toAppend.setDouble(Double.parseDouble(d.get("gain").toString()));

                toSet.add(toAppend);
            }

            toReturn.setList(toSet);
            toReturn.setItemCount(toSet.size());

        }catch (MongoException e){
            System.err.println(e);
        }

        return toReturn;
    }

    public ListDTO<AnalyticsDTO> getCaviale(){
        ListDTO<AnalyticsDTO> toReturn = new ListDTO<>();

        MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");
        //... {$unwind: "$dishes"},
        Bson unwind = new Document("$unwind", "$dishes");
        //... {$addFields: {dishPrice: {$toDouble: "$dishes.price"}}},
        Bson addFields = new Document("$addFields", new Document("dishPrice", new Document("$toDouble", "$dishes.price")));
        //... {$sort: {dishPrice: -1}},
        Bson sort = new Document("$sort", new Document("dishPrice", -1));
        //... {$project: {dish: "$dishes.name", cost: "$dishPrice", res: "$name"}}
        Bson project = new Document("$project", new Document("dish", "$dishes.name").append("cost","$dishPrice").append("res", "$name"));
        //... {$limit: 10}
        Bson limit = new Document("$limit", 10);

        try{
            List<Document> result = collection.aggregate(
                    Arrays.asList(unwind, addFields, sort, project, limit)).into(new ArrayList<>());
            List<AnalyticsDTO> toSet = new ArrayList<>();

            for(Document d : result){
                AnalyticsDTO toAppend = new AnalyticsDTO();

                toAppend.setDish(d.get("dish").toString());
                toAppend.setDouble(d.getDouble("cost"));
                toAppend.setRestaurant(d.get("res").toString());

                toSet.add(toAppend);
            }

            toReturn.setList(toSet);
            toReturn.setItemCount(toSet.size());

        }catch (MongoException e){
            System.err.println(e);
        }

        return toReturn;
    }
}

