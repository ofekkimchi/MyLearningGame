package com.example.mylearninggame.Model;

public class Profile extends User {
    String id;
    String description, pic;
    int coins;

    public Profile(String id, String fname, String lname, String phone, String email, String password) {
        super(id, fname, lname, phone, email, password,false);
    }
}
