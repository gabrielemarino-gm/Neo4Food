package it.unipi.lsmsd.neo4food.dao.neo4j;

import org.neo4j.driver.Session;

import static org.neo4j.driver.Values.parameters;

public class SupportNeoDAO extends BaseNeo4J{
    /** Aggiunge un nodo utente al database
     *
     * INPUT - Nome utente */

    private void addUserIfNotExists(String username) {
        try (Session session = driver.session()) {
            String addUser = "MERGE (u:User {username: $username})";

            session.writeTransaction(tx -> {
                tx.run(addUser, parameters("username", username))
                        .consume();
                return 1;
            });
        }
    }

    /** Aggiunge un nodo ristorante al database
     *
     * INPUT - Id ristorante, nome, zipcode */
    private void addRestaurantIfNotExists(String rid, String name, String zipcode) {
        try (Session session = driver.session()) {
            String addRestaurant = "MERGE (r:Restaurant {rid: $rid, name: $name, zipcode: $zipcode})";

            session.writeTransaction(tx -> {
                tx.run(addRestaurant, parameters("rid", rid, "name", name , "zipcode" , zipcode))
                        .consume();
                return 1;
            });
        }
    }

    @Override
    public void close() throws RuntimeException{
        driver.close();
    }
}
