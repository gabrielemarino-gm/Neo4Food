package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.client.*;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.ConnectionString;
import com.sun.corba.se.impl.orbutil.closure.Constant;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.dto.RestaurantDTO;
import it.unipi.lsmsd.neo4food.constants.Constants;
import it.unipi.lsmsd.neo4food.model.Dish;
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
                Float rating = res.get("score") != null ? Float.parseFloat(res.get("score").toString()) : 0;

                RestaurantDTO e = new RestaurantDTO(id,name,rating,null);
                supportList.add(e);
                count++;
            }
        }
//------------------------
        returnList.setList(supportList);
        returnList.setItemCount(count);
        return returnList;
    }

    public RestaurantDTO getRestaurantDetails(String rid){
        RestaurantDTO ret = null;
        ListDTO<Dish> list = new ListDTO<Dish>();

        List<Dish> supp = new ArrayList<Dish>();
        int count;
        MongoCollection<Document> collection = getDatabase().getCollection("Restaurants");
        try(MongoCursor cursor = collection.find(eq("_id", new ObjectId(rid))).iterator();
        ){
            while (cursor.hasNext()){
                Document res = (Document) cursor.next();
                String id = res.get("_id").toString();
                String name = res.get("name").toString();
                Float rating = res.get("score") != null ? Float.parseFloat(res.get("score").toString()) : 0;
                ArrayList<Document> items = (ArrayList<Document>) res.get("dish");
                count = 0;
                for(Document i:items){
                    supp.add(new Dish(
                            i.get("_id").toString(),
                            i.get("name").toString(),
                            Float.parseFloat(i.get("price").toString().replaceAll("[^0-9.]","")),
                            i.get("price").toString().replaceAll("[0-9.]",""),
                            i.get("description") != null ? i.get("description").toString() : "No description available",
                            id
                    ));
                    count++;
                }
                list.setItemCount(count);
                list.setList(supp);
                ret = new RestaurantDTO(id,name,rating,list);
            }
        }
        return ret;
    }
}