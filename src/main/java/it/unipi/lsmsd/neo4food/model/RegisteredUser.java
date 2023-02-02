package it.unipi.lsmsd.neo4food.model;

public abstract class RegisteredUser {
    private String email;
    private String password;
    private String address;
    private String zipcode;
    private Boolean isRestaurant;

//  COMPLETE CONSTRUCTOR
    RegisteredUser(String email, String password, String address, String zipcode, boolean isRestaurant){
        this.email = email;
        this.password = password;
        this.address = address;
        this.zipcode = zipcode;
        this.isRestaurant = isRestaurant;
    }
//  SET ONLY
    public boolean setPassword(String oldPassword, String newPassword){
        if(password.equals(oldPassword)){
            password = newPassword;
            return true;
        }
        return false;
    }
    public void setEmail(String newEmail) {email = newEmail;}
    public void setAddress(String newAddress) {address = newAddress;}
    public void setZipcode(String newZipcode) {zipcode = newZipcode;}
    public void setRestaurant(boolean newIsRestaurant) {isRestaurant = newIsRestaurant;}
//  GET ONLY
    public String getEmail() {return email;}
    public String getPassword() {return password;}
    public String getAddress() {return address;}
    public String getZipcode() {return zipcode;}
    public boolean isRestaurant() {return isRestaurant;}
//  OTHER LOGIC

}
