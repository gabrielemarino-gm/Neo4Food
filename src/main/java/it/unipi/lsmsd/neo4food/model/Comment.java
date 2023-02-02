package it.unipi.lsmsd.neo4food.model;

public class Comment {
    private User author;
    private Restaurant target;
    private String text;
    private int vote;
//  COMPLETE CONSTRUCTOR NEW COMMENT
    public Comment(){

    }
//  SET ONLY
    public void setAuthor(User newAuthor) {author = newAuthor;}
    public void setTarget(Restaurant newTarget) {target = newTarget;}
    public void setText(String newText) {text = newText;}
    public void setVote(int newVote){
        if(newVote > 5){
            vote = 5;
        }else{
            vote = Math.min(newVote, 0);
        }
    }
//  GET ONLY
    public User getAuthor() {return author;}
    public Restaurant getTarget() {return target;}
    public String getText() {return text;}
    public int getVote() {return vote;}
//  OTHER LOGIC

    Comment(User author, Restaurant target, String text, int vote){
        this.author = author;
        this.target = target;
        this.vote = (vote > 5) ? 5 : Math.max(vote, 0);
        this.text = (text != null || !text.equals("")) ? text : "";
    }

    public void deleteComment(){

    }

    public void modifyComment(){

    }

}
