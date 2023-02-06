package it.unipi.lsmsd.neo4food.dao.neo4j;

import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.dto.UserDTO;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.util.ArrayList;
import java.util.List;

public class UtilityNeoDAO extends BaseNeo4J{
    ListDTO<UserDTO> toReturn = new ListDTO<UserDTO>();
    /** Ritorna i 20 ristoranti con piu ratings in tutto il mondo
     *
     *  Fornisce il nome, il numero di ratings e la media di tali ratings */
    public void getBestRestaurants() {
        try (Session session = getSession()) {
            String searchQuery = "MATCH (u1:User)-[rate:RATED]->(r:Restaurant) " +
                                 "WITH r.name as name," +
                                 "COUNT(rate.rating) as nrating," +
                                 "AVG(rate.rating) as avg_rating " +
                                 "ORDER BY nrating, avg_rating DESC " +
                                 "RETURN name, avg_rating, nrating " +
                                 "LIMIT 20";

            session.writeTransaction(tx -> {
                Result result = tx.run(searchQuery);
                System.out.println(result.list());

                return 1;
            });
        }
    }

    /** Ritorna i 20 utenti piu attivi in tutto il mondo
     *  in termini di ratings effettuati
     *
     *  Fornisce nome, media e numero di ratings fatti */
    public void getMostActiveUsers() {
        try (Session session = getSession()) {
            String query = "MATCH (u1:User)-[rate:RATED]->(r:Restaurant) " +
                           "WITH u1.username as username," +
                           "COUNT(rate.rating) as nrating," +
                           "AVG(rate.rating) as avg_rating " +
                           "ORDER BY nrating DESC " +
                           "RETURN username, avg_rating, nrating " +
                           "LIMIT 20";

            session.writeTransaction(tx -> {
                Result result = tx.run(query);
                System.out.println(result.list());

                return 1;
            });
        }
    }

    /** Ritorna i 20 utenti della community con piu amici
     *
     *  Fornisce username e numero totale di amici */
    public ListDTO<UserDTO> getInfluencers() {
        try (Session session = getSession()) {
            String query = "MATCH (u1:User)-[x:FOLLOWS]->(u2:User) " +
                           "WITH u1.username as username," +
                           "COUNT(x) as nfollowers " +
                           "ORDER BY nfollowers DESC " +
                           "RETURN username , nfollowers LIMIT 20";

            session.writeTransaction(tx -> {
                Result result = tx.run(query);


                List<UserDTO> tempList = new ArrayList<UserDTO>();

                while (result.hasNext()) {
                    Record record = result.next();
                    UserDTO tempUser = new UserDTO();
                    System.out.println(record);
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

    @Override
    public void close() throws RuntimeException{
        driver.close();
    }
}
