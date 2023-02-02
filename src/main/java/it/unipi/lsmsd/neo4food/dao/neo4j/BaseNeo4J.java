package it.unipi.lsmsd.neo4food.dao.neo4j;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

public abstract class BaseNeo4J implements AutoCloseable{

//    URI
    private static final String USERNAME = "neo4j";
    private static final String PASSWORD = "12345678";

    private static final String URL = "10.1.1.9";
    private static final String PORT = "7687";

    private static final String URL_FORMAT = "neo4j://%s:%s";

//    VARIABLES

    protected static Driver driver = null;

    public static Session getSession(){
        return getDriver().session();
    }

    private static Driver getDriver(){
        if(driver != null){
            return driver;
        }
        String uri = String.format(URL_FORMAT, URL, PORT);
        return GraphDatabase.driver(uri, AuthTokens.basic(USERNAME, PASSWORD));
    }
}
