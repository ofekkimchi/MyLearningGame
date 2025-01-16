package com.example.mylearninggame.Model;

public class Profile extends User {
    protected String id;
    protected String description, pic;
    protected int coins;

    public Profile(String id, String fname, String lname, String phone, String email, String password) {
        super(id, fname, lname, phone, email, password,false);
    }

    public Profile(String id, String fname, String lname, String phone, String email, String password, boolean isAdmin, String id1, String description, String pic, int coins) {
        super(id, fname, lname, phone, email, password, isAdmin);
        this.id = id1;
        this.description = description;
        this.pic = pic;
        this.coins = coins;
    }

    public Profile(String id, String description, String pic, int coins) {
        this.id = id;
        this.description = description;
        this.pic = pic;
        this.coins = coins;
    }

    public Profile() {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", pic='" + pic + '\'' +
                ", coins=" + coins +
                ", id='" + id + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
