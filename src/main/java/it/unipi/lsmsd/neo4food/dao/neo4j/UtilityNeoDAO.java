package it.unipi.lsmsd.neo4food.dao.neo4j;

import it.unipi.lsmsd.neo4food.dto.AnalyticsDTO;
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

                return 1;
            });
        }
    }

    /** Ritorna i 20 utenti piu attivi in tutto il mondo
     *  in termini di ratings effettuati
     *
     *  Fornisce nome, media e numero di ratings fatti */
    public ListDTO<AnalyticsDTO> getMostActiveUsers() {
        ListDTO<AnalyticsDTO> toReturn = new ListDTO<AnalyticsDTO>();

        try (Session session = getSession()) {
            String query = "MATCH (u1:User)-[rate:RATED]->(r:Restaurant) " +
                           "WITH u1.username as username," +
                           "COUNT(rate.rating) as nrating," +
                           "AVG(rate.rating) as avg_rating " +
                           "ORDER BY nrating DESC " +
                           "RETURN username, avg_rating as avg, nrating " +
                           "LIMIT 10";

            session.writeTransaction(tx -> {
                Result result = tx.run(query);

                List<AnalyticsDTO> toSet = new ArrayList<AnalyticsDTO>();
                while (result.hasNext()){
                    Record r = result.next();
                    AnalyticsDTO toAppend = new AnalyticsDTO();

                    toAppend.setUser(r.get("username").asString());
                    toAppend.setDouble(r.get("avg").asDouble());
                    toAppend.setCount(r.get("nrating").asInt());

                    toSet.add(toAppend);
                }
                toReturn.setList(toSet);
                toReturn.setItemCount(toSet.size());

                return 1;
            });
        }
        return toReturn;
    }

    /** Ritorna i 20 utenti della community con piu amici
     *
     *  Fornisce username e numero totale di amici */
    public ListDTO<UserDTO> getInfluencers() {
        try (Session session = getSession()) {
            String query = "MATCH (u1:User)<-[x:FOLLOWS]-(:User) " +
                    "RETURN u1.username as username, COUNT(x) as nfollowers " +
                    "ORDER BY nfollowers DESC " +
                    "LIMIT 20";

            session.writeTransaction(tx -> {
                Result result = tx.run(query);


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

    @Override
    public void close() throws RuntimeException{
        driver.close();
    }
}
