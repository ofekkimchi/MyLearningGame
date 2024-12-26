package com.example.mylearninggame.Model;

import java.util.List;

public class Question {
    protected String id ;


    protected String word;
    protected String rightanswer;
    protected List<String> theoptions;
    protected  int level;



    public Question(String id, String word, String rightanswer, List<String> theoptions, int level) {
        this.id = id;
        this.word = word;
        this.rightanswer = rightanswer;
        this.theoptions = theoptions;
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getRightanswer() {
        return rightanswer;
    }

    public void setRightanswer(String rightanswer) {
        this.rightanswer = rightanswer;
    }

    public List<String> getTheoptions() {
        return theoptions;
    }

    public void setTheoptions(List<String> theoptions) {
        this.theoptions = theoptions;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", word='" + word + '\'' +
                ", rightanswer='" + rightanswer + '\'' +
                ", theoptions=" + theoptions +
                ", level=" + level +
                '}';
    }
}
