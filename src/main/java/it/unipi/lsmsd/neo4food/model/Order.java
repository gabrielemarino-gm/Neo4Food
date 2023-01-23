package it.unipi.lsmsd.neo4food.model;

import java.util.Date;
import java.util.List;

public class Order {
    private User customer;
    private Restaurant restaurant;
    private List<Dish> ordered;
    private int dishNumber;
    private Float total;
    private Date deliveryDate;
    private Date orderDate;
    private String paymentMethod;
    private int cardNumber;

}
