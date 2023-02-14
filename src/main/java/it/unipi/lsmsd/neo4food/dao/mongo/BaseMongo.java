package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.*;

public abstract class BaseMongo
{
    private static final String NODE01 = "10.1.1.9";
    private static final String NODE02 = "10.1.1.10";
    private static final String NODE03 = "10.1.1.11";
    private static final Integer PORT01 = 27017;
    private static final Integer PORT02 = 27018;
    private static final Integer PORT03 = 27019;
    private static final String DATABASE = "Neo4Food";

    private static final String URL_FORMAT = "mongodb://%s:%s,%s:%s,%s:%s";

    private static MongoClient clientConnection = null;

    public static MongoDatabase getDatabase()
    {
        if(clientConnection != null)
        {
            return clientConnection.getDatabase(DATABASE);
        }

        return initClient().getDatabase(DATABASE);

    }

    public static ClientSession getSession()
    {
        if (clientConnection != null) {
            return clientConnection.startSession();
        }

        return initClient().startSession();

    }

    private static MongoClient initClient()
    {
        ConnectionString uri = new ConnectionString(String.format(URL_FORMAT,NODE01,PORT01,NODE02,PORT02,NODE03,PORT03));
        MongoClientSettings mcs = MongoClientSettings.builder()
                .applyConnectionString(uri)
                .readPreference(ReadPreference.nearest())
                .writeConcern(WriteConcern.W1)
                .build();

        clientConnection = MongoClients.create(mcs);
        return clientConnection;
    }

    public static void close()
    {
        clientConnection.close();
        clientConnection = null;
    }
}