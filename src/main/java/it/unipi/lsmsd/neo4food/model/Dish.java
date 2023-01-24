package it.unipi.lsmsd.neo4food.model;

public class Dish {
    private String id;
    private String name;
    private Float cost;
    private String currency;
    private String description;
    private String ownerid;

    public Dish(String i, String n, Float c, String cu, String d, String o){
        id = i;
        name = n;
        cost = c;
        currency = cu;
        description = d;
        ownerid = o;
    }

    public String getName() {return name;}
    public Float getCost() {return cost;}
    public String getCurrency() {return currency;}
    public String getDescription() {return description;}

    @Override
    public String toString() {
        return "Dish{" +
                "id='" + id +
                ", name='" + name +
                ", cost=" + cost +
                ", currency='" + currency +
                ", description='" + description +
                ", ownerid='" + ownerid +
                '}';
    }
}
