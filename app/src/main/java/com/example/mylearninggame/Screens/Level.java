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
import com.google.firebase.database.annotations.Nullable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Level extends AppCompatActivity {
    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;
    private ArrayList<Question> questionsList;
    private Button btnAddQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        recyclerView = findViewById(R.id.recyclerView);
        btnAddQuestion = findViewById(R.id.btnAddNewQuestion);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // טעינת השאלות מה-SharedPreferences
        loadQuestionsFromPreferences();

        questionAdapter = new QuestionAdapter(this, questionsList);
        recyclerView.setAdapter(questionAdapter);

        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Level.this, AddQuestion.class);
                startActivity(intent);
            }
        });
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
        Log.d("Level", "Loaded questions count: " + questionsList.size());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadQuestionsFromPreferences(); // טוען מחדש את הרשימה מ-SharedPreferences
        questionAdapter.updateQuestionsList(questionsList); // מעדכן את הרשימה באדפטר
    }
}

