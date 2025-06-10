package com.example.mylearninggame.Model;

import com.example.mylearninggame.R;

public class Profile extends User {
    protected String id;
    protected String description, pic;

    public Profile(String id, String fname, String lname, String phone, String email, String password) {
        super(id, fname, lname, phone, email, password, false, 0, R.drawable.default_profile);
    }

    public Profile(String id, String fname, String lname, String phone, String email, String password, boolean isAdmin, String id1, String description, String pic, int coins) {
        super(id, fname, lname, phone, email, password, isAdmin, coins, R.drawable.default_profile);
        this.description = description;
        this.pic = pic;
    }

    public Profile(String id, String description, String pic, int coins) {
        this.id = id;
        this.description = description;
        this.pic = pic;
        this.setCoins(coins);
    }

    public Profile() {
        super();
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

    @Override
    public String toString() {
        return "Profile{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", pic='" + pic + '\'' +
                super.toString() +
                '}';
    }
}
