package it.unipi.lsmsd.neo4food.dto;

public class DishDTO {
//    -------------------------------------
    private String id;
    private String name;
    private Double price;
    private int quantity;
    private String currency;
    private String description;
    private String restaurantid;
//    -------------------------------------
    public void setId(String id){this.id = id;}
    public void setName(String name){this.name = name;}
    public void setPrice(Double price){this.price =price;}
    public void setQuantity(int quantity){this.quantity =quantity;}
    public void setCurrency(String currency){this.currency =currency;}
    public void setDescription(String description){this.description = description;}
    public void setRestaurantId(String restaurantid){this.restaurantid = restaurantid;}
//    -------------------------------------
    public String getId() {return id;}
    public String getName() {return name;}
    public Double getPrice() {return price;}
    public int getQuantity() {return quantity;}
    public String getCurrency() {return currency;}
    public String getDescription() {return description;}
    public String getRestaurantId() {return restaurantid;}
//    -------------------------------------
    @Override
    public String toString() {
        return "DishDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", currency='" + currency + '\'' +
                ", description='" + description + '\'' +
                ", restaurantid='" + restaurantid + '\'' +
                '}';
    }
}
