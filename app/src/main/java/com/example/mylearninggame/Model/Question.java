package com.example.mylearninggame.Model;

public class Question {
    protected String words, answer, id ;

    public Question(String words, String answer, String id) {
        this.words = words;
        this.answer = answer;
        this.id = id;
    }

    public Question() {
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
