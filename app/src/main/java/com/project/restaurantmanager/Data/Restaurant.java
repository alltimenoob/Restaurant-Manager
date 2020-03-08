package com.project.restaurantmanager.Data;

public class Restaurant {
    String name;
    String city;
    String address;
    String contact;
    String rid;
    String image;

    public Restaurant(String name, String city, String address, String contact, String rid, String image) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.contact = contact;
        this.rid = rid;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }
}
