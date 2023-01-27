package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.client.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.ConnectionString;
import com.sun.corba.se.impl.orbutil.closure.Constant;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.dto.OrderDTO;
import it.unipi.lsmsd.neo4food.dto.RestaurantDTO;
import it.unipi.lsmsd.neo4food.dto.DishDTO;
import it.unipi.lsmsd.neo4food.constants.Constants;
import jdk.nashorn.internal.runtime.ListAdapter;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsMongoDAO extends BaseMongo{

    public ListDTO<RestaurantDTO> getRestaurants(int page, String zipcode){
        ListDTO<RestaurantDTO> returnList = new ListDTO<RestaurantDTO>();
        List<RestaurantDTO> supportList = new ArrayList<RestaurantDTO>();

        int count = 0;
//------------------------
        int offset = page * Constants.PAGE_SIZE;
        MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");
        try(MongoCursor cursor = collection.find(eq("zip_code", zipcode)).limit(Constants.PAGE_SIZE).skip(offset).iterator();
        ){
            while (cursor.hasNext()){
                Document res = (Document)cursor.next();
                String id = res.get("_id").toString();
                String name = res.get("name").toString();
                String range = res.get("price_range") != null ? res.get("price_range").toString() : "Price not available";
                Float rating = res.get("score") != null ? Float.parseFloat(res.get("score").toString()) : 0;

                RestaurantDTO e = new RestaurantDTO(id,name,range,rating,null,null);
                supportList.add(e);
                count++;
            }
        }
//------------------------
        returnList.setList(supportList);
        returnList.setItemCount(count);
        return returnList;
    }
public RestaurantDTO getRestaurantOwner(String e, String password){
    MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");
    try(MongoCursor cursor = collection.find(and(eq("email", e),eq("password",password))).limit(1).iterator();)
    {

        if (cursor.hasNext()) {

        Document res = (Document) cursor.next();
//            One restaurantOwner found!
        String id = res.get("_id").toString();
        String name = res.get("name").toString();
        String address = res.get("full_address").toString();
        String email = res.get("email").toString();

        return new RestaurantDTO(id, name,email, address );
    }

    }
return new RestaurantDTO("0","","","");
}


    public RestaurantDTO getRestaurantDetails(String rid)
    {
        RestaurantDTO ret = null;
        // Piatti
        ListDTO<DishDTO> listD = new ListDTO<DishDTO>();
        List<DishDTO> suppD = new ArrayList<DishDTO>();

        // Ordini Pending
        ListDTO<OrderDTO> listO = new ListDTO<OrderDTO>();
        List<OrderDTO> suppO = new ArrayList<OrderDTO>();

        int count;
        MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");


        try(MongoCursor cursor = collection.find(eq("_id", new ObjectId(rid))).iterator();)
        {
            while (cursor.hasNext())
            {
                Document res = (Document) cursor.next();
                String id = res.get("_id").toString();
                String name = res.get("name").toString();
                String range = res.get("price_range") != null ? res.get("price_range").toString() : "Price not available" ;
                Float rating = res.get("score") != null ? Float.parseFloat(res.get("score").toString()) : 0;

                // PIATTI
                ArrayList<Document> itemsDishs = (ArrayList<Document>) res.get("dish");
                count = 0;
                for(Document i:itemsDishs)
                {
                    suppD.add(new DishDTO(
                            i.get("_id").toString(),
                            i.get("name").toString(),
                            Float.parseFloat(i.get("price").toString().replaceAll("[^0-9.]","")),
                            i.get("price").toString().replaceAll("[0-9.]",""),
                            i.get("description") != null ? i.get("description").toString() : "No description available",
                            id
                    ));
                    count++;
                }
                listD.setItemCount(count);
                listD.setList(suppD);



                // ORDINI PENDING
                ArrayList<Document> itemsOrder = (ArrayList<Document>) res.get("orders");

                ret = new RestaurantDTO(id,name,range,rating,listD,listO);
            }
        }
        return ret;
    }
}