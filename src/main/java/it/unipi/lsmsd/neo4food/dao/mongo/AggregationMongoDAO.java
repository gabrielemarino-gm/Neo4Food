package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.MongoException;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import it.unipi.lsmsd.neo4food.dto.AnalyticsDTO;
import it.unipi.lsmsd.neo4food.dto.DishDTO;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.service.ServiceProvider;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AggregationMongoDAO extends BaseMongo
{
    public void setAvgPrices()
    {
//        Aggregation avg price per restaurant

        MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");
        //... {$unwind: "$dishes"},
        Bson unwind = new Document("$unwind", "$dishes");
        //... {$addFields: {dishPrice: {$toDouble: "$dishes.price"}}},
        Bson addFields = new Document("$addFields", new Document("dishPrice", new Document("$toDouble", "$dishes.price")));
        //... {$match: {dishPrice: {$gt: 0}}}
        Bson match = new Document("$match", new Document("dishPrice", new Document("$gt",0)));
        //... {$group: {_id: "$_id", avg: {$avg:"$dishPrice"}}}])
        Bson group = new Document("$group", new Document("_id", "$_id").append("avg", new Document("$avg", "$dishPrice")));

        try
        {
            List<Document> result = collection.aggregate(
                    Arrays.asList(unwind, addFields, match, group)).into(new ArrayList<>());

            ClientSession session = getSession();
            try
            {
                List toWrite = new ArrayList();

                for(Document d : result)
                {
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

            }
            catch (Exception e)
            {
                session.abortTransaction();
                e.printStackTrace();
            }


        }
        catch (MongoException e)
        {
            System.err.println(e.getMessage());
        }

    }

    public void setAvgRate()
    {
        List<Document> list = ServiceProvider.getSupportService().getAvgRating();

        MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");

        try
        {
            ClientSession session = getSession();
            try
            {
                List toWrite = new ArrayList<>();
                for (Document d : list)
                {
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

            }
            catch (Exception e)
            {
                session.abortTransaction();
                e.printStackTrace();
            }

        }
        catch (MongoException e)
        {
            System.err.println(e.getMessage());
        }

    }

    public List<DishDTO> getUsual(String username, String rid)
    {
        List<DishDTO> toReturn = new ArrayList<>();

        MongoCollection<Document> collection = getDatabase().getCollection("Orders");
        //... { $match: {user: "PatataAliena", restaurantId: "<RID>"} }
        Bson match = new Document("$match", new Document("user", username).append("restaurantId", rid));
        //... { $group: { _id: { user: "$user", restaurant: "$restaurantId", dishes: "$dishes"}, count: {$sum: 1}} }
        Bson group = new Document("$group", new Document("_id",
                new Document("user", "$user").append("restaurant", "$restaurantId").append("dishes", "$dishes")
        ).append("count", new Document("$sum", 1)));
        //... { $sort: { count: -1 }}
        Bson sort = new Document("$sort", new Document("count", -1));
        //... { $limit: 1 }
        Bson limit = new Document("$limit", 1);

        try
        {
            List<Document> result = collection.aggregate(
                    Arrays.asList(match, group, sort, limit)).into(new ArrayList<>());
            if(result.size() == 0){return toReturn;}

            for(Document a : result)
            {
                Document id = (Document) a.get("_id");

                for(Document d : (List<Document>)id.get("dishes"))
                {
                    DishDTO toAppend = new DishDTO();

                    toAppend.setName(d.getString("name"));
                    toAppend.setPrice(d.getDouble("price"));
                    toAppend.setCurrency(d.getString("currency"));
                    toAppend.setQuantity(d.getInteger("quantity"));

                    toReturn.add(toAppend);
                }
            }
        }
        catch (MongoException e)
        {
            System.err.println(e.getMessage());
        }


        return toReturn;
    }

    public ListDTO<AnalyticsDTO> getOrdersPerZip()
    {
        ListDTO<AnalyticsDTO> toReturn = new ListDTO<>();

        MongoCollection<Document> collection = getDatabase().getCollection("Orders");
        //... {$group: {_id: "$zipcode", sum: {$count:{}}}}
        Bson group = new Document("$group", new Document("_id", "$zipcode").append("sum", new Document("$sum", 1)));
        //... {$sort: {sum: -1}},
        Bson sort = new Document("$sort", new Document("sum", -1));
        //... {$limit: 10}
        Bson limit = new Document("$limit", 10);

        try
        {
            List<Document> result = collection.aggregate(
                Arrays.asList(group, sort, limit)).into(new ArrayList<>());
            List<AnalyticsDTO> toSet = new ArrayList<>();

            for(Document d : result)
            {
                AnalyticsDTO toAppend = new AnalyticsDTO();

                toAppend.setZipcode(d.get("_id").toString());
                toAppend.setCount(d.getInteger("sum"));

                toSet.add(toAppend);
            }

            toReturn.setList(toSet);
            toReturn.setItemCount(toSet.size());

        }
        catch (MongoException e)
        {
            System.err.println(e.getMessage());
        }


        return toReturn;
    }

    public ListDTO<AnalyticsDTO> getLastMonthProfits()
    {
        ListDTO<AnalyticsDTO> toReturn = new ListDTO<>();
        MongoCollection<Document> collection = getDatabase().getCollection("Orders");
        //... {$match: {creationDate: {$gtr: new Date(new Date() - 1000*60*60*24*30)}}}
        Bson match = new Document("$match", new Document("creationDate",
                    new Document("$gte", LocalDateTime.now().minusMonths(1))));
        //... {$group: {_id: "$restaurantId"", name: {$first:"$restaurant"}, gain: {$sum: "$total"}, currency: {$first:"$currency"}}}]
        Bson group = new Document
        (
            "$group", new Document("_id", "$restaurantId")
            .append("rest",new Document("$first","$restaurant"))
            .append("gain", new Document("$sum", "$total"))
            .append("currency", new Document("$first", "$currency"))
        );
        //... {$sort: {gain: -1}},
        Bson sort = new Document("$sort", new Document("gain", -1));
        //... {$limit: 10}
        Bson limit = new Document("$limit", 10);

        try
        {
            List<Document> result = collection.aggregate(
                    Arrays.asList(match, group, sort, limit)).into(new ArrayList<>());
            List<AnalyticsDTO> toSet = new ArrayList<>();

            for(Document d : result)
            {
                AnalyticsDTO toAppend = new AnalyticsDTO();

                toAppend.setRestaurant(d.get("rest").toString());
                toAppend.setDouble(Double.parseDouble(d.get("gain").toString()));
                toAppend.setCurrency(d.get("currency").toString());

                toSet.add(toAppend);
            }

            toReturn.setList(toSet);
            toReturn.setItemCount(toSet.size());
        }
        catch (MongoException e)
        {
            System.err.println(e.getMessage());
        }


        return toReturn;
    }

    public ListDTO<AnalyticsDTO> getCaviale()
    {
        ListDTO<AnalyticsDTO> toReturn = new ListDTO<>();

        MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");
        //... {$unwind: "$dishes"},
        Bson unwind = new Document("$unwind", "$dishes");
        //... {$addFields: {dishPrice: {$toDouble: "$dishes.price"}, currency: {$toString: "$dishes.currency"}}},
        Bson addFields = new Document("$addFields", new Document("dishPrice", new Document("$toDouble", "$dishes.price"))
                        .append("currency", new Document("$toString", "$dishes.currency")));
        //... {$sort: {dishPrice: -1}},
        Bson sort = new Document("$sort", new Document("dishPrice", -1));
        //... {$project: {dish: "$dishes.name", cost: "$dishPrice", res: "$name", "currency": "$dishes.currency"}}
        Bson project = new Document("$project", new Document("dish", "$dishes.name").append("cost","$dishPrice").append("res", "$name").append("currency", "$dishes.currency"));
        //... {$limit: 10}
        Bson limit = new Document("$limit", 10);

        try
        {
            List<Document> result = collection.aggregate(
                    Arrays.asList(unwind, addFields, sort, project, limit)).into(new ArrayList<>());
            List<AnalyticsDTO> toSet = new ArrayList<>();

            for(Document d : result)
            {
                AnalyticsDTO toAppend = new AnalyticsDTO();

                toAppend.setDish(d.get("dish").toString());
                toAppend.setDouble(d.getDouble("cost"));
                toAppend.setRestaurant(d.get("res").toString());
                toAppend.setCurrency(d.get("currency").toString());

                toSet.add(toAppend);
            }

            toReturn.setList(toSet);
            toReturn.setItemCount(toSet.size());

        }
        catch (MongoException e)
        {
            System.err.println(e.getMessage());
        }

        return toReturn;
    }

    public ListDTO<AnalyticsDTO> getBestHours(String rid)
    {
        ListDTO<AnalyticsDTO> toReturn = new ListDTO<>();
        MongoCollection<Document> collection = getDatabase().getCollection("Orders");

// (    Prima Analitycs: ORARIO PIU' INCASINATO, CONSIDERANDO L'ULTIMO MESE DI ORDINI

//...   {$match: {restaurantId: "63d92b3cc416ac8e49aec90f",
//                      creationDate: {$gte: new Date(new Date() - 1000*60*60*24*30)}}}
        Bson match = new Document(
                "$match",
                new Document("restaurantId", rid)
                        .append("creationDate", new Document("$gte", LocalDateTime.now().minusMonths(1)))
            );

//...   $project{orderHour:{$hour: "$creationDate"}}
        Bson project = new Document(
                "$project",
                new Document("orderHour", new Document("$hour", "$creationDate"))
        );

//...   $group:{_id:{ _id: "$orderHour",count: {$sum: 1}}}
        Bson group = new Document(
                "$group",
                new Document("_id", "$orderHour")
                        .append("count", new Document("$sum", 1))
        );

//...   { $sort: { count: -1 }}
        Bson sort = new Document("$sort", new Document("count", -1));

//...   { $limit: 4 }
        Bson limit = new Document("$limit", 4);

        try
        {
            List<Document> result = collection.aggregate(
                    Arrays.asList(match, project, group, sort, limit)).into(new ArrayList<>());
            List<AnalyticsDTO> toSet = new ArrayList<>();

            for(Document d : result)
            {
                AnalyticsDTO toAppend = new AnalyticsDTO();

                toAppend.setOrario(d.get("_id").toString());
                toAppend.setCount(d.getInteger("count"));

                toSet.add(toAppend);
            }

            toReturn.setList(toSet);
            toReturn.setItemCount(toSet.size());

        }
        catch (MongoException e)
        {
            System.err.println(e.getMessage());
        }


        return toReturn;
    }


    // Vedo qual è stato il piatto più venduto del mese
    public ListDTO<AnalyticsDTO> getBestDishDay(String rid)
    {
        ListDTO<AnalyticsDTO> toReturn = new ListDTO<>();
        MongoCollection<Document> collection = getDatabase().getCollection("Orders");

        AnalyticsDTO analytic = new AnalyticsDTO();

//...   {$match: {restaurantId: "63d92b3cc416ac8e49aec90e", creationDate: {$gte: new Date(new Date().setHours(0,0,0,0)), $lt: new Date(new Date().setHours(24,0,0,0))}}}
        Bson match = new Document(
                "$match",
                new Document("restaurantId", rid).append("creationDate", new Document("$gte", LocalDateTime.now().minusDays(1)))
        );

//...   {$unwind: "$dishes"}
        Bson unwind = new Document("$unwind","$dishes");

//...   $group:{_id: "$dishes.name", total: { $sum: "$dishes.quantity" }}
        Bson group = new Document(
                "$group",
                new Document("_id", "$dishes.name")
                        .append("total", new Document("$sum", "$dishes.quantity"))
        );

//...   { $sort: { count: -1 }}
        Bson sort = new Document("$sort", new Document("total", -1));
//...   { $limit: 1 }
        Bson limit = new Document("$limit", 1);


        try
        {
            List<Document> result = collection.aggregate(
                    Arrays.asList(match, unwind, group, sort, limit)).into(new ArrayList<>());
            List<AnalyticsDTO> toSet = new ArrayList<>();

            if(result.size() == 0)
                return toReturn;

            for(Document d : result)
            {
                AnalyticsDTO toAppend = new AnalyticsDTO();

                toAppend.setDish(d.get("_id").toString());
                toAppend.setCount(d.getInteger("total"));

                toSet.add(toAppend);
            }

            toReturn.setList(toSet);
            toReturn.setItemCount(toSet.size());

        }
        catch (MongoException e)
        {
            System.err.println(e.getMessage());
        }


        return toReturn;
    }

    public AnalyticsDTO getDailyRevenue(String rid)
    {
        AnalyticsDTO toReturn = new AnalyticsDTO();
        MongoCollection<Document> collection = getDatabase().getCollection("Orders");

// (    Sec Analityc: FATTURATO GIORNALIERO CON LA PIATTAFORMA
//...   {$match: {restaurantId: "63d92b3cc416ac8e49aec90e", creationDate: {$gte: new Date(new Date().setHours(0,0,0,0)), $lt: new Date(new Date().setHours(24,0,0,0))}}}
        Bson match = new Document(
                "$match",
                new Document("restaurantId", rid)
                        .append("creationDate",
                                new Document("$gte", LocalDateTime.now().minusDays(1))
                                .append("$lte", LocalDateTime.now().plusDays(1))
                        )
        );

//...   $group:{_id: "$restaurantId", total: { $sum: "$total" }}
        Bson group = new Document(
                "$group",
                new Document("_id", "$restaurantId")
                        .append("total", new Document("$sum", "$total"))
                        .append("currency", new Document("$first", "$currency"))
        );

//...   { $sort: { count: -1 }}
        Bson sort = new Document("$sort", new Document("count", -1));

        try
        {
            List<Document> result = collection.aggregate(
                    Arrays.asList(match, group)).into(new ArrayList<>());
            AnalyticsDTO toSet = new AnalyticsDTO();


            for(Document d : result)
            {
                toSet.setRestaurant(d.get("_id").toString());
                toSet.setCurrency(d.getString("currency"));
                toSet.setDouble(Double.parseDouble(d.get("total").toString()));
            }

            toReturn = toSet;
        }
        catch (MongoException e)
        {
            System.err.println(e.getMessage());
        }

        return toReturn;
    }

    public ListDTO<AnalyticsDTO> getModaOrders(String rid)
    {
        ListDTO<AnalyticsDTO> toReturn = new ListDTO<>();
        MongoCollection<Document> collection = getDatabase().getCollection("Orders");

        AnalyticsDTO analytic = new AnalyticsDTO();
// (    Ter Analitycs: MODA PIATTI

//...   {$match: {restaurantId: "63d92b3cc416ac8e49aec90e"}}
        Bson match = new Document(
                "$match",
                new Document("restaurantId", rid)
        );

//...   {$unwind: "$dishes"}
        Bson unwind = new Document("$unwind","$dishes");

//...   $group:{_id: "$dishes.name", total: { $sum: "$dishes.quantity" }}
        Bson group = new Document(
                "$group",
                new Document("_id", "$dishes.name")
                        .append("total", new Document("$sum", "$dishes.quantity"))
        );

//...   { $sort: { count: -1 }}
        Bson sort = new Document("$sort", new Document("total", -1));
//...   { $limit: 1 }
        Bson limit = new Document("$limit", 1);


        try
        {
            List<Document> result = collection.aggregate(
                    Arrays.asList(match, unwind, group, sort, limit)).into(new ArrayList<>());
            List<AnalyticsDTO> toSet = new ArrayList<>();

            if(result.size() == 0)
                return toReturn;

            for(Document d : result)
            {
                AnalyticsDTO toAppend = new AnalyticsDTO();

                toAppend.setDish(d.get("_id").toString());
                toAppend.setCount(d.getInteger("total"));

                toSet.add(toAppend);
            }

            toReturn.setList(toSet);
            toReturn.setItemCount(toSet.size());

        }
        catch (MongoException e)
        {
            System.err.println(e.getMessage());
        }

        return toReturn;
    }

    // Vedo qual è stato il piatto più venduto del mese
    public ListDTO<AnalyticsDTO> getDeliveryTime(String zipcode)
    {
        ListDTO<AnalyticsDTO> toReturn = new ListDTO<>();
        MongoCollection<Document> collection = getDatabase().getCollection("Orders");

//...   {$match:
//      {
//        zipcode: "00162",
//        deliveryDate: {$ne: null}
//      }}
        Bson match = new Document(
                "$match",
                new Document("zipcode", zipcode)
                        .append("deliveryDate", new Document("$ne", "null"))
        );

//...   {$project:
//      {
//        restaurantId: "$restaurantId",
//        restaurant: "$restaurant",
//        deliveryTime: {
//          $dateDiff: {
//            startDate: "$creationDate",
//            endDate: "$deliveryDate",
//            unit: "minute"
//          }
//        }
//      }
//      }}
        Bson project = new Document(
                "$project",
                new Document("restaurantId","$restaurantId")
                .append("restaurant", "$restaurant")
                .append("deliveryTime", new Document("$dateDiff",
                        new Document("startDate", "$creationDate")
                                .append("endDate", "$deliveryDate")
                                .append("unit", "minute")
                )));

//...   $group:
//      {
//        _id:"$restaurantId",
//        restaurant:{$first:"$restaurant"},
//        deliveryAvgTime:
//        {
//          $avg: "$deliveryTime"
//        }
//      }
        Bson group = new Document(
                "$group",
                new Document("_id", "$restaurantId")
                        .append("restaurant", new Document("$first", "$restaurant"))
                        .append("deliveryAvgTime", new Document("$avg", "$deliveryTime"))
        );

//...   { $sort: { deliveryAvgTime: 1 }}
        Bson sort = new Document("$sort", new Document("deliveryAvgTime", 1));

        try
        {
            List<Document> result = collection.aggregate(
                    Arrays.asList(match, project, group, sort)).into(new ArrayList<>());
            List<AnalyticsDTO> toSet = new ArrayList<>();

            if(result.size() == 0)
                return toReturn;

            for(Document d : result)
            {
                AnalyticsDTO toAppend = new AnalyticsDTO();

                toAppend.setRestaurant(d.get("restaurant").toString());
                toAppend.setDouble(d.getDouble("deliveryAvgTime"));

                toSet.add(toAppend);
            }

            toReturn.setList(toSet);
            toReturn.setItemCount(toSet.size());

        }
        catch (MongoException e)
        {
            System.err.println(e.getMessage());
        }

        return toReturn;
    }
}
