package it.unipi.lsmsd.neo4food.dao.mongo;

import com.mongodb.client.*;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.ConnectionString;
import com.sun.corba.se.impl.orbutil.closure.Constant;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.dto.RestaurantDTO;
import it.unipi.lsmsd.neo4food.constants.Constants;
import org.bson.Document;

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

        try(MongoCursor cursor = collection.find(eq("zipcode", zipcode)).limit(Constants.PAGE_SIZE).skip(offset).iterator();
        ){
            while (cursor.hasNext()){
                Document res = (Document)cursor.next();
                String id = res.get("_id").toString();
                String name = res.get("name").toString();
                int rating = Integer.parseInt(res.get("rating").toString());

                RestaurantDTO e = new RestaurantDTO(id,name,rating);
                supportList.add(e);
                count++;
            }
        }
//------------------------
        returnList.setList(supportList);
        returnList.setItemCount(count);
        return returnList;
    }
}