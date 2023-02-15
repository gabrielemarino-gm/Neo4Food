package it.unipi.lsmsd.neo4food.dao.neo4j;

import it.unipi.lsmsd.neo4food.dto.UserDTO;
import it.unipi.lsmsd.neo4food.service.ServiceProvider;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;
import org.bson.Document;

public class SupportNeoDAO extends BaseNeo4J{
    /** Aggiunge un nodo utente al database
     *
     * INPUT - Nome utente */

    public void addUser(String username)
    {
        try (Session session = driver.session())
        {
            String addUser = "MERGE (u:User {username: $username})";

            session.writeTransaction(tx -> {
                tx.run(addUser, parameters("username", username))
                        .consume();
                return 1;
            });
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }

    /** Aggiunge un nodo ristorante al database
     *
     * INPUT - Id ristorante, nome, zipcode */
    public void addRestaurant(String rid, String name, String zipcode)
    {
        try (Session session = driver.session())
        {
            String addRestaurant = "MERGE (r:Restaurant {rid: $rid, name: $name, zipcode: $zipcode})";

            session.writeTransaction(tx -> {
                tx.run(addRestaurant, parameters("rid", rid, "name", name, "zipcode", zipcode))
                        .consume();
                return 1;
            });
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }

    public List<Document> getAvgRating()
    {
        List<Document> toReturn = new ArrayList<Document>();

        try(Session session = getSession())
        {
            String aggQuery = "MATCH (r:Restaurant)<-[a:RATED]-(:User) " +
                              "WITH r, avg(a.rating) as score " +
                              "RETURN r.rid as rid, score as avgScore";

            session.readTransaction(tx -> {
                Result result = tx.run(aggQuery);

                while(result.hasNext()){
                    Record r = result.next();

                    toReturn.add(new Document("rid", r.get("rid").asString()).append("val", r.get("avgScore").asDouble()));
                }
                return 1;
            });
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }

        return toReturn;
    }

    @Override
    public void close() throws RuntimeException{
        driver.close();
    }
}
