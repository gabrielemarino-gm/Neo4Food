package it.unipi.lsmsd.neo4food.dto;

public class RestaurantDTO {
    private String id;
    private String name;
    private int rating;

    public RestaurantDTO(String id, String name, int rating){
        this.id = id;
        this.name = name;
        this.rating = rating;
    }

    public void setId(String id) {this.id = id;}

    public void setName(String name) {this.name = name;}

    public void setRating(int rating) {this.rating = rating;}

    public String getId() {return id;}

    public String getName() {return name;}

    public int getRating() {return rating;}

    @Override
    public String toString() {
        return "RestaurantDTO{" +
                "id=" + id +
                "name=" + name +
                "rating=" + rating +
                "}";
    }
}
