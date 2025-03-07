package com.example.mylearninggame.Screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylearninggame.Adapters.QuestionAdapter;
import com.example.mylearninggame.Model.Question;
import com.example.mylearninggame.R;
import com.example.mylearninggame.Services.DatabaseService;
import com.google.firebase.database.annotations.Nullable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Level extends AppCompatActivity {
    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;
    private Button btnAddQuestion;

    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        databaseService = DatabaseService.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        btnAddQuestion = findViewById(R.id.btnAddNewQuestion);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadQuestions();

        questionAdapter = new QuestionAdapter(new QuestionAdapter.QuestionClickListener() {
            @Override
            public void onQuestionClick(Question question) {

            }

            @Override
            public void onLongQuestionClick(Question question) {

            }
        });
        recyclerView.setAdapter(questionAdapter);

        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Level.this, AddQuestion.class);
                startActivity(intent);
            }
        });
    }
    private void loadQuestions() {
        databaseService.getQuestions(new DatabaseService.DatabaseCallback<List<Question>>() {
            @Override
            public void onCompleted(List<Question> questions) {
                questionAdapter.setQuestionList(questions);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadQuestions(); // טוען מחדש את הרשימה מ-SharedPreferences
    }
}

