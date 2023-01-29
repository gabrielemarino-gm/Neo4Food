package it.unipi.lsmsd.neo4food.dao.neo4j;

import org.neo4j.driver.*;
import org.neo4j.driver.summary.ResultSummary;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class BaseNeo4j implements AutoCloseable {
    private final Driver driver;

    //construct driver instance: a connection URI and
    //authentication information must be supplied. Using Basic Authentication
    public BaseNeo4j(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws RuntimeException {
        driver.close();
    }



    //session with transaction function example
    public void addUser(final String userName) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("MERGE (u:User {username: $userName})", parameters("userName", userName))
                        .consume();
                return 1;
            });
        }
    }

    public void addRestaurant(final String name , final String address) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("MERGE (r:Restaurant {name: $name , address:$address}) ", parameters("name", name , "address" , address))
                        .consume();
                return 1;
            });
        }
    }

    public void setFriendship(final String user1 , final String user2) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("MATCH (u1:User), (u2:User) WHERE u1.username = $user1 AND u2.username = $user2  MERGE (u1)-[:IS_FRIEND]->(u2) MERGE (u2)-[:IS_FRIEND]->(u1)", parameters( "user1", user1 ,  "user2",user2  ))
                        .consume();
                return 1;
            });
        }
    }

    public void setRating(final String user , final String restaurant,final String raddress,final double rate) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
//Effettua una Query a Neo4j per controllare se un rating esiste già
                Result result = tx.run("MATCH (u:User)-[rate:RATED]->(r:Restaurant) WHERE u.username = $user AND r.name = $restaurant AND r.address=$raddress  RETURN r",
                        parameters("user", user, "restaurant", restaurant ,"raddress" ,raddress ));

//Se esiste già, aggiorna il rating
                if(result.hasNext()) {

                    tx.run("MATCH (u:User)-[rate:RATED]->(r:Restaurant) WHERE u.username = $user AND r.name = $restaurant AND r.address = $raddress  SET rate.rating = $rating ",
                                    parameters("user", user, "restaurant", restaurant, "raddress", raddress, "rating", rate))
                            .consume();

                }
                //Se non esiste, Aggiungi la relazione RATED con relativo Punteggio
                else {
                    tx.run("MATCH (u:User) ,(r:Restaurant) WHERE u.username = $user AND r.name = $restaurant AND r.address = $raddress  MERGE (u)-[:RATED{rating: $rating}]->(r) ",
                                    parameters("user", user, "restaurant", restaurant, "raddress", raddress, "rating", rate))
                            .consume();

                }


                return 1;
            });
        }
    }

    public void setFollow(final String user , final String restaurant,final String raddress) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                //Crea la Relazione IS_FOLLOW tra due utenti
                tx.run("MATCH (u:User), (r:Restaurant) WHERE u.username = $user AND r.name = $restaurant AND r.address = $raddress  MERGE (u)-[:FOLLOWS]->(r) ", parameters( "user", user ,  "restaurant",restaurant , "raddress" ,raddress  ))
                        .consume();
                return 1;
            });
        }
    }

    public void getRecommendation(final String user) {
        try (Session session = driver.session()) {
            //Esegue una raccomandazione del ristorante in base agli amici di user(Li ordina in base alle recensioni medie) ----> Da sistemare, sono più importanti il numero di recensioni, la media delle recensioni degli amici o la media delle recensioni degli utenti?
            session.writeTransaction(tx -> {
                Result result = tx.run("MATCH (u1:User)-[:IS_FRIEND]->(u2:User)-[rate:RATED]->(r:Restaurant) WHERE u1.username = $user WITH r, COUNT(rate.rating) as nrating,AVG(rate.rating) as avg_rating ORDER BY avg_rating DESC RETURN r.name as name, avg_rating, nrating", parameters( "user", user ));
                System.out.println(result.list());

                return 1;
            });
        }

    }

    public void getBestRestaurants() {
        try (Session session = driver.session()) {
            //Utente con più (Recensioni e Amici)
            session.writeTransaction(tx -> {
                Result result = tx.run("MATCH (u1:User)-[rate:RATED]->(r:Restaurant) WITH r.name as name , COUNT(rate.rating) as nrating, AVG(rate.rating) as avg_rating ORDER BY nrating,avg_rating DESC RETURN name , avg_rating, nrating LIMIT 20");
                System.out.println(result.list());

                return 1;
            });
        }

    }

    public void getBestUsers() {
        try (Session session = driver.session()) {
            //Utente con più (Recensioni e Amici)
            session.writeTransaction(tx -> {
                //Trova Top 20 Utenti che hanno recensito di più
                Result result = tx.run("MATCH (u1:User)-[rate:RATED]->(r:Restaurant) WITH u1.username as username,COUNT(rate.rating) as nrating, AVG(rate.rating) as avg_rating ORDER BY nrating DESC RETURN username, avg_rating, nrating LIMIT 20");
                System.out.println(result.list());

                return 1;
            });
        }

    }

    public void getInfluencers() {
        try (Session session = driver.session()) {
            //Utente con più (Recensioni e Amici)
            session.writeTransaction(tx -> {
                //Trova Top 20 Utenti che hanno recensito di più
                Result result = tx.run("MATCH (u1:User)-[x:IS_FRIEND]->(u2:User) WITH u1.username as username,COUNT(x) as nfriends ORDER BY nfriends DESC RETURN username , nfriends LIMIT 20");
                System.out.println(result.list());

                return 1;
            });
        }

    }



  //TRASFERIRE TUTTI I METODI NELLE APPOSITE CARTELLE ED ELIMINARE IL MAIN
  //TRASFERIRE TUTTI I METODI NELLE APPOSITE CARTELLE ED ELIMINARE IL MAIN
  //TRASFERIRE TUTTI I METODI NELLE APPOSITE CARTELLE ED ELIMINARE IL MAIN
    public static void main( String... args ) throws Exception {
        //construct driver instance: a connection URI and
        //authentication information must be supplied. Using Basic Authentication
        //connecting to Neo4j Server, local instance
        BaseNeo4j neo4j = new BaseNeo4j("neo4j://localhost:7687", "neo4j", "12345678");

        //adding new node
        System.out.println("\nAdding new Person node to DB ...");
        neo4j.addUser("Prato");
        System.out.println("------------------------------------------------");
        neo4j.addUser("Baudo");
        neo4j.setFriendship("Pippo","Baudo");
        System.out.println("Amicizia è stata fatta!");
        neo4j.addRestaurant("Trattoria da Lucazz" , "Via dei Puppi , 33");
        System.out.println("Ristorante Aggiunto!");
        neo4j.setRating("Baudo","Cucina di Pippo" , "Via dei Pippi , 11" , 9);
        neo4j.setRating("Prato","Cucina di Pippo" , "Via dei Pippi , 11" , 2);

        neo4j.setFollow("Pippo","Cucina di Pippo" , "Via dei Pippi , 11");
        neo4j.getRecommendation("Pippo");
        neo4j.getBestRestaurants();
        neo4j.getBestUsers();
        neo4j.getInfluencers();
    }
}
