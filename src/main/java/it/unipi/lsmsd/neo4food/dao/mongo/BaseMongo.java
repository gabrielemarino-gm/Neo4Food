package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.*;

public abstract class BaseMongo {
//    OLD SERVER

//    private static final String USERNAME = "extern";
//    private static final String PASSWORD = "12345678";
//    private static final String ADDRESS = "172.16.4.203";
//    private static final Integer PORT = 27017;
//    private static final String URL_FORMAT = "mongodb://%s:%s@%s:%d";
//    private static final String DATABASE = "neo4food";

//    NEW SERVER
    private static final String NODE01 = "10.1.1.9";
    private static final String NODE02 = "10.1.1.10";
    private static final String NODE03 = "10.1.1.11";
    private static final Integer PORT01 = 27017;
    private static final Integer PORT02 = 27018;
    private static final Integer PORT03 = 27019;
    private static final String DATABASE = "Neo4Food";

    private static String URL_FORMAT = "mongodb://%s:%s,%s:%s,%s:%s";

    private static MongoClient clientConnection = null;
    private static MongoDatabase clientDatabase = null;

    public static MongoDatabase getDatabase(){
        if(clientDatabase != null){
            return clientDatabase;
        }
        return init();
    }

    private static MongoDatabase init(){

        ConnectionString uri = new ConnectionString(String.format(URL_FORMAT,NODE01,PORT01,NODE02,PORT02,NODE03,PORT03));
        MongoClientSettings mcs = MongoClientSettings.builder()
                .applyConnectionString(uri)
                .readPreference(ReadPreference.primaryPreferred())
                .writeConcern(WriteConcern.W1)
                .build();

        clientConnection = MongoClients.create(mcs);
        clientDatabase = clientConnection.getDatabase(DATABASE);

        return clientDatabase;
    }

    public static void closePool() {
        clientConnection.close();
    }
}
