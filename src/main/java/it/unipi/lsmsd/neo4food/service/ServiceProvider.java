package it.unipi.lsmsd.neo4food.service;

import it.unipi.lsmsd.neo4food.dao.mongo.*;
import it.unipi.lsmsd.neo4food.dao.neo4j.*;

public class ServiceProvider
{
//    -----------------------MONGODB-----------------------
    private static final UserMongoDAO userService = new UserMongoDAO();
    private static final RestaurantMongoDAO restaurantService = new RestaurantMongoDAO();
    private static final OrderMongoDAO orderService = new OrderMongoDAO();
    private static final AdminMongoDAO adminService = new AdminMongoDAO();
    private static final AggregationMongoDAO aggregationService = new AggregationMongoDAO();
//    -----------------------NEO4J-----------------------
    private static final SocialNeoDAO socialService = new SocialNeoDAO();
    private static final SupportNeoDAO supportService = new SupportNeoDAO();
    private static final UtilityNeoDAO utilityService = new UtilityNeoDAO();

//    ----------------------- METODI ----------------------
//    -----------------------MONGODB-----------------------
    public static UserMongoDAO getUserService(){return userService;}
    public static RestaurantMongoDAO getRestaurantService(){return restaurantService;}
    public static OrderMongoDAO getOrderService(){return orderService;}
    public static AdminMongoDAO getAdminService(){return adminService;}
    public static AggregationMongoDAO getAggregationService(){return aggregationService;}
//    -----------------------NEO4J-----------------------
    public static SocialNeoDAO getSocialService(){return socialService;}
    public static SupportNeoDAO getSupportService(){return supportService;}
    public static UtilityNeoDAO getUtilityService(){return utilityService;}
}
