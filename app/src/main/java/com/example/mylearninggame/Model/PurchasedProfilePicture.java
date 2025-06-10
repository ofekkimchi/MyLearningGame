package com.example.mylearninggame.Model;

public class PurchasedProfilePicture {
    private int pictureId;
    private int price;
    private boolean isSelected;

    public PurchasedProfilePicture(int pictureId, int price) {
        this.pictureId = pictureId;
        this.price = price;
        this.isSelected = false;
    }

    public int getPictureId() {
        return pictureId;
    }

    public int getPrice() {
        return price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
} 