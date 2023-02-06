package it.unipi.lsmsd.neo4food.model;

public class Dish {
    private String name;
    private Restaurant owner;
    private Double price;
    private String currency;
    private String description;
//  COMPLETE CONSTRUCTOR
    public Dish(String name, Double price, String currency, String description){
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.description = description;
    }
//  SET ONLY
    public void setName(String newName) {name = newName;}
    public void setPrice(Double newPrice) {price = newPrice;}
    public void setCurrency(String newCurrency) {currency = newCurrency;}
    public void setDescription(String newDescription) {description = newDescription;}
//  GET ONLY
    public String getName() {return name;}
    public Double getPrice() {return price;}
    public String getCurrency() {return currency;}
    public String getDescription() {return description;}
//  OTHER LOGIC
    public void addToRestaurant(Restaurant target){
        target.addDish(this);
    }
    public void addToOrder(Order target){
        target.addDish(this);
    }
}
