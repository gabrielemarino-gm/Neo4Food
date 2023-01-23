package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.client.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import it.unipi.lsmsd.neo4food.dto.UserDTO;
import org.bson.Document;
public class UserMongoDAO extends BaseMongo{

    public UserDTO getUser(String e, String password){

        MongoCollection<Document> collection = getDatabase().getCollection("Users");

        try(MongoCursor cursor = collection.find(and(eq("mail",e),eq("password",password))).limit(1).iterator()
        ){
            if (cursor.hasNext()) {
                Document res = (Document) cursor.next();
//            One user found
                String id = res.get("_id").toString();
                String username = res.get("username").toString();
                String email = res.get("mail").toString();

                return new UserDTO(id, username, email);
            }
//            No match
            return new UserDTO("0", "", "");
        }
    }
}
