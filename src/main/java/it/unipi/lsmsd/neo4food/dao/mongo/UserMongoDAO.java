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
        MongoCollection<Document> collectionRest = getDatabase().getCollection("Restaurants");
        MongoCollection<Document> collectionOrd = getDatabase().getCollection("Orders");

        // Devo aggiungere un ordine
        // Un ordine ha dei campi e una lista di piatti
        // Inizio creando una lista di documenti di tipo piatto
        // Aggiungo i campi importanti per ogni piatto nell'ordine
        List<Document> dishes = new ArrayList<>();
        for(DishDTO item: order.getItems())
        {
            dishes.add(new Document("_id", new ObjectId())
                    .append("name", item.getName())
                    .append("price", item.getPrice())
                    .append("quantity", item.getQuantity())
                    .append("currency", item.getCurrency().replace(" ","")));
        }

        // A questo punto creo il documento dell ordine
        Document toInsert = new Document("_id", new ObjectId()).
                append("user", order.getUser()).
                append("restaurant", order.getRestaurant()).
                append("paymentMethod", order.getPaymentMethod()).
                append("paymentNumber", order.getPaymentNumber()).
                append("address", order.getAddress()).
                append("zipcode", order.getZipcode()).
                append("total", order.getTotal()).
                append("status", order.getStatus()).
                append("dish", dishes);

        collectionOrd.insertOne(toInsert);
        collectionRest.updateOne(eq("name", order.getRestaurant()),
                            Updates.addToSet("orders", toInsert));
    }
}
