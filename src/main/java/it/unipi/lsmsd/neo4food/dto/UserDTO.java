package it.unipi.lsmsd.neo4food.dto;

public class UserDTO {
    private String id;
    private String username;
    private String email;

    public UserDTO(String id, String username, String email){
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                "username=" + username +
                "email=" + email +
                "}";
    }
}
