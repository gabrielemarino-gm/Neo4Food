package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.client.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import it.unipi.lsmsd.neo4food.dto.UserDTO;
import it.unipi.lsmsd.neo4food.model.User;

import org.bson.Document;

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
                String birthday = res.get("birthday").toString();

                return new UserDTO(id, username, firstName, lastName, email, phoneNumber, address, zipcode, birthday);
            }
//            No match
            return new UserDTO("0", "", "","","","","","","");
        }
    }

    public void registerUser(User user){

    }
}
