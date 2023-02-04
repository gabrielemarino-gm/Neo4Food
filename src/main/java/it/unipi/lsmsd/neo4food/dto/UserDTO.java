package it.unipi.lsmsd.neo4food.dto;

public class UserDTO {
//    -------------------------------------
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String zipcode;
    private String paymentMethod;
    private String paymentNumber;
    private int nfollowers;
//    -------------------------------------
    public void setId(String id) {this.id = id;}
    public void setUsername(String username) {this.username = username;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setEmail(String email) {this.email = email;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setAddress(String address) {this.address = address;}
    public void setZipcode(String zipcode) {this.zipcode = zipcode;}
    public void setPaymentMethod(String paymentMethod) {this.paymentMethod = paymentMethod;}
    public void setPaymentNumber(String paymentNumber) {this.paymentNumber = paymentNumber;}
    public void setNfollowers(int nfollowers) {this.nfollowers = nfollowers;}

    //    -------------------------------------
    public String getId() {return id;}
    public String getUsername() {return username;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getAddress() {return address;}
    public String getZipcode() {return zipcode;}
    public String getPaymentMethod() {return paymentMethod;}
    public String getPaymentNumber() {return paymentNumber;}
    public int getNfollowers() {return nfollowers;}
    //    -------------------------------------
    @Override
    public String toString() {
        return "UserDTO{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentNumber='" + paymentNumber + '\'' +
                ", nFollowers='" + nfollowers + '\'' +
                '}';
    }
}
