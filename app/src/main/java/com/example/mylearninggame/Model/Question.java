package com.example.mylearninggame.Model;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {

    protected String id ;
    protected String word;
    protected String rightAnswer;
    protected String wrongAnswer1;
    protected String wrongAnswer2;
    protected String wrongAnswer3;

    public Question() {
    }

    public Question(String id, String word, String rightAnswer, String wrongAnswer1, String wrongAnswer2, String wrongAnswer3) {
        this.id = id;
        this.word = word;
        this.rightAnswer = rightAnswer;
        this.wrongAnswer1 = wrongAnswer1;
        this.wrongAnswer2 = wrongAnswer2;
        this.wrongAnswer3 = wrongAnswer3;
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
        return rightAnswer;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public String getWrongAnswer1() {
        return wrongAnswer1;
    }

    public void setWrongAnswer1(String wrongAnswer1) {
        this.wrongAnswer1 = wrongAnswer1;
    }

    public String getWrongAnswer2() {
        return wrongAnswer2;
    }

    public void setWrongAnswer2(String wrongAnswer2) {
        this.wrongAnswer2 = wrongAnswer2;
    }

    public String getWrongAnswer3() {
        return wrongAnswer3;
    }

    public void setWrongAnswer3(String wrongAnswer3) {
        this.wrongAnswer3 = wrongAnswer3;
    }

    public void setRightanswer(String rightanswer) {
        this.rightAnswer = rightanswer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", word='" + word + '\'' +
                ", rightAnswer='" + rightAnswer + '\'' +
                ", wrongAnswer1='" + wrongAnswer1 + '\'' +
                ", wrongAnswer2='" + wrongAnswer2 + '\'' +
                ", wrongAnswer3='" + wrongAnswer3 + '\'' +
                '}';
    }
}
