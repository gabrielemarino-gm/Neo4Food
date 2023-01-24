package it.unipi.lsmsd.neo4food.model;

import java.util.ArrayList;
import java.util.List;

public class Restaurant extends RegisteredUser{
    private String name;
    private double meanScore;
    private List<Dish> dishes;

    public Restaurant(String i, String e, String n, String p, String fn, String ln, String pn, String a, String z){
        super(i, p, e, pn, a, z, true);
        name = n;
        meanScore = 0.0;
        dishes = null;
    }
}
