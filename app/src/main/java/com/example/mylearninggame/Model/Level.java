package com.example.mylearninggame.Model;

public class Level {

    protected int number, coins;

    protected String id;

    public Level(int number, int coins, String id) {
        this.number = number;
        this.coins = coins;
        this.id = id;
    }

    public Level() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
