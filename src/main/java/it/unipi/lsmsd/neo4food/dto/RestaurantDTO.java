package it.unipi.lsmsd.neo4food.dto;

import it.unipi.lsmsd.neo4food.dto.DishDTO;

import java.lang.management.ThreadInfo;
import java.util.List;

public class RestaurantDTO {
//    -------------------------------------
    private String id;
    private String name;
    private String priceRange;
    private Float rating;
    private String email;
    private String password;
    private String address;
    private List<DishDTO> dishes;
    private List<OrderDTO> pendingOrders;
//    -------------------------------------
    public void setId(String id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setPriceRange(String priceRange) {this.priceRange = priceRange;}
    public void setRating(Float rating) {this.rating = rating;}
    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}
    public void setAddress(String address) {this.address = address;}
    public void setDishes(List<DishDTO> list) {this.dishes = list;}
    public void setPendingOrders(List<OrderDTO> list) {this.pendingOrders = list;}
//    -------------------------------------
    public String getId() {return id;}
    public String getName() {return name;}
    public String getPriceRange() {return priceRange;}
    public Float getRating() {return rating;}
    public String getEmail() {return email;}
    public String getPassword() {return password;}
    public String getAddress() {return address;}
    public List<DishDTO> getDishes() {return dishes;}
    public List<OrderDTO> getOrders() {return pendingOrders;}
//    -------------------------------------
    @Override
    public String toString() {
        return "RestaurantDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", priceRange='" + priceRange + '\'' +
                ", rating=" + rating +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", dishes=" + dishes +
                ", pendingOrders=" + pendingOrders +
                '}';
    }
}