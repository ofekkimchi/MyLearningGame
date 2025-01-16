package com.example.mylearninggame.Model;

import java.util.ArrayList;

public class Level {
    protected String id;
    protected ArrayList<Question> questions;
    protected int score;

    public Level(String id, ArrayList<Question> questions, int score) {
        this.id = id;
        this.questions = questions;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", questions=" + questions +
                ", score=" + score +
                '}';
    }
}
