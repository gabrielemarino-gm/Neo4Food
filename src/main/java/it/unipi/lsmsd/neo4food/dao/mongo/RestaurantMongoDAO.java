package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import it.unipi.lsmsd.neo4food.constants.Constants;
import it.unipi.lsmsd.neo4food.dto.DishDTO;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.dto.OrderDTO;
import it.unipi.lsmsd.neo4food.dto.RestaurantDTO;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import static it.unipi.lsmsd.neo4food.utility.Utilities.unpackDishes;
import static it.unipi.lsmsd.neo4food.utility.Utilities.unpackOrders;

public class RestaurantMongoDAO extends BaseMongo
{
//--------------------------------------------------------------
//---USED BY USER TO SEARCH RESTAURANTS BY ZIPCODE AND FILTER---
//--------------------------------------------------------------
    public ListDTO<RestaurantDTO> getRestaurantsForSearchPage(int page, String zipcode, String filter)
    {
        ListDTO<RestaurantDTO> toReturn = new ListDTO<RestaurantDTO>();
        List<RestaurantDTO> tempList = new ArrayList<RestaurantDTO>();

        int count = 0;
        int offset = page * Constants.PAGE_SIZE;
        MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");
        Bson query = new Document();
        if(filter.equals("") || filter == null){
//          Query senza category
            query = Filters.eq("zip_code", zipcode);
        }else{
//          Query con category
            query = Filters.and(
                    Filters.eq("zip_code", zipcode),
                    Filters.regex("category",".*"+filter+".*"));
        }

        try(MongoCursor cursor = collection.find(query).sort(ascending("position")).limit(Constants.PAGE_SIZE).skip(offset).iterator())
        {
            while (cursor.hasNext()){
                Document res = (Document) cursor.next();

                String id = res.get("_id").toString();
                String name = res.get("name").toString();
                String range = res.get("price_range") != null ? res.get("price_range").toString() : "Price not available";
                Float rating = res.get("score") != null ? Float.parseFloat(res.get("score").toString()) : 0;

                RestaurantDTO e = new RestaurantDTO();
                e.setId(id);
                e.setName(name);
                e.setRating(rating);

                tempList.add(e);
                count++;
            }
        }

        toReturn.setList(tempList);
        toReturn.setItemCount(count);
        return toReturn;
    }

    //------------------------------------------------------------------
    //---USED BY RESTAURANT TO CHECK CORRECTNESS OF LOGIN CREDENTIALS---
    //------------------------------------------------------------------
    public RestaurantDTO getRestaurantLogin(String eml, String password)
    {
        MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");
        try(MongoCursor cursor = collection.find(
                and(
                        eq("email", eml),
                        eq("password",password)
                )
            ).limit(1).iterator()
        )
    //            INIZIO BLOCCO TRY
        {

            if (cursor.hasNext()) {

            Document res = (Document) cursor.next();

            String id = res.get("_id").toString();
            String name = res.get("name") != null ? res.get("name").toString() : "Name not available";
            Float rating = res.get("score") != null ? Float.parseFloat(res.get("score").toString()): 0;
            String address = res.get("full_address") != null ? res.get("full_address").toString() : "Address not available";
            String zipcode = res.get("zip_code") != null ? res.get("zip_code").toString() : "Zipcode not available";
            String email = res.get("email") != null? res.get("email").toString() : "Email not available";

            RestaurantDTO e = new RestaurantDTO();
            e.setId(id);
            e.setName(name);
            e.setRating(rating);
            e.setEmail(email);
            e.setAddress(address);
            e.setZipcode(zipcode);

            return e;
            }
        }

        RestaurantDTO e = new RestaurantDTO();
        e.setId("0");
        return e;
    }

//  -------------------------------------------------------------
//  ---USED BY RESTAURANT AND USER TO GET ONLY SELECTED FIELDS---
//  -------------------------------------------------------------
//  CASE 1 - Un utente richiede alcune informazioni e la lista dei piatti senza ordini (RESTAURANT - DETAILS)
//  CASE 2 - Restaurant itself wants to get only list of dishes without orders (CHECKOUT - SEND)
//  CASE 3 - Il ristorante richiede solo la lista di ordini pendenti (PERSONAL - RESTAURANT - PERSONAL) (LOGIN - RESTURANT)
    public RestaurantDTO getRestaurantDetails(String rid, boolean getDishes, boolean getOrders)
    {
//      Variabile che devo restituire
        RestaurantDTO toReturn = new RestaurantDTO();

        MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");
        Document projection = new Document();

        if(!getOrders){
            projection.append("orders", 0);
        }
        if(!getDishes){
            projection.append("dishes", 0);
        }

        try(MongoCursor cursor = collection.find(eq("_id", new ObjectId(rid))).projection(projection).limit(1).iterator())
        {
//          Per ogni documento trovato su MongoDB:
            while (cursor.hasNext())
            {
//              Itero il risultato
                Document res = (Document) cursor.next();

                toReturn.setId(res.get("_id") != null ? res.get("_id").toString() : "ID not available");
                toReturn.setName(res.get("name") != null ? res.get("name").toString() : "Name not available");
                toReturn.setEmail(res.get("email") != null ? res.get("email").toString() : "Email not available");
                toReturn.setPriceRange(res.get("price_range") != null ? res.get("price_range").toString() : "Price not available");
                toReturn.setAddress(res.get("full_address") != null ? res.get("full_address").toString() : "Address not available");
                toReturn.setRating(res.get("score") != null ? Float.parseFloat(res.get("score").toString()) : 0);

//              prendo liste documenti dishes and orders
                ArrayList<Document> dishDocuments = res.get("dishes") != null ? (ArrayList<Document>) res.get("dishes") : null;
                ArrayList<Document> orderDocuments = res.get("orders") != null ? (ArrayList<Document>) res.get("orders") : null;

                List<DishDTO> tempDishesList = new ArrayList<DishDTO>();
                List<OrderDTO> tempOrdersList = new ArrayList<OrderDTO>();

                if(dishDocuments != null && getDishes)
                {
                    unpackDishes(dishDocuments, tempDishesList);
                }

                if(orderDocuments != null && getOrders)
                {
                    unpackOrders(orderDocuments, tempOrdersList);
                }

                toReturn.setDishes(tempDishesList);
                toReturn.setPendingOrders(tempOrdersList);
            }
        }

        return toReturn;
    }

    public String addDish(DishDTO target)
    {
//      rid - dname - dprice - dcurr - ddesc

        Document query = new Document("_id", new ObjectId(target.getRestaurantId()));
        ObjectId oid = new ObjectId();
        Bson update = new Document("$push", new Document("dishes",
                            new Document("_id", oid)
                                    .append("name", target.getName())
                                    .append("price", target.getPrice().toString())
                                    .append("currency", target.getCurrency())
                                    .append("description", target.getDescription())
                            ));

        try{
            UpdateResult result = getDatabase().getCollection("Restaurants")
                                .updateOne(query, update);

            return oid.toString();
        }
        catch (MongoException e){
            System.err.println(e);
        }

        return "";
    }
    public int remDish(DishDTO target)
    {
//        rid - did
        Document query = new Document("_id", new ObjectId(target.getRestaurantId()));
        Bson update = new Document("$pull", new Document("dishes",
                    new Document("_id", new ObjectId(target.getId()))));

        try{
            UpdateResult result = getDatabase().getCollection("Restaurants")
                    .updateOne(query, update);

            return (int) result.getModifiedCount();
        }
        catch (MongoException e){
            System.err.println(e);
        }

        return 0;
    }

    public int modDish(DishDTO target)
    {
//      rid - did - dname - dprice - ddesc
        Document query = new Document("_id", new ObjectId(target.getRestaurantId()))
                .append("dishes._id", new ObjectId(target.getId()));

        Bson update = new Document("$set",
                        new Document("dishes.$.name",target.getName())
                                .append("dishes.$.price",target.getPrice().toString())
                                .append("dishes.$.description", target.getDescription())
                        );


        try{
            UpdateResult result = getDatabase().getCollection("Restaurants")
                    .updateOne(query, update);

            return (int) result.getModifiedCount();
        }
        catch (MongoException e){
            System.err.println(e);
        }

        return 0;
    }
}