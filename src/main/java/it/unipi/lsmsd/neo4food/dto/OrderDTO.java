package it.unipi.lsmsd.neo4food.dto;

import it.unipi.lsmsd.neo4food.dto.DishDTO;

import java.util.List;
import java.util.ArrayList;

public class OrderDTO {
//        Li setto solo al momento del checkout
    private String user;
    private String restaurant;
    private String paymentMethod;
    private String paymentNumber;
    private String address;
    private String zipcode;
//        ---------------------------------------
    private float total;
    private List<DishDTO> items;

    public OrderDTO(){
        total = 0;
        items = new ArrayList<DishDTO>();
    }

    public void setRestaurant(String restaurant) {this.restaurant = restaurant;}
    public void setUser(String user) {this.user = user;}
    public void setPaymentMethod(String paymentMethod) {this.paymentMethod = paymentMethod;}
    public void setPaymentNumber(String paymentNumber) {this.paymentNumber = paymentNumber;}
    public void setAddress(String address) {this.address = address;}
    public void setZipcode(String zipcode) {this.zipcode = zipcode;}

    public void setTotal(float total) {this.total = total;}
    public void setItems(List<DishDTO> items) {this.items = items;}
//    ---------------
    private float addToTotal(float q){return (total+=q);}
    private float removeFromTotal(float q){return (total-=q);}

    private DishDTO getDish(String id){
        for(DishDTO i: items){
            if(id.equals(i.getId())) {
                return i;
            }
        }
        return null;
    }
    public int addItem(DishDTO newDish){
        DishDTO e = getDish(newDish.getId());
        if(e != null){
//            Elemento gia presente, devo incrementare
            items.get(items.indexOf(e)).incQuantity();
        } else {
//            Elemento assente, devo inserirne uno nuovo
            items.add(newDish);
        }
        addToTotal(newDish.getPrice());
        return 1;
    }
    public int removeItem(String id){
        DishDTO e = getDish(id);
        if(e == null){
//            Piatto non presente nell ordine
            return 0;
        }else{
//            Piatto presente, decremento
            removeFromTotal(e.getPrice());
            if(items.get(items.indexOf(e)).decQuantity() == 0){
                items.remove(e);
            }
        }
        return 1;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "user='" + user + '\'' +
                ", restaurant='" + restaurant + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentNumber='" + paymentNumber + '\'' +
                ", address='" + address + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", total=" + total +
                ", items=" + items +
                '}';
    }
}
