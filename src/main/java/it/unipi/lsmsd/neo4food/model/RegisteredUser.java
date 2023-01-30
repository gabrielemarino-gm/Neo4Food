package it.unipi.lsmsd.neo4food.model;

public abstract class RegisteredUser {
    private String id;
    private String password;
    private String email;
    private String phoneNumber;
    private String address;
    private String zipcode;
    private Boolean isRestaurant;
//-----------
    RegisteredUser(String i, String p, String e, String pn, String a, String z, Boolean R){
        id = i;
        password = p;
        email = e;
        phoneNumber = pn;
        address = a;
        zipcode = z;
        isRestaurant = R;
    }
//-----------
    public void setId(String id) {this.id = id;}
    public void setPassword(String password) {this.password = password;}
    public void setEmail(String email) {this.email = email;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setAddress(String address) {this.address = address;}
    public void setZipcode(String zipcode) {this.zipcode = zipcode;}
    public void setRestaurant(Boolean restaurant) {isRestaurant = restaurant;}
//-----------
    public String getId() {return id;}
    public String getPassword() {return password;}
    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getAddress() {return address;}
    public String getZipcode() {return zipcode;}
    public Boolean getRestaurant() {return isRestaurant;}
//-----------
}
