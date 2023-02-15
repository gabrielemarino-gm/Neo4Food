package it.unipi.lsmsd.neo4food.dao.neo4j;

import it.unipi.lsmsd.neo4food.constants.Constants;
import it.unipi.lsmsd.neo4food.dto.CommentDTO;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.dto.RestaurantDTO;
import it.unipi.lsmsd.neo4food.dto.UserDTO;
import java.util.ArrayList;
import java.util.List;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class SocialNeoDAO extends BaseNeo4J
{
    /** Imposta FOLLOW tra Follower e Following
     *
     * INPUT - Follower and Following */
    public void setFollow(String user1 , String user2)
    {
        String setQuery = "MATCH (u1:User), (u2:User) " +
                          "WHERE u1.username = $user1 AND u2.username = $user2 " +
                          "MERGE (u1)-[:FOLLOWS]->(u2)";

        try (Session session = getSession())
        {
            session.writeTransaction(tx -> {
                tx.run(setQuery, parameters( "user1", user1 ,  "user2",user2  ))
                        .consume();

                return 1;
            });
        }
    }

    public void removeFollow(String user1 , String user2)
    {
        String remQuery = "MATCH (u1:User)-[r:FOLLOWS]->(u2:User) " +
                          "WHERE u1.username = $user1 AND u2.username = $user2 " +
                          "DELETE r";

        try (Session session = getSession())
        {
            session.writeTransaction(tx -> {
                tx.run(remQuery, parameters( "user1", user1 ,  "user2",user2  ))
                        .consume();
                return 1;
            });
        }
    }

    /** Esegue una raccomandazione ad un utente di 5 ristoranti
     *
     *  Fornisce i ristoranti piu valutati dagli amici
     *  come nome, media dei voti e numero di voti*/
    public ListDTO<RestaurantDTO> getRecommendationRestaurant(String user, String zipcode)
    {
        ListDTO<RestaurantDTO> toReturn = new ListDTO<>();

        try (Session session = getSession())
        {
            // Match ristoranti che segue un follower
            String searchQuery = "MATCH (u1:User)-[:FOLLOWS]->(u2:User)-[rate:RATED]->(r:Restaurant) " +
//                              Dove io sono il soggetto e lo zipcode e' quello dato
                                "WHERE u1.username = $user and r.zipcode = $zipcode " +
//                              Dove io non ho dato un voto a quel ristorante
                                "AND NOT (u1)-[:RATED]->(r) "+
                                "WITH r, COUNT(rate.rating) as nrating, " +
                                "AVG(rate.rating) as avg_rating " +
                                "ORDER BY nrating DESC, avg_rating DESC " +
                                "RETURN r.rid as rid, r.name as name, avg_rating as avg " +
                                "LIMIT 5";

            session.writeTransaction(tx -> {
                Result result = tx.run(searchQuery, parameters( "user", user, "zipcode", zipcode));
                List<RestaurantDTO> toSet = new ArrayList<>();

                while (result.hasNext())
                {
                    Record r = result.next();
                    RestaurantDTO toAppend = new RestaurantDTO();

                    toAppend.setId(r.get("rid").asString());
                    toAppend.setName(r.get("name").asString());
                    toAppend.setRating(r.get("avg").asFloat());

                    toSet.add(toAppend);
                }

                toReturn.setList(toSet);
//                Metto 0 cosi non mi mostra il bottone pagina successiva
                toReturn.setItemCount(0);

                return 1;
            });
        }
        return toReturn;
    }

    /** Imposta un commento da parte di un utente verso un ristorante
     *
     *  Se un commento esiste gia, quello vecchio viene sovrascritto
     *
     *  INPUT - ReastaurantId, username utente, rating, review testuale */
    public void setRating(String user , String rid, double rate, String review)
    {
        try (Session session = getSession())
        {
            String searchQuery = "MATCH (u:User)-[rate:RATED]->(r:Restaurant) " +
                                 "WHERE u.username = $user AND r.rid = $rid " +
                                 "RETURN r";

            session.writeTransaction(tx -> {
//              Controllo se commento esiste gia
                Result result = tx.run(searchQuery, parameters("user", user, "rid", rid));

//              Se esiste già, aggiorna il rating
                if(result.hasNext())
                {
                    String modifyQuery = "MATCH (u:User)-[rate:RATED]->(r:Restaurant) " +
                                         "WHERE u.username = $user AND r.rid = $rid " +
                                         "SET rate.rating = $rating, rate.review = $review";

                    tx.run(modifyQuery, parameters("user", user, "rid", rid, "rating", rate, "review", review)).consume();

                }
                else // Se non esiste, aggiungo nuovo rating
                {
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
    public ListDTO<CommentDTO> getComments(String restaurantid, int page)
    {
        try (Session session = getSession())
        {
            String searchQuery = "MATCH (r:Restaurant)<-[rate:RATED]-(u:User) " +
                            "WHERE r.rid = $rid " +
                            "RETURN u.username as user, rate.rating as rate, rate.review as comment " +
                            "SKIP $skip " +
                            "LIMIT $limit";

            ListDTO<CommentDTO> toReturn = new ListDTO<CommentDTO>();

            session.writeTransaction(tx ->
            {
                Result result = tx.run(searchQuery, parameters( "rid", restaurantid, "skip", (page * Constants.MAX_COMMENTS), "limit", Constants.MAX_COMMENTS));
                List<CommentDTO> tempList = new ArrayList<CommentDTO>();
                System.out.println("NEO4J: " + result);
                while(result.hasNext())
                {
                    Record r = result.next();
                    CommentDTO tempComment = new CommentDTO();

                    tempComment.setUserName(r.get("user") != null ? r.get("user").asString() : "Anonymous");
                    tempComment.setRate(r.get("rate") != null ? r.get("rate").asDouble(): -1);
                    tempComment.setReview(!r.get("comment").isNull() ? r.get("comment").asString() : "No comment available");

                    if(tempComment.getRate() >= 0) {
                        tempList.add(tempComment);
                    }
                }

                toReturn.setList(tempList);
                toReturn.setItemCount(tempList.size());

                return 1;
            });

            return toReturn;
        }
    }
    public static ListDTO<UserDTO> getRecommendationFriendOfFriend(String user)
    {
        try (Session session = getSession())
        {
            ListDTO<UserDTO> toReturn = new ListDTO<UserDTO>();
            String searchQuery = "MATCH (u1:User{username : $user})-[:FOLLOWS]->(u2:User)-[:FOLLOWS]->(u3:User)<-[f:FOLLOWS]-(:User) " +
                    "WHERE NOT EXISTS {(u1)-[:FOLLOWS]->(u3)}"+
                    "WITH COUNT(f) as nfollowers , u3 " +
                    "ORDER BY nfollowers DESC " +
                    "RETURN nfollowers , u3.username as username " +
                    "LIMIT 10";

            session.writeTransaction(tx ->
            {
                Result result = tx.run(searchQuery, parameters( "user", user));

                List<UserDTO> tempList = new ArrayList<UserDTO>();

                while (result.hasNext()) {
                    Record record = result.next();
                    UserDTO tempUser = new UserDTO();
                    tempUser.setUsername(record.get("username").asString());
                    tempUser.setNfollowers(record.get("nfollowers").asInt());
                    tempList.add(tempUser);

                }

                toReturn.setList(tempList);
                toReturn.setItemCount(tempList.size());

                return 1;
            });

            return toReturn;
        }
    }



    public static ListDTO<UserDTO> getRecommendationUserRestaurant(String user) {
        try (Session session = getSession()) {
            ListDTO<UserDTO> toReturn = new ListDTO<UserDTO>();
            String searchQuery = "MATCH (u1:User{username : $user})-[:RATED]->(r:Restaurant)<-[rate:RATED]-(u2:User)<-[f:FOLLOWS]-(:User)" +
                    "WHERE NOT EXISTS {(u1)-[:FOLLOWS]->(u2)}"+
                    "WITH COUNT(DISTINCT f) as nfollowers , COUNT (DISTINCT rate) as nsamerest,u2 " +
                    "ORDER BY nsamerest , nfollowers DESC " +
                    "RETURN nfollowers , u2.username as username " +
                    "LIMIT 10";

            session.writeTransaction(tx -> {
                Result result = tx.run(searchQuery, parameters( "user", user));
                List<UserDTO> tempList = new ArrayList<UserDTO>();

                while (result.hasNext()) {
                    Record record = result.next();
                    UserDTO tempUser = new UserDTO();
                    tempUser.setUsername(record.get("username").asString());
                    tempUser.setNfollowers(record.get("nfollowers").asInt());

                    tempList.add(tempUser);

                }

                toReturn.setList(tempList);
                toReturn.setItemCount(tempList.size());

                return 1;

            });
            return toReturn;
        }
    }

    public ListDTO<UserDTO> getFollowers(String username, int page)
    {
        try (Session session = getSession())
        {
            String searchQuery = "MATCH (u1:User)-[:FOLLOWS]->(u2:User) " +
                                "WHERE u1.username = $username " +
                                "RETURN u2.username as user " +
                                "SKIP $skip " +
                                "LIMIT $limit";

            ListDTO<UserDTO> toReturn = new ListDTO<UserDTO>();

            session.writeTransaction(tx -> {
                Result result = tx.run(searchQuery, parameters( "username", username, "skip", (page * Constants.MAX_FOLLOWERS), "limit", Constants.MAX_FOLLOWERS));
                List<UserDTO> tempList = new ArrayList<UserDTO>();

                while(result.hasNext()){
                    Record r = result.next();

                    UserDTO tempUser = new UserDTO();
                    tempUser.setUsername(r.get("user") != null ? r.get("user").asString() : "Anonymous");
                    tempList.add(tempUser);
                }

                toReturn.setList(tempList);
                toReturn.setItemCount(tempList.size());

                return 1;
            });

            return toReturn;
        }
    }


    @Override
    public void close() throws RuntimeException{
        driver.close();
    }
}


