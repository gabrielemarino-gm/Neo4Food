package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.MongoException;
import com.mongodb.client.*;

import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.dto.UserDTO;
import it.unipi.lsmsd.neo4food.model.User;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.model.Filters;

public class UserMongoDAO extends BaseMongo {

    //>>>>>>>>>>>>>OK<<<<<<<<<<<<<

    //LOGIN - Email, Password
    //CREDENTIALS - Username, Email
    public Boolean userExists(String usr, String eml)
    {
        MongoCollection<Document> collection = getDatabase().getCollection("Users");
        try(MongoCursor cursor = collection.find(or(eq("email", eml),eq("username",usr))).limit(1).iterator())
        {
            if (cursor.hasNext()) {
                return true;
            }
        }

        return false;
    }
//>>>>>>>>>>>>>OK<<<<<<<<<<<<<
    public UserDTO getUserLogin(String eml, String psw)
    {
        MongoCollection<Document> collection = getDatabase().getCollection("Users");
        UserDTO userDTO = new UserDTO();

        try(MongoCursor cursor = collection.find(and(eq("email", eml),eq("password",psw))).limit(1).iterator())
        {
            if (cursor.hasNext())
            {
                Document res = (Document) cursor.next();
//              One user found

                userDTO.setId(res.get("_id") != null ? res.get("_id").toString() : "ID not available");
                userDTO.setUsername(res.get("username") != null ? res.get("username").toString() : "Username not available");
                userDTO.setFirstName(res.get("firstname") != null ? res.get("firstname").toString() : "Firstname not available");
                userDTO.setLastName(res.get("lastname") != null ? res.get("lastname").toString() : "Lastname not available");
                userDTO.setEmail(res.get("email") != null ? res.get("email").toString() : "EMail not available");
                userDTO.setPhoneNumber(res.get("phonenumber") != null ? res.get("phonenumber").toString() : "Phone number not available");
                userDTO.setAddress(res.get("address") != null ? res.get("address").toString() : "Address not available");
                userDTO.setZipcode(res.get("zipcode") != null ? res.get("zipcode").toString() : "Zipcode not available");
                userDTO.setPaymentMethod(res.get("paymentMethod") != null ? res.get("paymentMethod").toString() : "");
                userDTO.setPaymentNumber(res.get("paymentNumber") != null ? res.get("paymentNumber").toString() : "");

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

//>>>>>>>>>>>>>OK<<<<<<<<<<<<<
    public UserDTO registerUser(User user)
    {
        MongoCollection<Document> collection = getDatabase().getCollection("Users");

        try
        {
            ObjectId id = new ObjectId();
            InsertOneResult result = collection.insertOne(
                    new Document()
                    .append("_id", id)
                    .append("firstname", user.getFirstName())
                    .append("lastname", user.getLastName())
                    .append("username", user.getUsername())
                    .append("email", user.getEmail())
                    .append("phonenumber" ,user.getPhoneNumber())
                    .append("address", user.getAddress())
                    .append("zipcode", user.getZipcode())
                    .append("password", user.getPassword())
//                   Non inserite in fase di registrazione, aggiunte dopo all occorrenza
                    .append("paymentMethod", user.getPaymentMethod())
                    .append("paymentNumber", user.getPaymentNumber())
            );

//          A questo punto se sono riuscito ad inserire un utente, questo
//          ha utilizzato credenziali mai usate prima
//          se quindi fa ACK, sono sicuro che l'inserimento ha avuto successo
//          posso quindi restituire un nuovo utente

            if(result.wasAcknowledged()){
                UserDTO userDTO = new UserDTO();

                userDTO.setId(id.toString());
                userDTO.setUsername(user.getUsername());
                userDTO.setFirstName(user.getFirstName());
                userDTO.setLastName(user.getLastName());
                userDTO.setEmail(user.getEmail());
                userDTO.setPhoneNumber(user.getPhoneNumber());
                userDTO.setAddress(user.getAddress());
                userDTO.setZipcode(user.getZipcode());
                userDTO.setPaymentMethod(user.getPaymentMethod());
                userDTO.setPaymentNumber(user.getPaymentNumber());

                return userDTO;
            }

        }
        catch (MongoException mongoException)
        {
            System.err.println("Mongo Exception: " + mongoException.getMessage());
        }

        return null;
    }

    public void removeUser(UserDTO user)
    {
        try
        {
            MongoCollection<Document> collection = getDatabase().getCollection("Users");
            DeleteResult result = collection.deleteOne(Filters.eq("username", user.getUsername()));
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }

    }

    public boolean updateUser(UserDTO user){

        Document query = new Document("_id", new ObjectId(user.getId()));
        Bson updates = Updates.combine(
                Updates.set("firstname", user.getFirstName()),
                Updates.set("lastname", user.getLastName()),
                Updates.set("phonenumber", user.getPhoneNumber()),
                Updates.set("address", user.getAddress()),
                Updates.set("zipcode", user.getZipcode()),
                Updates.set("paymentMethod", user.getPaymentMethod()),
                Updates.set("paymentNumber", user.getPaymentNumber())
                );

        try{
            UpdateResult result = getDatabase().
                                    getCollection("Users").
                                    updateOne(query,updates);

            if(result.getModifiedCount() == 1){
                return true;
            }

        }catch (MongoException e){
            System.err.println(e);
            return false;
        }

        return false;
    }

    public ListDTO<UserDTO> getUsers(String usr){
        ListDTO<UserDTO> toReturn = new ListDTO<>();

        MongoCollection<Document> collection = getDatabase().getCollection("Users");
        List<Document> qList = new ArrayList<>();

        qList.add(new Document("username", new Document("$regex", usr)));
        qList.add(new Document("firstname", usr));
        qList.add(new Document("lastname", usr));

        Document query = new Document("$or", qList);

        try(MongoCursor cursor = collection.find(query).limit(20).iterator())
        {
            List<UserDTO> toSet = new ArrayList<>();

            while (cursor.hasNext()) {
                Document res = (Document) cursor.next();
                UserDTO toAppend = new UserDTO();

                toAppend.setId(res.get("_id") != null ? res.get("_id").toString() : "ID not available");
                toAppend.setUsername(res.get("username") != null ? res.get("username").toString() : "Username not available");
                toAppend.setFirstName(res.get("firstname") != null ? res.get("firstname").toString() : "Firstname not available");
                toAppend.setLastName(res.get("lastname") != null ? res.get("lastname").toString() : "Lastname not available");

                toSet.add(toAppend);
            }
            
            toReturn.setList(toSet);
            toReturn.setItemCount(toSet.size());
        }

        return toReturn;
    }

}
