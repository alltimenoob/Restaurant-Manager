package com.project.restaurantmanager.Data;

public class Orders {
    private String date;
    private String amount;
    private String[] items=new String[10];
    private String[] quantity=new String[10];

    public Orders(String date, String amount,String[] items,String[] quantity){
        this.date = date;
        this.amount = amount;
        this.items = items;
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    public String[] getQuantity() {
        return quantity;
    }

    public void setQuantity(String[] quantity) {
        this.quantity = quantity;
    }
}
