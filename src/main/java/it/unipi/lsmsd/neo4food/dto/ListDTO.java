package it.unipi.lsmsd.neo4food.dto;

import java.util.List;
// List of DTO items
public class ListDTO <T>{
//    -------------------------------------
    private List<T> list;
    private int itemCount;
//    -------------------------------------
    public void setList(List<T> list) {this.list = list;}
    public void setItemCount(int n) {this.itemCount = n;}
//    -------------------------------------
    public List<T> getList() {return list;}
    public int getItemCount() {return itemCount;}
//    -------------------------------------
    @Override
    public String toString(){
        return "ListDTO{" +
                "elements=" + list +
                ", count=" + itemCount +
                "}";
    }
}
