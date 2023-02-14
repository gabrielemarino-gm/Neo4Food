package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.MongoException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import it.unipi.lsmsd.neo4food.dto.DishDTO;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.dto.OrderDTO;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;
import static it.unipi.lsmsd.neo4food.utility.Utilities.unpackOneOrder;

public class OrderMongoDAO extends BaseMongo
{

    public void insertOrder(OrderDTO order)
    {
        // Devo aggiungere sia in Ordini che in Ristoranti
        MongoCollection<Document> collectionOrders = getDatabase().getCollection("Orders");
        MongoCollection<Document> collectionRestaurants = getDatabase().getCollection("Restaurants");

//          ----------------------------------------
//              PREPARAZIONE ORDINE PER ORDERS
//          ----------------------------------------
        List<Document> ordersDishes = new ArrayList<>();
        List<Document> restaurantsDishes = new ArrayList<>();

//        Creo la lista piatti per l'ordine
        for(DishDTO item: order.getDishes())
        {
//            Lista piatti per l'ordine nella collection Orders
            ordersDishes.add(new Document()
                    .append("name", item.getName())
                    .append("price", item.getPrice())
                    .append("currency", item.getCurrency().replace(" ",""))
                    .append("quantity", item.getQuantity()));

//            Lista piatti per l'ordine embedded nel Restaurant
            restaurantsDishes.add(new Document()
                    .append("name", item.getName())
                    .append("quantity", item.getQuantity()));
        }

        ObjectId orderId = new ObjectId();
//            Ordine da inserire nella collection Orders
        Document toInsertOrders = new Document("_id", orderId).
                append("user", order.getUser()).
                append("restaurant", order.getRestaurant()).
                append("restaurantId", order.getRestaurantId()).
                append("paymentMethod", order.getPaymentMethod()).
                append("paymentNumber", order.getPaymentNumber()).
                append("creationDate", order.getCreationDate()).
                append("deliveryDate", order.getDeliveryDate()).
                append("address", order.getAddress()).
                append("zipcode", order.getZipcode()).
                append("total", order.getTotal()).
                append("currency", order.getCurrency()).
                append("status", order.getStatus()).
                append("dishes", ordersDishes);

//            Ordine da inserire nella collection Orders
        Document toInsertRestaurants = new Document("_id", orderId).
                append("user", order.getUser()).
                append("paymentMethod", order.getPaymentMethod()).
                append("paymentNumber", order.getPaymentNumber()).
                append("creationDate", order.getCreationDate()).
                append("address", order.getAddress()).
                append("zipcode", order.getZipcode()).
                append("total", order.getTotal()).
                append("currency", order.getCurrency()).
                append("dishes", ordersDishes);

//          --------------------------------
//              CREO LA TRANSAZIONE ACID
//          --------------------------------
        ClientSession session = getSession();
        try {
            session.startTransaction();

            collectionOrders.insertOne(session, toInsertOrders);

            collectionRestaurants.updateOne(session,
                    eq("_id", new ObjectId(order.getRestaurantId())),
                    Updates.addToSet("orders", toInsertRestaurants)
            );

            session.commitTransaction();
        } catch (Exception e) {
            session.abortTransaction();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

    public boolean sendOrder(String orderid, String restaurantid){
//        Aggiorno ordine globale
            MongoCollection<Document> collectionOrders = getDatabase().getCollection("Orders");
            MongoCollection<Document> collectionRestaurants = getDatabase().getCollection("Restaurants");

            Document queryOrders = new Document()
                        .append("_id", new ObjectId(orderid));
            Bson updateOrders = Updates.combine(
                                Updates.set("deliveryDate", new Date()),
                                Updates.set("status", true)
                            );

            Document queryRestaurants = new Document()
                        .append("_id", new ObjectId(restaurantid));
            Bson updateRestaurants = Updates.combine(
                                Updates.pull("orders",
                                        new Document("_id", new ObjectId(orderid)))
                            );

            boolean result = true;
            ClientSession session = getSession();
            try {
                session.startTransaction();

//                Aggiorno lo stato dell'ordine nella collection Orders
                collectionOrders.updateOne(session, queryOrders, updateOrders);
//                Rimuovo l'ordine dalla collection Restaurants
                collectionRestaurants.updateOne(session, queryRestaurants, updateRestaurants);

                session.commitTransaction();
            } catch (Exception e) {
                session.abortTransaction();
                e.printStackTrace();
                result = false;
            } finally {
                session.close();
            }

        return result;
    }
//---------------------
    public ListDTO<OrderDTO> getOrders(String actorid, boolean isRestaurant)
    {
        ListDTO<OrderDTO> toReturn = new ListDTO<OrderDTO>();
        MongoCollection<Document> collection = getDatabase().getCollection("Orders");
        Bson query = new Document();

        if(isRestaurant)
        {
            query = Filters.eq("restaurantId", actorid);
        }
        else
        {
            query = Filters.eq("user", actorid);
        }

        try(MongoCursor cursor = collection.find(query).sort(descending("creationDate")).limit(20).iterator())
        {
            if(!cursor.hasNext()){
                return toReturn;
            }

            List<OrderDTO> tempOrderList = new ArrayList<OrderDTO>();
            int count = 0;
            while (cursor.hasNext())
            {
                Document res = (Document) cursor.next();

                OrderDTO tempOrder = new OrderDTO();

                unpackOneOrder(res, tempOrder);

                tempOrderList.add(tempOrder);
                count++;
            }

            toReturn.setList(tempOrderList);
            toReturn.setItemCount(count);
        }
        catch(MongoException e)
        {
            System.err.println(e);
        }

        return toReturn;
    }
}
