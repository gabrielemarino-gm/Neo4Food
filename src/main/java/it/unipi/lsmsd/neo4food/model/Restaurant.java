package it.unipi.lsmsd.neo4food.model;

import java.util.List;
import java.util.ArrayList;

public class Restaurant extends RegisteredUser{
    private String name;
    private int position;
    private String category;
    private double meanScore;
    private int priceRange;
    private List<Dish> dishes;
    private List<Order> orders;
//  COMPLETE CONSTRUCTOR NEW RESTAURANT
    public Restaurant(String email, String password, String name,
                      String fullAddress, String zipcode,
                      String category){
        super(email, password, fullAddress, zipcode, true);
        this.name = name;
        position = 0;
        this.category = category;
        meanScore = 0;
        priceRange = 0;
        dishes = new ArrayList<Dish>();
        orders = new ArrayList<Order>();
    }
//  SET ONLY
    public void setName(String newName) {name = newName;}
    public void setPosition(int newPosition) {position = newPosition;}
    public void setCategory(String newCategory) {category = newCategory;}
    public void setMeanScore(double newMeanScore) {meanScore = newMeanScore;}
    public void setPriceRange(int newPriceRange) {priceRange = newPriceRange;}
    public void setDishes(List<Dish> newDishes) {dishes = newDishes;}
    public void setOrders(List<Order> newOrders) {orders = newOrders;}
//  GET ONLY
    public String getName() {return name;}
    public int getPosition() {return position;}
    public  String getCategory() {return category;}
    public double getMeanScore() {return meanScore;}
    public int getPriceRange() {return priceRange;}
    public List<Dish> getDishes() {return dishes;}
    public List<Order> getOrders() {return orders;}
//  OTHER LOGIC
    public void addDish(Dish newDish) {dishes.add(newDish);}
    public void addOrder(Order newOrder) {orders.add(newOrder);}
}
