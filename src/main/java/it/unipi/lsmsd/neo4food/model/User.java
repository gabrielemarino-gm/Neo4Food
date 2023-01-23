package it.unipi.lsmsd.neo4food.model;

import java.util.ArrayList;
import java.util.List;

public class User extends RegisteredUser{
    private String username;
    private List<Order> orders;

    public User(String id){super.setId(id);}

    public void setUsername(String username) {this.username = username;}
    public void setOrders(List<Order> orders) {this.orders = orders;}

    public String getUsername() {return username;}
    public List<Order> getOrders() {return orders;}
}
