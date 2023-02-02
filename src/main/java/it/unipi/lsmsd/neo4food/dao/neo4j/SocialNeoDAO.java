package it.unipi.lsmsd.neo4food.dao.neo4j;

import it.unipi.lsmsd.neo4food.dto.CommentDTO;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.util.ArrayList;
import java.util.List;

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


    /** Esegue una raccomandazione ad un utente di 10 ristoranti
     *
     *  Fornisce i ristoranti piu valutati dagli amici
     *  come nome, media dei voti e numero di voti*/
    public void getRecommendation(String user, String zipcode) {
        try (Session session = driver.session()) {
            String searchQuery = "MATCH (u1:User)-[:IS_FRIEND]->(u2:User)-[rate:RATED]->(r:Restaurant) " +
                                 "WHERE u1.username = $user and r.zipcode = $zipcode" +
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

    /** Imposta un commento da parte di un utente verso un ristorante
     *
     *  Se un commento esiste gia, quello vecchio viene sovrascritto
     *
     *  INPUT - ReastaurantId, username utente, rating, review testuale */
    public void setRating(String user , String rid, int rate, String review) {
        try (Session session = driver.session()) {


            String searchQuery = "MATCH (u:User)-[rate:RATED]->(r:Restaurant) " +
                                 "WHERE u.username = $user AND r.rid = $rid " +
                                 "RETURN r";

            session.writeTransaction(tx -> {
//      Controllo se commento esiste gia
                Result result = tx.run(searchQuery, parameters("user", user, "rid", rid));

//      Se esiste già, aggiorna il rating
                if(result.hasNext()) {
                    String modifyQuery = "MATCH (u:User)-[rate:RATED]->(r:Restaurant) " +
                                         "WHERE u.username = $user AND r.rid = $rid " +
                                         "SET rate.rating = $rating, rate.review = $review";

                    tx.run(modifyQuery, parameters("user", user, "rid", rid, "rating", rate, "review", review)).consume();

                }
//      Se non esiste, aggiungo nuovo rating
                else {
                    String setQuery = "MATCH (u:User) ,(r:Restaurant) " +
                                      "WHERE u.username = $user AND r.rid = $rid " +
                                      "MERGE (u)-[:RATED{rating: $rating, review: $review}]->(r)";

                    tx.run(setQuery, parameters("user", user, "rid", rid, "rating", rate, "review", review)).consume();
                }
                return 1;
            });
        }
    }

    /** Ritorna una lista di 20 commenti del ristorante richiest
     *
     *  INPUT - restaurantId, pagina di commenti */
    public ListDTO<CommentDTO> getComments(String restaurantid, int page){

        try (Session session = driver.session()) {
            String searchQuery = "MATCH (r:Restaurant)<-[rate:RATED]-(u:User) " +
                    "WHERE r.rid = $rid " +
                    "RETURN u.name as user, rate.rating as rate, rate.review as comment " +
                    "SKIP $skip " +
                    "LIMIT 20";

            session.writeTransaction(tx -> {
                Result result = tx.run(searchQuery, parameters( "rid", restaurantid, "skip", (page*20)));
                ListDTO<CommentDTO> toReturn = new ListDTO<CommentDTO>();
                List<CommentDTO> tempList = new ArrayList<CommentDTO>();

                while(result.hasNext()){
                    Record r = result.next();
                    CommentDTO tempComment = new CommentDTO();

                    tempComment.setUserName(r.get("user").asString());
                    tempComment.setRate(r.get("rate").asDouble());
                    tempComment.setCommentText(r.get("comment")!= null ? r.get("comment").asString() : "");

                    tempList.add(tempComment);
                }

                toReturn.setList(tempList);
                toReturn.setItemCount(tempList.size());

                return toReturn;
            });
        }
        return null;
    }

    @Override
    public void close() throws RuntimeException{
        driver.close();
    }
}
