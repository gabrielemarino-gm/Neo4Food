package it.unipi.lsmsd.neo4food.dto;

import java.util.Date;
import java.util.List;

public class OrderDTO
{
//    -------------------------------------
    private String id;
    private String user;
    private String restaurant;
    private String restaurantId;
    private String paymentMethod;
    private String paymentNumber;
    private String address;
    private String zipcode;
//    -------------------------------------
    private Date creationDate;
    private Date deliveryDate;
    private double total;
    private String currency;
    private boolean isSent;
    private List<DishDTO> dishes;
//    -------------------------------------
    public void setId(String id){this.id = id;}
    public void setUser(String user) {this.user = user;}
    public void setRestaurant(String restaurant) {this.restaurant = restaurant;}
    public void setRestaurantId(String restaurantId) {this.restaurantId = restaurantId;}
    public void setPaymentMethod(String paymentMethod) {this.paymentMethod = paymentMethod;}
    public void setPaymentNumber(String paymentNumber) {this.paymentNumber = paymentNumber;}
    public void setAddress(String address) {this.address = address;}
    public void setZipcode(String zipcode) {this.zipcode = zipcode;}
//    -------------------------------------

    public void setCreationDate(Date date){this.creationDate = date;}
    public void setDeliveryDate(Date date){this.deliveryDate = date;}
    public void setSent() {isSent = true;}
    public void setTotal(double total) {this.total = total;}
    public void setCurrency(String currency) {this.currency = currency;}
    public void setDishes(List<DishDTO> items) {this.dishes = items;}
//    -------------------------------------
    public String getId() {return id;}
    public String getUser() {return user;}
    public String getRestaurant() {return restaurant;}
    public String getRestaurantId() {return restaurantId;}
    public String getPaymentMethod() {return paymentMethod;}
    public String getPaymentNumber() {return paymentNumber;}
    public String getAddress() {return address;}
    public String getZipcode() {return zipcode;}
//    -------------------------------------
    public Date getCreationDate(){return creationDate;}
    public Date getDeliveryDate(){return deliveryDate;}
    public boolean getStatus(){return isSent;}
    public double getTotal() {return total;}
    public String getCurrency() {return currency;}
    public List<DishDTO> getDishes() {return dishes;}
//    -------------------------------------
    @Override
    public String toString() {
        return "OrderDTO{" +
                "id='" + id + '\'' +
                ", user='" + user + '\'' +
                ", restaurant='" + restaurant + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentNumber='" + paymentNumber + '\'' +
                ", address='" + address + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", creationDate=" + creationDate +
                ", deliveryDate=" + deliveryDate +
                ", total=" + total +
                ", currency='" + currency + '\'' +
                ", isSent=" + isSent +
                ", dishes=" + dishes +
                '}';
    }
}
