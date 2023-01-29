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
//--------------------------------------------------------------
//---USED BY USER TO SEARCH RESTAURANTS BY ZIPCODE AND FILTER---
//--------------------------------------------------------------
    public ListDTO<RestaurantDTO> getRestaurantsForSearchPage(int page, String zipcode, String filter)
    {
        ListDTO<RestaurantDTO> toReturn = new ListDTO<RestaurantDTO>();
        List<RestaurantDTO> tempList = new ArrayList<RestaurantDTO>();

        int count = 0;
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
        toReturn.setList(tempList);
        toReturn.setItemCount(count);
        return toReturn;
    }
//------------------------------------------------------------------
//---USED BY RESTAURANT TO CHECK CORRECTNESS OF LOGIN CREDENTIALS---
//------------------------------------------------------------------
public RestaurantDTO getRestaurantLogin(String eml, String password){
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

        String id = res.get("_id").toString();
        String name = res.get("name") != null ? res.get("name").toString() : "Name not available";
        Float rating = res.get("rating") != null ? Float.parseFloat(res.get("rating").toString()): 0;
        String address = res.get("full_address") != null ? res.get("full_address").toString() : "Address not available";
        String zipcode = res.get("zip_code") != null ? res.get("zip_code").toString() : "Zipcode not available";
        String email = res.get("email") != null? res.get("email").toString() : "Email not available";

        RestaurantDTO e = new RestaurantDTO();
        e.setId(id);
        e.setName(name);
        e.setRating(rating);
        e.setEmail(email);
        e.setAddress(address);
        e.setZipcode(zipcode);

        return e;
        }
    }
    RestaurantDTO e = new RestaurantDTO();
    e.setId("0");
    return e;
}
//-------------------------------------------------------------
//---USED BY RESTAURANT AND USER TO GET ONLY SELECTED FIELDS---
//-------------------------------------------------------------
//CASE 1 - Any user need to get only list of dishes without orders
//CASE 2 - Restaurant itself wants to get only list of dishes without orders
//CASE 3 - Restaurant itself wants to get only list of orders without dishes
//CASE 4 -
public RestaurantDTO getRestaurantDetails(String rid, boolean getMoreDetails, boolean getDishes, boolean getOrders)
    {
//      Variabile che devo restituire
        RestaurantDTO toReturn = new RestaurantDTO();

        MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");

        try(MongoCursor cursor = collection.find(eq("_id", new ObjectId(rid))).limit(1).iterator();)
        {
//          Per ogni documento trovato su MongoDB:
            while (cursor.hasNext())
            {
//              Itero il risultato
                Document res = (Document) cursor.next();

                toReturn.setId(res.get("_id") != null ? res.get("_id").toString() : "ID not available");
                toReturn.setName(res.get("name") != null ? res.get("name").toString() : "Name not available");
                toReturn.setEmail(res.get("email") != null ? res.get("email").toString() : "Email not available");
                toReturn.setPriceRange(res.get("price_range") != null ? res.get("price_range").toString() : "Price not available");
                toReturn.setAddress(res.get("full_address") != null ? res.get("full_address").toString() : "Address not available");
                toReturn.setRating(res.get("score") != null ? Float.parseFloat(res.get("score").toString()) : 0);

//              prendo liste documenti dishes and orders
                ArrayList<Document> dishDocuments = res.get("dishes") != null ? (ArrayList<Document>) res.get("dishes") : null;
                ArrayList<Document> orderDocuments = res.get("orders") != null ? (ArrayList<Document>) res.get("orders") : null;

                List<DishDTO> tempDishesList = new ArrayList<DishDTO>();
                List<OrderDTO> tempOrdersList = new ArrayList<OrderDTO>();

                if(dishDocuments != null && getDishes)
                {
                    unpackDishes(dishDocuments, tempDishesList);
                }

                if(orderDocuments != null && getOrders)
                {
                    unpackOrders(orderDocuments, tempOrdersList);
                }

                toReturn.setDishes(tempDishesList);
                toReturn.setPendingOrders(tempOrdersList);
            }
        }

        return toReturn;

    }

//    _______________________
    private void unpackDishes(List<Document> documents, List<DishDTO> dishes){

        for (Document i : documents)
        {
            DishDTO tempDish = new DishDTO();

            unpackOneDish(i, tempDish);

            dishes.add(tempDish);
        }
    }
//    ________________________
    private void unpackOneDish(Document document, DishDTO dish){
        dish.setId(document.get("_id") != null ? document.get("_id").toString() : "ID not available");
        dish.setName(document.get("name") != null ? document.get("name").toString() : "Name not available");
        dish.setQuantity(document.get("quantity") != null ? Integer.parseInt(document.get("quantity").toString()) : 0);
        dish.setDescription(document.get("description") != null ? document.get("description").toString() : "Description not available");
        if(document.get("price") != null){
            dish.setPrice(Double.parseDouble(document.get("price").toString()));
            dish.setCurrency(document.get("currency").toString());
        }
    }
//    ________________________
//    ________________________
    private void unpackOrders(List<Document> document, List<OrderDTO> orders){

        for (Document i : document)
        {
            OrderDTO tempOrder = new OrderDTO();

            unpackOneOrder(i, tempOrder);

            orders.add(tempOrder);
        }
    }
//    _______________________
    private void unpackOneOrder(Document document, OrderDTO order){
        order.setId(document.get("_id") != null ? document.get("_id").toString() : "ID not available");
        order.setUser(document.get("user") != null ? document.get("user").toString(): "User not available");
        order.setRestaurant(document.get("restaurant") != null ? document.get("restaurant").toString() : "Restaurant not available");
        order.setCreationDate(document.getDate("creationDate") != null ? document.getDate("creationDate") : null);
        order.setDeliveryDate(document.getDate("deliveryDate") != null ? document.getDate("creationDate") : null);
        order.setRestaurantId(document.get("restaurantId") != null ? document.get("restaurantId").toString() : "RestaurantID not available");
        order.setPaymentMethod(document.get("paymentMethod") != null ? document.get("paymentMethod").toString() : "Payment method not available");
        order.setPaymentNumber(document.get("paymentNumber") != null ? document.get("paymentNumber").toString() : "Payment number not available");
        order.setAddress(document.get("address") != null ? document.get("address").toString() : "Address not available");
        order.setZipcode(document.get("zipcode") != null ? document.get("zipcode").toString() : "Zipcode not available");
        if(document.get("status") != null){
            if(Boolean.parseBoolean(document.get("status").toString())) {
                order.setSent();
            }
        }
        order.setTotal(document.get("total") != null ? Double.parseDouble(document.get("total").toString()) : null);

        List<Document> dishDocuments = document.get("dishes") != null ? (ArrayList<Document>) document.get("dishes") : null;
        List<DishDTO> tempDishes = new ArrayList<DishDTO>();
        if(dishDocuments != null) {
            unpackDishes(dishDocuments, tempDishes);
        }
        order.setDishes(tempDishes);
    }


}