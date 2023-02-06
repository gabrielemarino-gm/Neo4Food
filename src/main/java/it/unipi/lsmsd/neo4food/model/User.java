package it.unipi.lsmsd.neo4food.model;

import java.util.List;

public class User extends RegisteredUser {
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private String paymentMethod;
    private String paymentNumber;
    private List<Order> orders;
//  COMPLETE CONSTRUCTOR NEW USER
    public User(String email, String password, String username,
                String firstName, String lastName, String fullAddress,
                String phoneNumber, String zipcode){
        super(email, password, fullAddress, zipcode, false);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        paymentNumber = "";
        paymentMethod = "";
    }
//  SET ONLY
    public void setFirstName(String newFirstName) {firstName = newFirstName;}
    public void setLastName(String newLastName) {lastName = newLastName;}
    public void setUsername(String newUsername) {username = newUsername;}
    public void setPhoneNumber(String newPhoneNumber) {phoneNumber = newPhoneNumber;}
    public void setPaymentMethod(String newPaymentMethod) {paymentMethod = newPaymentMethod;}
    public void setPaymentNumber(String newPaymentNumber) {paymentNumber = newPaymentNumber;}
//  GET ONLY
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getUsername() {return username;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getPaymentMethod() {return paymentMethod;}
    public String getPaymentNumber() {return paymentNumber;}
//  OTHER LOGIC

}
