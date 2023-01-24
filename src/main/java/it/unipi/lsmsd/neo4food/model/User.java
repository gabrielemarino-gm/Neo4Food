package it.unipi.lsmsd.neo4food.model;

import java.util.ArrayList;
import java.util.List;

public class User extends RegisteredUser{
    private String username;
    private String firstName;
    private String lastName;

    private List<Order> orders;

    public User(String i, String e, String u, String p, String fn, String ln, String pn, String a, String z){
        super(i, p, e, pn, a, z, false);
        username = u;
        firstName = fn;
        lastName = ln;

    }

    public void setUsername(String username) {this.username = username;}
    public void setOrders(List<Order> orders) {this.orders = orders;}

    public String getUsername() {return username;}
    public List<Order> getOrders() {return orders;}
}
