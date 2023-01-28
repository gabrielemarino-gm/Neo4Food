package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.MongoException;
import com.mongodb.client.*;

import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import it.unipi.lsmsd.neo4food.dto.DishDTO;
import it.unipi.lsmsd.neo4food.dto.OrderDTO;
import it.unipi.lsmsd.neo4food.dto.UserDTO;
import it.unipi.lsmsd.neo4food.model.User;

import org.bson.Document;
import org.bson.types.ObjectId;

import javax.print.Doc;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class UserMongoDAO extends BaseMongo{

        //LOGIN - Email, Password
        //CREDENTIALS - Username, Email
    public Boolean userExists(String usr, String eml)
    {
        MongoCollection<Document> collection = getDatabase().getCollection("Users");
        try(MongoCursor cursor = collection.find(or(eq("email", eml),eq("username",usr))).limit(1).iterator();)
        {
            if (cursor.hasNext()) {
                return true;
            }
        }
        return false;
    }

    public UserDTO getUserLogin(String eml, String psw)
    {
        MongoCollection<Document> collection = getDatabase().getCollection("Users");
        UserDTO userDTO = new UserDTO();

        try(MongoCursor cursor = collection.find(and(eq("email", eml),eq("password",psw))).limit(1).iterator();)
        {
            if (cursor.hasNext())
            {
                Document res = (Document) cursor.next();
//              One user found

                String id = res.get("_id").toString();
                String username = res.get("username").toString();
                String firstName = res.get("firstname").toString();
                String lastName = res.get("lastname").toString();
                String email = res.get("email").toString();
                String phoneNumber = res.get("phonenumber").toString();
                String address = res.get("address").toString();
                String zipcode = res.get("zipcode").toString();

                userDTO.setId(id);
                userDTO.setUsername(username);
                userDTO.setFirstName(firstName);
                userDTO.setLastName(lastName);
                userDTO.setEmail(email);
                userDTO.setPhoneNumber(phoneNumber);
                userDTO.setAddress(address);
                userDTO.setZipcode(zipcode);

                return userDTO;
            }

//          No match
            userDTO.setId("0");
            return userDTO;
        }
        catch (MongoException mongoException)
        {
            System.err.println("Mongo Exception: " + mongoException.getMessage());
        }

//      No match
        userDTO.setId("0");
        return userDTO;
    }

    public void registerUser(User user)
    {
        MongoCollection<Document> collection = getDatabase().getCollection("Users");

        try
        {
            InsertOneResult result = collection.insertOne(
                    new Document()
                    .append("_id", new ObjectId())
                    .append("firstname", user.getFirstName())
                    .append("lastname", user.getLastName())
                    .append("username", user.getUsername())
                    .append("email", user.getEmail())
                    .append("phonenumber" ,user.getPhoneNumber())
                    .append("address", user.getAddress())
                    .append("zipcode", user.getZipcode())
                    .append("password", user.getPassword())
                    .append("paymentMethod", "")
                    .append("paymentNumber", "")
            );

        }
        catch (MongoException mongoException)
        {
            System.err.println("Mongo Exception: " + mongoException.getMessage());
        }
    }

    public void insertOrder(OrderDTO order)
    {
        // Devo aggiungere sia in Ordini che in Ristoranti
        MongoCollection<Document> collectionOrders = getDatabase().getCollection("Orders");
        MongoCollection<Document> collectionRestaurants = getDatabase().getCollection("Restaurants");

        // Devo aggiungere un ordine
        // Un ordine ha dei campi e una lista di piatti
        // Inizio creando una lista di documenti di tipo piatto
        // Aggiungo i campi importanti per ogni piatto nell'ordine
        List<Document> dishesList = new ArrayList<>();
        for(DishDTO item: order.getDishes())
        {
            dishesList.add(new Document("_id", new ObjectId())
                        .append("name", item.getName())
                        .append("price", item.getPrice())
                        .append("currency", item.getCurrency().replace(" ",""))
                        .append("quantity", item.getQuantity()));
        }

        // A questo punto creo il documento dell ordine
        Document toInsert = new Document("_id", new ObjectId()).
                append("user", order.getUser()).
                append("restaurant", order.getRestaurant()).
                append("restaurantId", order.getRestaurantId()).
                append("paymentMethod", order.getPaymentMethod()).
                append("paymentNumber", order.getPaymentNumber()).
                append("address", order.getAddress()).
                append("zipcode", order.getZipcode()).
                append("total", order.getTotal()).
                append("status", order.getStatus()).
                append("dishes", dishesList);

        collectionOrders.insertOne(toInsert);

//        L'ordine da inserire sotto il ristorante ha alcuni campi in meno
//        I piatti non devono avere price e currency
//        L ordine non deve avere restaurant, restaurantId e status
        for(Document i : dishesList){
            i.remove("price");
            i.remove("currency");
        }
        toInsert.remove("restaurant");
        toInsert.remove("restaurantId");
        toInsert.remove("status");

        toInsert.remove("dishes");

        toInsert.append("dishes", dishesList);

        collectionRestaurants.updateOne(
                eq("_id", new ObjectId(order.getRestaurantId())),
                Updates.addToSet("orders", toInsert));
    }
}
