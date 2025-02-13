package com.example.mylearninggame.Screens;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

public class Level extends AppCompatActivity {
    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;
    private ArrayList<Question> questionsList;
    private Button btnAddNewQuestion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        recyclerView = findViewById(R.id.recyclerView);
        btnAddNewQuestion = findViewById(R.id.btnAddNewQuestion);

        // קבלת רשימת השאלות שהגיעה מה-Intent
        if (getIntent().hasExtra("questions")) {
            questionsList = (ArrayList<Question>) getIntent().getSerializableExtra("questions");
        } else {
            questionsList = new ArrayList<>();
        }

        // הגדרת ה-RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionAdapter = new QuestionAdapter(questionsList);
        recyclerView.setAdapter(questionAdapter);

        // כפתור להוספת שאלה חדשה
        btnAddNewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Level.this, AddQuestion.class);
                intent.putExtra("questions", questionsList);
                startActivity(intent);
            }
        });
    }
}
