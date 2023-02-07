package it.unipi.lsmsd.neo4food.dto;

public class AnalyticsDTO {
    private String zipcode;
    private String user;
    private String restaurant;
    private int count;
    private double dub;
    private String dish;


    public void setZipcode(String zipcode){this.zipcode = zipcode;}
    public void setUser(String user){this.user = user;}
    public void setRestaurant(String restaurant){this.restaurant = restaurant;}
    public void setCount(int count){this.count = count;}
    public void setDouble(double dub){this.dub = dub;}
    public void setDish(String dish){this.dish = dish;}

    public String getZipcode(){return zipcode;}
    public String getUser(){return user;}
    public String getRestaurant(){return restaurant;}
    public int getCount(){return count;}
    public double getDouble(){return dub;}
    public String getDish(){return dish;}
}
