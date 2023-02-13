package it.unipi.lsmsd.neo4food.dto;

public class AnalyticsDTO
{
    private String zipcode;
    private String user;
    private String restaurant;
    private int count;
    private double dub;
    private String dish;
    private String currency;
    private String orario;


    public void setZipcode(String zipcode){this.zipcode = zipcode;}
    public void setUser(String user){this.user = user;}
    public void setRestaurant(String restaurant){this.restaurant = restaurant;}
    public void setCount(int count){this.count = count;}
    public void setDouble(double dub){this.dub = dub;}
    public void setDish(String dish){this.dish = dish;}
    public void setCurrency(String c){this.currency = c;}
    public void setOrario(String orari){this.orario = orari;}

    public String getZipcode(){return zipcode;}
    public String getUser(){return user;}
    public String getRestaurant(){return restaurant;}
    public int getCount(){return count;}
    public double getDouble(){return dub;}
    public String getDish(){return dish;}
    public String getCurrency(){return currency;}
    public String getOrario(){return orario;}


    @Override
    public String toString() {
        return "AnalyticsDTO{" +
                "zipcode='" + zipcode + '\'' +
                ", user='" + user + '\'' +
                ", restaurant='" + restaurant + '\'' +
                ", count=" + count +
                ", dub=" + dub +
                ", dish='" + dish + '\'' +
                ", currency='" + currency + '\'' +
                ", orario='" + orario + '\'' +
                '}';
    }
}
