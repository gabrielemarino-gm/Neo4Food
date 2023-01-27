package it.unipi.lsmsd.neo4food.dto;

public class DishDTO
{
    private String id;
    private String name;
    private Float price;
    private int quantity;
    private String currency;
    private String description;
    private String ownerid;

    public DishDTO(String i, String n, Float p, String cu, String d, String o)
    {
        id = i;
        name = n;
        quantity = 0;
        price = p;
        currency = cu;
        description = d;
        ownerid = o;
    }

    public DishDTO(String i, String n, Float p, String c){
        id = i;
        name = n;
        price = p;
        currency = c;
        quantity = 1;
    }

    public int incQuantity(){return ++quantity;}
    public int decQuantity(){return --quantity;}


    public String getId() {return id;}
    public String getName() {return name;}
    public Float getPrice() {return price;}
    public String getCurrency() {return currency;}
    public String getDescription() {return description;}

    @Override
    public String toString() {
        return "DishDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", currency='" + currency + '\'' +
                ", description='" + description + '\'' +
                ", ownerid='" + ownerid + '\'' +
                '}';
    }
}
