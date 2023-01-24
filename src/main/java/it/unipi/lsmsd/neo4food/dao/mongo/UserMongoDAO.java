package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.MongoException;
import com.mongodb.client.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.result.InsertOneResult;
import it.unipi.lsmsd.neo4food.dto.UserDTO;
import it.unipi.lsmsd.neo4food.model.User;

import org.bson.Document;
import org.bson.types.ObjectId;

public class UserMongoDAO extends BaseMongo{

    public UserDTO getUser(String e, String password){

        MongoCollection<Document> collection = getDatabase().getCollection("Users");

        try(MongoCursor cursor = collection.find(and(eq("email", e),eq("password",password))).limit(1).iterator()
        ){
            if (cursor.hasNext()) {
                Document res = (Document) cursor.next();
//            One user found
                String id = res.get("_id").toString();
                String username = res.get("username").toString();
                String firstName = res.get("firstname").toString();
                String lastName = res.get("lastname").toString();
                String email = res.get("email").toString();
                String phoneNumber = res.get("phonenumber").toString();
                String address = res.get("address").toString();
                String zipcode = res.get("zipcode").toString();

                return new UserDTO(id, username, firstName, lastName, email, phoneNumber, address, zipcode);
            }
//            No match
            return new UserDTO("0", "", "","","","","","");
        }
    }

    public void registerUser(User user){
        MongoCollection<Document> collection = getDatabase().getCollection("Users");

        try{
            InsertOneResult result = collection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("firstname", user.getFirstName())
                    .append("lastname", user.getLastName())
                    .append("username", user.getUsername())
                    .append("email", user.getEmail())
                    .append("phonenumber" ,user.getPhoneNumber())
                    .append("address", user.getAddress())
                    .append("zipcode", user.getZipcode())
                    .append("password", user.getPassword()));

        }catch (MongoException e){
            System.err.println(e);
        }
    }
}
