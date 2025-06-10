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
import android.widget.EditText;
import android.widget.Toast;

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

/**
 * Admin interface for managing Level 2 questions
 * Allows administrators to add, edit, and delete questions for Level 2
 */
public class Level2Admin extends AppCompatActivity {
    // UI Components
    private EditText etWord, etRightAnswer, etWrongAnswer1, etWrongAnswer2, etWrongAnswer3;
    private Button btnAddQuestion, btnBack;
    // Database service for managing questions
    private DatabaseService databaseService;
    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;
    User currentUser;

    /**
     * Initializes the activity and sets up the UI components
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_level2_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        currentUser = SharedPreferencesUtil.getUser(this);

        databaseService = DatabaseService.getInstance();
        initializeViews();
        setupClickListeners();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        questionAdapter = new QuestionAdapter(new QuestionAdapter.QuestionClickListener() {
            @Override
            public void onQuestionClick(Question question) {
                // Handle question click
            }

            @Override
            public void onLongQuestionClick(Question question) {
                // Handle long click
            }
        });
        recyclerView.setAdapter(questionAdapter);

        if (currentUser != null && currentUser.getIsAdmin()) {
            enableSwipeToDelete(recyclerView, questionAdapter);
        }
    }

    /**
     * Initializes all UI components
     */
    private void initializeViews() {
        etWord = findViewById(R.id.etWord);
        etRightAnswer = findViewById(R.id.etRightAnswer);
        etWrongAnswer1 = findViewById(R.id.etWrongAnswer1);
        etWrongAnswer2 = findViewById(R.id.etWrongAnswer2);
        etWrongAnswer3 = findViewById(R.id.etWrongAnswer3);
        btnAddQuestion = findViewById(R.id.btnAddQuestion);
        btnBack = findViewById(R.id.btnBack);
    }

    /**
     * Sets up click listeners for buttons
     */
    private void setupClickListeners() {
        btnAddQuestion.setOnClickListener(v -> addQuestion());
        btnBack.setOnClickListener(v -> finish());
    }

    /**
     * Adds a new question to Level 2
     * Validates input and saves to database
     */
    private void addQuestion() {
        String word = etWord.getText().toString().trim();
        String rightAnswer = etRightAnswer.getText().toString().trim();
        String wrongAnswer1 = etWrongAnswer1.getText().toString().trim();
        String wrongAnswer2 = etWrongAnswer2.getText().toString().trim();
        String wrongAnswer3 = etWrongAnswer3.getText().toString().trim();

        if (validateInput(word, rightAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3)) {
            Question question = new Question(null, word, rightAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, 2);
            databaseService.createNewQuestion(question, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {
                    runOnUiThread(() -> {
                        Toast.makeText(Level2Admin.this, "Question added successfully!", Toast.LENGTH_SHORT).show();
                        clearInputs();
                    });
                }

                @Override
                public void onFailed(Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(Level2Admin.this, "Failed to add question: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }

    /**
     * Validates all input fields
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validateInput(String word, String rightAnswer, String wrongAnswer1, String wrongAnswer2, String wrongAnswer3) {
        if (word.isEmpty() || rightAnswer.isEmpty() || wrongAnswer1.isEmpty() || wrongAnswer2.isEmpty() || wrongAnswer3.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Clears all input fields after successful question addition
     */
    private void clearInputs() {
        etWord.setText("");
        etRightAnswer.setText("");
        etWrongAnswer1.setText("");
        etWrongAnswer2.setText("");
        etWrongAnswer3.setText("");
    }

    public void enableSwipeToDelete(RecyclerView recyclerView, QuestionAdapter adapter) {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private final Drawable deleteIcon = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.delete_icon);
            private final ColorDrawable background = new ColorDrawable(Color.RED);

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Question question = adapter.getByPosition(position);
                DatabaseService.getInstance().deleteQuestion(question.getId(), new DatabaseService.DatabaseCallback<Void>() {
                    @Override
                    public void onCompleted(Void object) {
                        adapter.removeItem(position);
                    }

                    @Override
                    public void onFailed(Exception e) {

                    }
                });
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20;

                if (dX < 0) {
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