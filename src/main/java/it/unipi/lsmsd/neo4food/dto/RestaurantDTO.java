package it.unipi.lsmsd.neo4food.dto;

import it.unipi.lsmsd.neo4food.dto.DishDTO;

import java.util.List;

public class RestaurantDTO {
    private String id;
    private String name;
    private String pricerange;
    private Float rating;
    private ListDTO<DishDTO> dishes;

    public RestaurantDTO(String id, String name, String pricerange, Float rating, ListDTO<DishDTO> list){
        this.id = id;
        this.name = name;
        this.pricerange = pricerange;
        this.rating = rating;
        this.dishes = list;
    }

    public void setId(String id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setPricerange(String pricerange) {this.pricerange = pricerange;}
    public void setRating(Float rating) {this.rating = rating;}
    public void setDishes(ListDTO<DishDTO> list) {this.dishes = list;}

    public String getId() {return id;}
    public String getName() {return name;}
    public String getPricerange() {return pricerange;}
    public Float getRating() {return rating;}
    public ListDTO<DishDTO> getDishes() {return dishes;}

    @Override
    public String toString() {
        return "RestaurantDTO{" +
                "id='" + id +
                ", name='" + name +
                ", pricerange='" + pricerange +
                ", rating=" + rating +
                ", dishes=" + dishes +
                '}';
    }
}
