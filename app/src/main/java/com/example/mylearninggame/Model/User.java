package com.example.mylearninggame.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.mylearninggame.R; // Import R class for default drawable

public class User implements Serializable {
    String id;
    String fname, lname, phone, email, password;
    boolean isAdmin;
    int coins;
    int profilePictureId; // New field for profile picture
    private List<Integer> purchasedProfilePictures;

    public User(String id, String fname, String lname, String phone, String email, String password, boolean isAdmin, int coins, int profilePictureId) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.coins = coins;
        this.profilePictureId = profilePictureId;
        this.purchasedProfilePictures = new ArrayList<>();
    }

    public User() {
        this.coins = 0;
        this.profilePictureId = R.drawable.default_profile; // Default profile picture
        this.purchasedProfilePictures = new ArrayList<>();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getProfilePictureId() {
        return profilePictureId;
    }

    public void setProfilePictureId(int profilePictureId) {
        this.profilePictureId = profilePictureId;
    }

    public List<Integer> getPurchasedProfilePictures() {
        return purchasedProfilePictures;
    }

    public void addPurchasedProfilePicture(int pictureId) {
        if (!purchasedProfilePictures.contains(pictureId)) {
            purchasedProfilePictures.add(pictureId);
        }
    }

    public boolean hasPurchasedProfilePicture(int pictureId) {
        return purchasedProfilePictures.contains(pictureId);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                ", coins=" + coins +
                ", profilePictureId=" + profilePictureId +
                ", purchasedProfilePictures=" + purchasedProfilePictures +
                '}';
    }
}
