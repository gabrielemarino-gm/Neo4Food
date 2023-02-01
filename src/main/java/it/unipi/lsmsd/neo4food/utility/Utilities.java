package it.unipi.lsmsd.neo4food.utility;

import it.unipi.lsmsd.neo4food.dto.DishDTO;
import it.unipi.lsmsd.neo4food.dto.OrderDTO;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Utilities {
    //    _______________________
    public static void unpackDishes(List<Document> documents, List<DishDTO> dishes){
        /** INPUT:
         *      1- A list of dish documents
         *      2- A list of DishDTO
         *  OUTPUT:
         *       Fill the given list with dishes*/
        for (Document i : documents)
        {
            DishDTO tempDish = new DishDTO();

            unpackOneDish(i, tempDish);

            dishes.add(tempDish);
        }
    }
    //    ________________________
    public static void unpackOneDish(Document document, DishDTO dish){
        /** INPUT:
         *      1- A dish document
         *      2- A DishDTO object
         *  OUTPUT:
         *       Fill the given object with one dish*/
        dish.setId(document.get("_id") != null ? document.get("_id").toString() : "ID not available");
        dish.setName(document.get("name") != null ? document.get("name").toString() : "Name not available");
        dish.setQuantity(document.get("quantity") != null ? Integer.parseInt(document.get("quantity").toString()) : 0);
        dish.setDescription(document.get("description") != null ? document.get("description").toString() : "Description not available");
        if(document.get("price") != null){
            dish.setPrice(Double.parseDouble(document.get("price") != null? document.get("price").toString() : "0.0"));
            dish.setCurrency(document.get("currency") != null ? document.get("currency").toString() : "");
        }
    }
    //    ________________________
//    ________________________
    public static void unpackOrders(List<Document> document, List<OrderDTO> orders){
        /** INPUT:
         *      1- A list of order documents
         *      2- A list of OrderDTO
         *  OUTPUT:
         *       Fill the given list with orders*/
        for (Document i : document)
        {
            OrderDTO tempOrder = new OrderDTO();

            unpackOneOrder(i, tempOrder);

            orders.add(tempOrder);
        }
    }
    //    _______________________
    public static void unpackOneOrder(Document document, OrderDTO order){
        /** INPUT:
         *      1- An order document
         *      2- An OrderDTO object
         *  OUTPUT:
         *       Fill the given object with one order*/
        order.setId(document.get("_id") != null ? document.get("_id").toString() : "ID not available");
        order.setUser(document.get("user") != null ? document.get("user").toString(): "User not available");
        order.setRestaurant(document.get("restaurant") != null ? document.get("restaurant").toString() : "Restaurant not available");
        order.setCreationDate(document.getDate("creationDate") != null ? document.getDate("creationDate") : null);
        order.setDeliveryDate(document.getDate("deliveryDate") != null ? document.getDate("deliveryDate") : null);
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
        order.setCurrency(document.get("currency") != null ? document.get("currency").toString() : "Currency not available");

        List<Document> dishDocuments = document.get("dishes") != null ? (ArrayList<Document>) document.get("dishes") : null;
        List<DishDTO> tempDishes = new ArrayList<DishDTO>();
        if(dishDocuments != null) {
            unpackDishes(dishDocuments, tempDishes);
        }
        order.setDishes(tempDishes);
    }
}
