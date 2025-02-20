package com.example.mylearninggame.Screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mylearninggame.Model.Question;
import com.example.mylearninggame.R;
import com.example.mylearninggame.utils.SharedPreferencesUtil;
import com.google.gson.Gson;

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

        // קבלת הרשימה מה-Intent
        if (getIntent().hasExtra("questions")) {
            questionsList = (ArrayList<Question>) getIntent().getSerializableExtra("questions");
        } else {
            questionsList = new ArrayList<>();
        }

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

        Question newQuestion = new Question(null, word, rightAnswer, wrong1, wrong2, wrong3, 0);
        questionsList.add(newQuestion);

        // שמירת הרשימה ב-SharedPreferences דרך SharedPreferencesUtil
        SharedPreferencesUtil.saveQuestions(AddQuestion.this, questionsList);

        // מעבר למסך הבא (למשל Level)
        Intent intent = new Intent(AddQuestion.this, Level.class);
        startActivity(intent);
    }

    // פונקציה לשמירת הנתונים ב-SharedPreferences
    private void saveQuestionsToPreferences() {
        SharedPreferencesUtil prefs = (SharedPreferencesUtil) PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(questionsList);
        editor.putString("questions_list", json);
        editor.apply();
    }
}