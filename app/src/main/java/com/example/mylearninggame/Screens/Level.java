package com.example.mylearninggame.Screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylearninggame.Adapters.QuestionAdapter;
import com.example.mylearninggame.Model.Question;
import com.example.mylearninggame.Model.User;
import com.example.mylearninggame.R;
import com.example.mylearninggame.Services.DatabaseService;
import com.example.mylearninggame.utils.SharedPreferencesUtil;
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
    User currentUser;


    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        currentUser = SharedPreferencesUtil.getUser(this);

        databaseService = DatabaseService.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        btnAddQuestion = findViewById(R.id.btnAddNewQuestion);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadQuestions();

        questionAdapter = new QuestionAdapter(new QuestionAdapter.QuestionClickListener() {
            @Override
            public void onQuestionClick(Question question) {
                Intent intent = new Intent(Level.this, AddQuestion.class);
                intent.putExtra("question", question);
                startActivity(intent);
            }

            @Override
            public void onLongQuestionClick(Question question) {

            }
        });
        recyclerView.setAdapter(questionAdapter);
        if (currentUser.getIsAdmin()){
            enableSwipeToDelete(recyclerView, questionAdapter);
        }


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


    public void enableSwipeToDelete(RecyclerView recyclerView, QuestionAdapter adapter) {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            // TODO replace icon
            private final Drawable deleteIcon = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.delete_icon);
            private final ColorDrawable background = new ColorDrawable(Color.RED);

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // We don't need drag & drop
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                adapter.removeItem(position);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20;

                if (dX < 0) { // Swipe left
                    background.setBounds(itemView.getRight() + (int) dX - backgroundCornerOffset, itemView.getTop(),
                            itemView.getRight(), itemView.getBottom());
                    background.draw(c);

                    if (deleteIcon != null) {
                        int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                        int iconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                        int iconLeft = itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
                        int iconRight = itemView.getRight() - iconMargin;
                        int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();

                        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        deleteIcon.draw(c);
                    }
                }
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }

}

