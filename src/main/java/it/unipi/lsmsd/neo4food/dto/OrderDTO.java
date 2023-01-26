package it.unipi.lsmsd.neo4food.dto;

import java.util.List;
import java.util.ArrayList;

public class OrderDTO {
    private String user;
    private String restaurant;
    private float total;
    private List<DishDTO> items;

    public OrderDTO(){
        user = null;
        restaurant = null;
        total = 0;
        items = new ArrayList<DishDTO>();
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setTotal(float total) {
        this.total = total;
    }
    public void setItems(List<DishDTO> items) {
        this.items = items;
    }

    public float addToTotal(float q){
        total += q;
        return total;
    }
    public float removeFromTotal(float q){
        total -= q;
        return total;
    }
    public int addItem(){

        return 1;
    }
    public int removeItem(){

        return 1;
    }

}
