package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import javax.print.Doc;

import static com.mongodb.client.model.Filters.*;

public class AdminMongoDAO extends BaseMongo
{
    public boolean isTokenValid(String token)
    {
        MongoCollection<Document> collection = getDatabase().getCollection("Users");

        try (MongoCursor cursor = collection.find(and(eq("isAdmin", true), eq("token", token))).cursor())
        {
            if (cursor.hasNext())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (MongoException e)
        {
            System.err.println(e);
        }
        finally {
            closePool();
        }

        return false;
    }

    public long userCount()
    {
        MongoCollection<Document> collection = getDatabase().getCollection("Users");

        try
        {
            return collection.countDocuments();
        }
        catch (MongoException e)
        {
            System.out.println(e.getMessage());
        }
        finally {
            closePool();
        }
        return 0;
    }


    public long restCount()
    {
        MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");
        try
        {
            return collection.countDocuments();
        }
        catch (MongoException e)
        {
            System.out.println(e.getMessage());
        }
        finally {
            closePool();
        }
        return 0;
    }

    public long orderCount()
    {
        MongoCollection<Document> collection = getDatabase().getCollection("Orders");
        try
        {
            return collection.countDocuments();
        }
        catch (MongoException e)
        {
            System.out.println(e.getMessage());
        }
        finally {
            closePool();
        }
        return 0;
    }
}
