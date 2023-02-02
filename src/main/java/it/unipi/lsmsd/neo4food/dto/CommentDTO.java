package it.unipi.lsmsd.neo4food.dto;

public class CommentDTO
{
    private String restaurantID;
    private String username;
    private double rate;
    private String review;

    public void setRestaurantID(String rid) {this.restaurantID = rid;}
    public void setUserName(String user) {this.username = user;}
    public void setRate(double rate) {this.rate = rate;}
    public void setReview(String text) {this.review = text;}

    public String getRestaurantID() {return this.restaurantID;}
    public String getUserName() {return this.username;}
    public double getRate() {return this.rate;}
    public String getReview() {return this.review;}

    @Override
    public String toString() {
        return "CommentDTO{" +
                "restaurantID='" + restaurantID + '\'' +
                ", username='" + username + '\'' +
                ", rate=" + rate +
                ", review='" + review + '\'' +
                '}';
    }
}
