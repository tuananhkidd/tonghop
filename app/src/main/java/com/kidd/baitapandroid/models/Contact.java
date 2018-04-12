package com.kidd.baitapandroid.models;

import java.io.Serializable;

public class Contact implements Serializable {
    private String name;
    private String phonenumber;

    public Contact( String name,  String phonenumber) {
        this.name = name;
        this.phonenumber = phonenumber;
    }

    public Contact() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
