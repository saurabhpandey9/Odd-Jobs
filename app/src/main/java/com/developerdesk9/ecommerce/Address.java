package com.developerdesk9.ecommerce;

public class Address {
    String name;
    String address;
    String address_key;

    Address(){

    }

    public Address(String name, String address, String address_key) {
        this.name = name;
        this.address = address;
        this.address_key = address_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_key() {
        return address_key;
    }

    public void setAddress_key(String address_key) {
        this.address_key = address_key;
    }
}
