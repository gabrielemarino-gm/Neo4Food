package it.unipi.lsmsd.neo4food.model;

public abstract class RegisteredUser {
    private String id;
    private String password;
    private String email;
    private String phoneNumber;
    private String address;
    private String country;
    private String zipcode;
    private Boolean isRestaurant;
//-----------
    public void setId(String id) {this.id = id;}
    public void setPassword(String password) {this.password = password;}
    public void setEmail(String email) {this.email = email;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setAddress(String address) {this.address = address;}
    public void setCountry(String country) {this.country = country;}
    public void setZipcode(String zipcode) {this.zipcode = zipcode;}
    public void setRestaurant(Boolean restaurant) {isRestaurant = restaurant;}
//-----------
    public String getId() {return id;}
    public String getPassword() {return password;}
    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getAddress() {return address;}
    public String getCountry() {return country;}
    public String getZipcode() {return zipcode;}
    public Boolean getRestaurant() {return isRestaurant;}
//-----------
    @Override
    public String toString() {
        return "RegisteredUser{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", isRestaurant=" + isRestaurant +
                '}';
    }
}
