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
    private Button btnAddQuestion;
    private ArrayList<Question> questionsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_question);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etWord = findViewById(R.id.etWord);
        etRightAnswer = findViewById(R.id.etRightAnswer);
        etWrongAnswer1 = findViewById(R.id.etWrongAnswer1);
        etWrongAnswer2 = findViewById(R.id.etWrongAnswer2);
        etWrongAnswer3 = findViewById(R.id.etWrongAnswer3);
        btnAddQuestion = findViewById(R.id.btnAddQuestion);

        // קבלת הרשימה הקיימת של השאלות אם הועברה דרך Intent
        if (getIntent().hasExtra("questions")) {
            questionsList = (ArrayList<Question>) getIntent().getSerializableExtra("questions");
        }
        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuestion();
            }
        });
    }

    private void saveQuestion() {
        // קבלת הנתונים מהשדות
        String word = etWord.getText().toString().trim();
        String rightAnswer = etRightAnswer.getText().toString().trim();
        String wrongAnswer1 = etWrongAnswer1.getText().toString().trim();
        String wrongAnswer2 = etWrongAnswer2.getText().toString().trim();
        String wrongAnswer3 = etWrongAnswer3.getText().toString().trim();

        // בדיקה שכל השדות מלאים
        if (word.isEmpty() || rightAnswer.isEmpty() || wrongAnswer1.isEmpty() || wrongAnswer2.isEmpty() || wrongAnswer3.isEmpty()) {
            Toast.makeText(this, "יש למלא את כל השדות!", Toast.LENGTH_SHORT).show();
            return;
        }

        // יצירת אובייקט חדש של שאלה
        Question newQuestion = new Question(word, rightAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3);
        questionsList.add(newQuestion);

        // מעבר לדף הצגת השאלות עם הרשימה המעודכנת
        Intent intent = new Intent(AddQuestion.this, Levels.class);
        intent.putExtra("questions", questionsList);
        startActivity(intent);
        finish(); // סגירת הפעילות הנוכחית כדי למנוע חזרה אחורה
    }
}