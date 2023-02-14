package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.*;

public class AdminMongoDAO extends BaseMongo
{
    public boolean isTokenValid(String token)
    {
        MongoCollection<Document> collection = getDatabase().getCollection("Users");

        try (MongoCursor cursor = collection.find(and(eq("isAdmin", true), eq("token", token))).cursor())
        {
            return cursor.hasNext();
        }
        catch (MongoException e)
        {
            System.err.println(e);
            return false;
        }


    }

    public Document getPopulation()
    {
        try
        {
            MongoDatabase collection = getDatabase();

            return new Document("uCount", collection.getCollection("Users").countDocuments())
                   .append("rCount", collection.getCollection("Restaurants").countDocuments())
                   .append("oCount", collection.getCollection("Orders").countDocuments());
        }
        catch (MongoException e)
        {
            System.err.println(e.getMessage());
            return new Document("uCount", 0)
                    .append("rCount", 0)
                    .append("oCount", 0);
        }

    }
}
