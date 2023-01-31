package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import it.unipi.lsmsd.neo4food.dto.DishDTO;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.dto.OrderDTO;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import static it.unipi.lsmsd.neo4food.utility.Utilities.unpackOneOrder;

public class OrderMongoDAO extends BaseMongo
{

    public void insertOrder(OrderDTO order)
    {
        // Devo aggiungere sia in Ordini che in Ristoranti
        MongoCollection<Document> collectionOrders = getDatabase().getCollection("Orders");
        MongoCollection<Document> collectionRestaurants = getDatabase().getCollection("Restaurants");

        // Devo aggiungere un ordine
        // Un ordine ha dei campi e una lista di piatti
        // Inizio creando una lista di documenti di tipo piatto
        // Aggiungo i campi importanti per ogni piatto nell'ordine
        List<Document> dishesList = new ArrayList<>();

        for(DishDTO item: order.getDishes())
        {
            dishesList.add(new Document()
                    .append("name", item.getName())
                    .append("price", item.getPrice())
                    .append("currency", item.getCurrency().replace(" ",""))
                    .append("quantity", item.getQuantity()));

        }

        // A questo punto creo il documento dell ordine
        Document toInsert = new Document("_id", new ObjectId()).
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
                append("dishes", dishesList);

//        Nella collection Orders sono inseriti con i dati completi
        try
        {
            collectionOrders.insertOne(toInsert);
        }
        catch(MongoException e)
        {
            System.err.println(e);
        }

//        L'ordine da inserire sotto il ristorante ha alcuni campi in meno
//        I piatti non devono avere price e currency
//        L ordine non deve avere restaurant, restaurantId e status
        for(Document i : dishesList)
        {
            i.remove("price");
            i.remove("currency");
        }

        toInsert.remove("restaurant");
        toInsert.remove("restaurantId");
        toInsert.remove("deliveryDate");
        toInsert.remove("status");
        toInsert.remove("dishes");
//      Nella collection del ristorante vengo inseriti solo con i dati necessari al ristoratore
        toInsert.append("dishes", dishesList);

        try
        {
            collectionRestaurants.updateOne(
                    eq("_id", new ObjectId(order.getRestaurantId())),
                    Updates.addToSet("orders", toInsert)
            );
        }
        catch(MongoException e)
        {
            System.err.println(e);
        }

    }

    public boolean sendOrder(String orderid, String restaurantid){
//        Aggiorno ordine globale
            MongoCollection<Document> collection = getDatabase().getCollection("Orders");
            UpdateResult result;

            Document query = new Document().append("_id", new ObjectId(orderid));
            Bson update = Updates.combine(
                                Updates.set("deliveryDate", new Date())
                            );

            try {
                result = collection.updateOne(query, update);
                if (result.getModifiedCount() != 1){
//                    ROLLBACK


                    return false;
                }
            }catch (MongoException e){
                System.err.println(e);
            }
//------------------------------------------------------------------------
//        Rimuovo ordine dal ristorante
            MongoCollection<Document> collection1 = getDatabase().getCollection("Restaurants");
            UpdateResult result1;

            Document query1 = new Document()
                        .append("_id", new ObjectId(restaurantid));

            Bson update1 = Updates.combine(
                                Updates.pull("orders", new Document("_id", new ObjectId(orderid)))
                            );

            try {
                result1 = collection1.updateOne(query1, update1);
                if (result1.getModifiedCount() != 1){
//                    ROLLBACK

                    return false;
                }
            }catch (MongoException e){
                System.err.println(e);
            }

        return true;
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

        try(MongoCursor cursor = collection.find(query).sort(ascending("creationDate")).limit(20).iterator())
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
