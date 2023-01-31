package it.unipi.lsmsd.neo4food.service;

import it.unipi.lsmsd.neo4food.dao.mongo.*;

public class ServiceProvider {
    private static final UserMongoDAO userService = new UserMongoDAO();
    private static final RestaurantMongoDAO restaurantService = new RestaurantMongoDAO();
    private static final OrderMongoDAO orderService = new OrderMongoDAO();
    private static final AdminMongoDAO adminService = new AdminMongoDAO();

    public static UserMongoDAO getUserService(){return userService;}
    public static RestaurantMongoDAO getRestaurantService(){return restaurantService;}
    public static OrderMongoDAO getOrderService(){return orderService;}
    public static AdminMongoDAO getAdminService(){return adminService;}

}
