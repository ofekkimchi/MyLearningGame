package com.example.mylearninggame.Screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mylearninggame.Model.Question;
import com.example.mylearninggame.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AddQuestion extends AppCompatActivity {
    private EditText etWord, etRightAnswer, etWrongAnswer1, etWrongAnswer2, etWrongAnswer3;
    private Button btnSave;
    private ArrayList<Question> questionsList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        // קישור ל-XML
        etWord = findViewById(R.id.etWord);
        etRightAnswer = findViewById(R.id.etRightAnswer);
        etWrongAnswer1 = findViewById(R.id.etWrongAnswer1);
        etWrongAnswer2 = findViewById(R.id.etWrongAnswer2);
        etWrongAnswer3 = findViewById(R.id.etWrongAnswer3);
        btnSave = findViewById(R.id.btnAddQuestion);

        // טעינת רשימת השאלות מה-SharedPreferences
        loadQuestionsFromPreferences();

        // לחיצה על כפתור שמירה
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuestion();
            }
        });
    }

    private void saveQuestion() {
        String word = etWord.getText().toString();
        String rightAnswer = etRightAnswer.getText().toString();
        String wrong1 = etWrongAnswer1.getText().toString();
        String wrong2 = etWrongAnswer2.getText().toString();
        String wrong3 = etWrongAnswer3.getText().toString();

        // בדיקה אם כל השדות מלאים
        if (word.isEmpty() || rightAnswer.isEmpty() || wrong1.isEmpty() || wrong2.isEmpty() || wrong3.isEmpty()) {
            Log.d("DEBUG", "נא למלא את כל השדות");
            return;
        }

        // יצירת שאלה חדשה והוספתה לרשימה
        Question newQuestion = new Question(null, word, rightAnswer, wrong1, wrong2, wrong3, 0);
        questionsList.add(newQuestion);

        // שמירת הרשימה ב-SharedPreferences
        saveQuestionsToPreferences();

        // הדפסת לוג לבדיקה
        Log.d("DEBUG", "Saved question: " + newQuestion.toString());

        // מעבר למסך Level
        Intent intent = new Intent(AddQuestion.this, Level.class);
        startActivity(intent);
        finish();
    }

    private void saveQuestionsToPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(questionsList);
        editor.putString("questions_list", json);
        editor.apply();
    }

    private void loadQuestionsFromPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString("questions_list", null);

        Type type = new TypeToken<ArrayList<Question>>() {}.getType();
        questionsList = gson.fromJson(json, type);

        if (questionsList == null) {
            questionsList = new ArrayList<>();
        }
    }


}