package com.example.mylearninggame.Screens;

import android.content.Intent;
import android.os.Bundle;
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

        // יצירת אובייקט שאלה חדש
        Question newQuestion = new Question(null, word, rightAnswer, wrong1, wrong2, wrong3, 0);
        questionsList.add(newQuestion);

        // חזרה למסך השאלות עם הרשימה המעודכנת
        Intent intent = new Intent(AddQuestion.this, Level.class);
        intent.putExtra("questions", questionsList); // העברת הרשימה
        startActivity(intent);
    }
}