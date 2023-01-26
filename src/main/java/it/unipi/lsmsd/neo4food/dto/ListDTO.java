package it.unipi.lsmsd.neo4food.dto;

import java.util.List;

// List of restaurants, orders or dishes
public class ListDTO <T>{

    private List<T> list;
    private int itemCount;

    public List<T> getList() {return list;}
    public int getItemCount() {return itemCount;}
    public void setList(List<T> list) {this.list = list;}
    public void setItemCount(int n) {this.itemCount = n;}

    public String toString(){
        return "ListDTO{" +
                "elements=" + list +
                ", count=" + itemCount +
                "}";
    }
}
