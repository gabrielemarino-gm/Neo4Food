package it.unipi.lsmsd.neo4food.model;

import java.util.ArrayList;
import java.util.List;

public class User extends RegisteredUser{
    private String username;
    private String firstName;
    private String lastName;
    private String paymentMethod;
    private String paymentNumber;

    public User(String id, String email, String uname,
                String psw, String fname, String lname,
                String phone, String addr, String zip,
                String pmethod, String pnumber){
        super(id, psw, email, phone, addr, zip, false);
        username = uname;
        firstName = fname;
        lastName = lname;
        paymentMethod = pmethod;
        paymentNumber = pnumber;
    }

    public void setUsername(String username) {this.username = username;}

    public String getUsername() {return username;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getPaymentMethod() {return paymentMethod;}
    public String getPaymentNumber() {return paymentNumber;}
}
