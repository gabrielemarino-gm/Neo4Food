package it.unipi.lsmsd.neo4food.dto;

import it.unipi.lsmsd.neo4food.dto.DishDTO;

import java.lang.management.ThreadInfo;
import java.util.List;

public class RestaurantDTO {
    private String id;
    private String name;
    private String pricerange;
    private Float rating;
    private ListDTO<DishDTO> dishes;
    private ListDTO<OrderDTO> pendingOrders;
    private String email;
    private String password;
    private String address;

    public RestaurantDTO(String id, String name, String pricerange, Float rating, ListDTO<DishDTO> listD, ListDTO<OrderDTO> listO){
        this.id = id;
        this.name = name;
        this.pricerange = pricerange;
        this.rating = rating;
        this.dishes = listD;
        this.pendingOrders = listO;
    }
    public RestaurantDTO(String id, String name,String email, String address){
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;

    }

    public void setId(String id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setPricerange(String pricerange) {this.pricerange = pricerange;}
    public void setRating(Float rating) {this.rating = rating;}
    public void setDishes(ListDTO<DishDTO> list) {this.dishes = list;}
    public void setPendingOrders(ListDTO<OrderDTO> list) {this.pendingOrders = list;}

    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}
    public void setAddress(String address) {this.address = address;}

    public String getId() {return id;}
    public String getName() {return name;}
    public String getPricerange() {return pricerange;}
    public Float getRating() {return rating;}
    public String getPassword() {return password;}
    public String getEmail() {return email;}
    public String getAddress() {return address;}
    public ListDTO<DishDTO> getDishes() {return dishes;}

    public ListDTO<OrderDTO> getOrders() {return pendingOrders;}

    @Override
    public String toString() {
        return "RestaurantDTO{" +
                "id='" + id +
                ", name='" + name +
                ", pricerange='" + pricerange +
                ", rating=" + rating +
                ", dishes=" + dishes +
                '}';
    }
}
