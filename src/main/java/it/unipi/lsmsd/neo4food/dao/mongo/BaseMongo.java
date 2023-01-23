package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.client.*;

public abstract class BaseMongo {
    private static final String USERNAME = "extern";
    private static final String PASSWORD = "12345678";
    private static final String ADDRESS = "172.16.4.203";
    private static final Integer PORT = 27017;

    private static final String DATABASE = "neo4food";

    private static String URL_FORMAT = "mongodb://%s:%s@%s:%d";

    private static MongoClient clientConnection = null;
    private static MongoDatabase clientDatabase = null;

    public static MongoDatabase getDatabase(){
        if(clientDatabase != null){
            return clientDatabase;
        }
        return init();
    }

    private static MongoDatabase init(){
        String url = String.format(URL_FORMAT, USERNAME, PASSWORD, ADDRESS, PORT);
        clientConnection = MongoClients.create(url);
        clientDatabase = clientConnection.getDatabase(DATABASE);
        return clientDatabase;
    }

    public static void closePool() {
        clientConnection.close();
    }
}
