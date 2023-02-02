package it.unipi.lsmsd.neo4food.dto;

public class CommentDTO
{
    private String commentText;
    private double rate;
    private String userName;
    private String restaurantID;

    public void setCommentText(String t) {this.commentText = t;}
    public void setRate(double r) {this.rate = r;}
    public void setUserName(String u) {this.userName = u;}
    public void setRestaurantID(String id) {this.restaurantID = id;}

    public String getCommentText() {return this.commentText;}
    public String getUserName() {return this.userName;}
    public String getRestaurantID() {return this.restaurantID;}
    public double getRate() {return this.rate;}
    
    @Override
    public String toString() {
        return "CommentDTO{" +
                "commentText='" + commentText + '\'' +
                ", rate=" + rate +
                ", userName='" + userName + '\'' +
                ", restaurantID='" + restaurantID + '\'' +
                '}';
    }
}
