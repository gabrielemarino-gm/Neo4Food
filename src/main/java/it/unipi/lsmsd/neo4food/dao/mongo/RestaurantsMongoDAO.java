package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.client.*;

import com.mongodb.ConnectionString;
import com.mongodb.client.model.Filters;
import com.sun.corba.se.impl.orbutil.closure.Constant;
import com.sun.org.apache.xpath.internal.operations.Or;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.dto.OrderDTO;
import it.unipi.lsmsd.neo4food.dto.RestaurantDTO;
import it.unipi.lsmsd.neo4food.dto.DishDTO;
import it.unipi.lsmsd.neo4food.constants.Constants;
import jdk.nashorn.internal.runtime.ListAdapter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class  RestaurantsMongoDAO extends BaseMongo
{

    public ListDTO<RestaurantDTO> getRestaurants(int page, String zipcode, String filter)
    {
        ListDTO<RestaurantDTO> toReturn = new ListDTO<RestaurantDTO>();
        List<RestaurantDTO> tempList = new ArrayList<RestaurantDTO>();

        int count = 0;
//------------------------
        int offset = page * Constants.PAGE_SIZE;
        MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");
        Bson query = new Document();
        if(filter.equals("") || filter == null){
//          Query senza category
            query = Filters.eq("zip_code", zipcode);
        }else{
//          Query con category
            query = Filters.and(
                    Filters.eq("zip_code", zipcode),
                    Filters.regex("category",".*"+filter+".*"));
        }

        try(MongoCursor cursor = collection.find(query).limit(Constants.PAGE_SIZE).skip(offset).iterator();)
        {
            while (cursor.hasNext()){
                Document res = (Document) cursor.next();

                String id = res.get("_id").toString();
                String name = res.get("name").toString();
                String range = res.get("price_range") != null ? res.get("price_range").toString() : "Price not available";
                Float rating = res.get("score") != null ? Float.parseFloat(res.get("score").toString()) : 0;

                RestaurantDTO e = new RestaurantDTO();
                e.setId(id);
                e.setName(name);
                e.setPriceRange(range);
                e.setRating(rating);

                tempList.add(e);
                count++;
            }
        }
//------------------------
        toReturn.setList(tempList);
        toReturn.setItemCount(count);
        return toReturn;
    }
public RestaurantDTO getRestaurantOwner(String eml, String password){
    MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");
    try(MongoCursor cursor = collection.find(
            and(
                    eq("email", eml),
                    eq("password",password)
            )
        ).limit(1).iterator();
    )
//            INIZIO BLOCCO TRY
    {

        if (cursor.hasNext()) {

        Document res = (Document) cursor.next();
//            One restaurantOwner found!
        String id = res.get("_id").toString();
        String name = res.get("name").toString();
        Float rating = res.get("rating") != null ? Float.parseFloat(res.get("rating").toString()): 0;
        String address = res.get("full_address").toString();
        String email = res.get("email").toString();

        RestaurantDTO e = new RestaurantDTO();
        e.setId(id);
        e.setName(name);
        e.setRating(rating);
        e.setEmail(email);
        e.setAddress(address);

        return e;
        }
    }
    RestaurantDTO e = new RestaurantDTO();
    e.setId("0");

    return e;
}

//  Questa query serve per prendere tutti i dettagli del ristorante per restaurant ID (rid)
    public RestaurantDTO getRestaurantDetails(String rid)
    {
//      Variabile che devo restituire
        RestaurantDTO toReturn = new RestaurantDTO();

//      Lista di DishDTO da inserire in toReturn 
        List<DishDTO> tempDishesList = new ArrayList<DishDTO>();
//      Ordini Pending da inserire in toReturn
        List<OrderDTO> tempOrdersList = new ArrayList<OrderDTO>();

        MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");

        try(MongoCursor cursor = collection.find(eq("_id", new ObjectId(rid))).limit(1).iterator();)
        {
//          Per ogni documento trovato su MongoDB:
            while (cursor.hasNext())
            {
//              Itero il risultato
                Document res = (Document) cursor.next();
                String id = res.get("_id") != null ? res.get("_id").toString() : "ID not available";
                String name = res.get("name") != null ? res.get("name").toString() : "Name not available" ;
                String range = res.get("price_range") != null ? res.get("price_range").toString() : "Price not available" ;
                String address = res.get("full_address") != null ? res.get("full_address").toString() : "Address not available" ;
                Float rating = res.get("score") != null ? Float.parseFloat(res.get("score").toString()) : 0;

                toReturn.setId(id);
                toReturn.setName(name);
                toReturn.setAddress(address);
                toReturn.setPriceRange(range);
                toReturn.setRating(rating);

//              Attributo dish del documento
                ArrayList<Document> dishDocuments = res.get("dishes") != null ? (ArrayList<Document>) res.get("dishes") : null;

//              Per ogni piatto nel documento, genera un DishDTO
                if(dishDocuments != null)
                {

                    for (Document i : dishDocuments)
                    {
                        DishDTO tempDish = new DishDTO();

                        tempDish.setId(i.get("_id").toString());
                        tempDish.setName(i.get("name").toString());
                        tempDish.setDescription(i.get("description") != null ? i.get("description").toString() : "No description available");
                        tempDish.setPrice(Double.parseDouble(i.get("price").toString().replaceAll("[^0-9.]", "")));
                        tempDish.setCurrency(i.get("currency").toString());

                        tempDishesList.add(tempDish);
                    }
                }

//              Devo creare la lista di ordini
                ArrayList<Document> orderDocuments = res.get("orders") != null ? (ArrayList<Document>) res.get("orders") : null;
                if(orderDocuments != null)
                {
                    for (Document i : orderDocuments)
                    {
//                      Per ogni documento, creo un ordine temporaneo, da aggiungere alla lista di ordini del ristorante
                        OrderDTO tempOrder = new OrderDTO();

                        tempOrder.setId(i.get("_id").toString());
                        tempOrder.setUser(i.get("user").toString());
                        if(i.get("restaurant") != null)
                        {
                            tempOrder.setRestaurant(i.get("restaurant").toString());
                            tempOrder.setRestaurantId(i.get("restaurantId").toString());
                        }
                        tempOrder.setPaymentMethod(i.get("paymentMethod").toString());
                        tempOrder.setPaymentNumber(i.get("paymentNumber").toString());
                        tempOrder.setAddress(i.get("address").toString());
                        tempOrder.setZipcode(i.get("zipcode").toString());
                        if(i.get("status") != null){
                            if(Boolean.parseBoolean(i.get("status").toString())) {
                                tempOrder.setSent();
                            }
                        }
                        tempOrder.setTotal(Double.parseDouble(i.get("total").toString()));

//                      Ogni ordine puo contenere una lista di piatti
                        List<DishDTO> tempDishSublist = new ArrayList<DishDTO>();
                        List<Document> dishOrderDocuments = i.get("dishes") != null ? (ArrayList<Document>) i.get("dishes") : null;

//                      Per ogni ordine ci sono DishDTO
                        if(dishOrderDocuments != null) {
                            System.out.println(dishOrderDocuments);
                            for (Document j : dishOrderDocuments) {

                                DishDTO tempSubdish = new DishDTO();

                                tempSubdish.setId(j.get("_id").toString());
                                tempSubdish.setName(j.get("name").toString());
                                if(j.get("price") != null){
                                    tempSubdish.setPrice(Double.parseDouble(j.get("price").toString()));
                                    tempSubdish.setCurrency(j.get("currency").toString());
                                }
                                tempSubdish.setQuantity(Integer.parseInt(j.get("quantity").toString()));

                                tempDishSublist.add(tempSubdish);
                            }
                        tempOrder.setDishes(tempDishSublist);
                        }

                        tempOrdersList.add(tempOrder);
                    }
                }
            }
        }

        toReturn.setDishes(tempDishesList);
        toReturn.setPendingOrders(tempOrdersList);

        return toReturn;
    }
}