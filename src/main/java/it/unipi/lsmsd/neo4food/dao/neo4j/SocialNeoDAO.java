package it.unipi.lsmsd.neo4food.dao.neo4j;

import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import static org.neo4j.driver.Values.parameters;

public class SocialNeoDAO extends BaseNeo4J{

    /** Imposta un amicizia tra due utenti
     *
     * INPUT - USERNAME 1 and USERNAME 2*/
    public void setFriendship(String user1 , String user2) {
        String setQuery = "MATCH (u1:User), (u2:User) " +
                          "WHERE u1.username = $user1 AND u2.username = $user2 " +
                          "MERGE (u1)-[:IS_FRIEND]->(u2) " +
                          "MERGE (u2)-[:IS_FRIEND]->(u1)";

        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run(setQuery, parameters( "user1", user1 ,  "user2",user2  ))
                        .consume();
                return 1;
            });
        }
    }

    /** Imposta un commento da parte di un utente verso un ristorante
     *
     *  Se un commento esiste gia, quello vecchio viene sovrascritto */

    public void setRating(String user , String restaurant, String raddress, int rate) {
        try (Session session = driver.session()) {

            String searchQuery = "MATCH (u:User)-[rate:RATED]->(r:Restaurant) " +
                                 "WHERE u.username = $user AND r.name = $restaurant AND r.address = $raddress " +
                                 "RETURN r";

            session.writeTransaction(tx -> {
//      Controllo se commento esiste gia
                Result result = tx.run(searchQuery, parameters("user", user, "restaurant", restaurant ,"raddress" ,raddress ));

//      Se esiste già, aggiorna il rating
                if(result.hasNext()) {
                    String modifyQuery = "MATCH (u:User)-[rate:RATED]->(r:Restaurant) " +
                                         "WHERE u.username = $user AND r.name = $restaurant AND r.address = $raddress " +
                                         "SET rate.rating = $rating";

                    tx.run(modifyQuery, parameters("user", user, "restaurant", restaurant, "raddress", raddress, "rating", rate)).consume();

                }
//      Se non esiste, aggiungo nuovo rating
                else {
                    String setQuery = "MATCH (u:User) ,(r:Restaurant) " +
                                      "WHERE u.username = $user AND r.name = $restaurant AND r.address = $raddress " +
                                      "MERGE (u)-[:RATED{rating: $rating}]->(r)";
                    tx.run(setQuery, parameters("user", user, "restaurant", restaurant, "raddress", raddress, "rating", rate)).consume();
                }
                return 1;
            });
        }
    }

    /** Esegue una raccomandazione ad un utente di 10 ristoranti
     *
     *  Fornisce i ristoranti piu valutati dagli amici
     *  come nome, media dei voti e numero di voti*/
    public void getRecommendation(String user, String zipcode) {
        try (Session session = driver.session()) {
            String searchQuery = "MATCH (u1:User)-[:IS_FRIEND]->(u2:User)-[rate:RATED]->(r:Restaurant) " +
                                 "WHERE u1.username = $user and r.zipcdoe = $zipcode" +
                                 "WITH r, COUNT(rate.rating) as nrating, " +
                                 "AVG(rate.rating) as avg_rating " +
                                 "ORDER BY nrating, avg_rating DESC " +
                                 "RETURN r.name as name, avg_rating " +
                                 "LIMIT 10";

            session.writeTransaction(tx -> {
                Result result = tx.run(searchQuery, parameters( "user", user, "zipcode", zipcode));
                System.out.println(result.list());

                return 1;
            });
        }
    }

    @Override
    public void close() throws RuntimeException{
        driver.close();
    }
}
