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
import com.example.mylearninggame.Services.DatabaseService;
import com.example.mylearninggame.utils.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddQuestion extends AppCompatActivity {
    private EditText etWord, etRightAnswer, etWrongAnswer1, etWrongAnswer2, etWrongAnswer3;
    private Button btnSave;
    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        databaseService = DatabaseService.getInstance();

        // קישור ל-XML
        etWord = findViewById(R.id.etWord);
        etRightAnswer = findViewById(R.id.etRightAnswer);
        etWrongAnswer1 = findViewById(R.id.etWrongAnswer1);
        etWrongAnswer2 = findViewById(R.id.etWrongAnswer2);
        etWrongAnswer3 = findViewById(R.id.etWrongAnswer3);
        btnSave = findViewById(R.id.btnAddQuestion);


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
        /* if(word.contains())
        {
            Toast.makeText(this, "אי אפשר להוסיף מספרים למילה", Toast.LENGTH_SHORT).show();
            return;
        }*/

        String id = databaseService.generateNewQuestionId();
        Question newQuestion = new Question(id, word, rightAnswer, wrong1, wrong2, wrong3);

        databaseService.createNewQuestion(newQuestion, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(AddQuestion.this, "השאלה נוספה בהצלחה!", Toast.LENGTH_SHORT).show();

                // חזרה לרשימת השאלות
                Intent intent = new Intent(AddQuestion.this, Level.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }
}