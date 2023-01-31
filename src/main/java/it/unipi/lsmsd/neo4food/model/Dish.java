package it.unipi.lsmsd.neo4food.model;

public class Dish {
    private final String id;
    private final String name;
    private final Float cost;
    private final String currency;
    private final String description;
    private final String ownerid;

    public Dish(String i, String n, Float c, String cu, String d, String o){
        id = i;
        name = n;
        cost = c;
        currency = cu;
        description = d;
        ownerid = o;
    }

    public String getId() {return id;}
    public String getName() {return name;}
    public Float getCost() {return cost;}
    public String getCurrency() {return currency;}
    public String getDescription() {return description;}

}
