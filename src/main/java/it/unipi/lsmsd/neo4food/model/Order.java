package it.unipi.lsmsd.neo4food.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private User customer;
    private Restaurant restaurant;

    private List<Dish> ordered;
    private int dishNumber;
    private double total;
    private Date orderDate;
    private Date deliveryDate;
    private String paymentMethod;
    private String paymentNumber;
//  COMPLETE CONSTRUCTOR NEW ORDER
    Order(User customer, Restaurant restaurant, String paymentMethod, String paymentNumber) {
        this.customer = customer;
        this.restaurant = restaurant;

        ordered = new ArrayList<Dish>();
        dishNumber = 0;
        total = 0;
        orderDate = new Date();
        deliveryDate = null;
        this.paymentMethod = paymentMethod;
        this.paymentNumber = paymentNumber;
    }
//  SET ONLY
    public void setCustomer(User newCustomer) {customer = newCustomer;}
    public void setRestaurant(Restaurant newRestaurant) {restaurant = newRestaurant;}
    public void setOrdered(List<Dish> dishes) {ordered = dishes;}
    public void setTotal(Double newTotal) {total = newTotal;}
    public void setOrderDate(Date newDate) {orderDate = newDate;}
    public void setDeliveryDate(Date newDate) {deliveryDate = newDate;}
    public void setPaymentMethod(String newPaymentMethod) {paymentMethod = newPaymentMethod;}
    public void setPaymentNumber(String newPaymentNumber) {paymentNumber = newPaymentNumber;}
//  GET ONLY
    public User getCustomer() {return customer;}
    public Restaurant getRestaurant() {return restaurant;}
    public List<Dish> getOrdered() {return ordered;}
    public int getDishNumber() {return dishNumber;}
    public Double getTotal() {return total;}
    public Date getOrderDate() {return orderDate;}
    public Date getDeliveryDate() {return deliveryDate;}
    public String getPaymentMethod() {return paymentMethod;}
    public String getPaymentNumber() {return paymentNumber;}
//  OTHER LOGIC
    public boolean isDelivered() {return (deliveryDate!= null);}
    public void addDish(Dish newDish) {
        ordered.add(newDish);
        total += newDish.getPrice();
        dishNumber++;
    }
    public boolean removeDish(Dish dish) {
        if(dishNumber > 0){
            ordered.remove(dish);
            total -= dish.getPrice();
            dishNumber--;
            return true;
        }
        return false;
    }
    public void deliveryOrder(){
        deliveryDate = new Date();
    }
}
