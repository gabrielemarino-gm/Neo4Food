package it.unipi.lsmsd.neo4food.dto;

public class UserDTO {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String zipcode;
    private String birthday;

    public UserDTO(String id, String username, String firstName, String lastName, String email, String phoneNumber, String address, String zipcode, String birthday){
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.zipcode = zipcode;
        this.birthday = birthday;
    }

    public void setId(String id) {this.id = id;}
    public void setUsername(String username) {this.username = username; }
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName; }
    public void setEmail(String email) {this.email = email; }
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber; }
    public void setAddress(String address) {this.address = address; }
    public void setZipcode(String zipcode) {this.zipcode = zipcode; }
    public void setBirthday(String birthday) {this.birthday = birthday; }

    public String getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getAddress() {
        return address;
    }
    public String getZipcode() {
        return zipcode;
    }
    public String getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username=" + username +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", email=" + email +
                ", phoneNumber=" + phoneNumber +
                ", address=" + address +
                ", zipcode=" + zipcode +
                ", birthday=" + birthday +
                '}';
    }
}
