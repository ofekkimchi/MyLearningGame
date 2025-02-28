package com.example.mylearninggame.Screens;

import static com.example.mylearninggame.Adapters.QuestionAdapter.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mylearninggame.Adapters.QuestionAdapter;
import com.example.mylearninggame.Model.Question;
import com.example.mylearninggame.R;
import com.example.mylearninggame.utils.SharedPreferencesUtil;
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

        // טעינת רשימת השאלות מ-SharedPreferences
        loadQuestionsFromPreferences();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuestion();
            }
        });
    }

    private void saveQuestion() {
        String word = etWord.getText().toString().trim();
        String rightAnswer = etRightAnswer.getText().toString().trim();
        String wrong1 = etWrongAnswer1.getText().toString().trim();
        String wrong2 = etWrongAnswer2.getText().toString().trim();
        String wrong3 = etWrongAnswer3.getText().toString().trim();

        if (word.isEmpty() || rightAnswer.isEmpty() || wrong1.isEmpty() || wrong2.isEmpty() || wrong3.isEmpty()) {
            Toast.makeText(this, "אנא מלא את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        }

        Question newQuestion = new Question(null, word, rightAnswer, wrong1, wrong2, wrong3, 0);
        questionsList.add(newQuestion);

        // שמירת הרשימה ב-SharedPreferences
        saveQuestionsToPreferences();

        Toast.makeText(this, "השאלה נוספה בהצלחה!", Toast.LENGTH_SHORT).show();

        // חזרה לרשימת השאלות
        Intent intent = new Intent(AddQuestion.this, Level.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // לוודא שהמסך מתעדכן
        startActivity(intent);
        finish();
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

    private void saveQuestionsToPreferences() {
        SharedPreferencesUtil.saveQuestions(this, questionsList);
        Log.d("AddQuestion", "Saved questions count: " + questionsList.size());
    }

}