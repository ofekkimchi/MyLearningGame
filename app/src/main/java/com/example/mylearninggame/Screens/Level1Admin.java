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

/**
 * Admin interface for managing Level 1 questions
 * Allows viewing, adding, editing, and deleting questions for Level 1
 */
public class Level1Admin extends AppCompatActivity {
    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;
    private Button btnAddQuestion;
    User currentUser;
    DatabaseService databaseService;

    /**
     * Initializes the activity and sets up the UI
     * Configures the RecyclerView and loads questions
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level1_admin);

        currentUser = SharedPreferencesUtil.getUser(this);
        databaseService = DatabaseService.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        btnAddQuestion = findViewById(R.id.btnAddNewQuestion);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadQuestions();

        // Set up question adapter with click handlers
        questionAdapter = new QuestionAdapter(new QuestionAdapter.QuestionClickListener() {
            @Override
            public void onQuestionClick(Question question) {
                Intent intent = new Intent(Level1Admin.this, AddQuestion.class);
                intent.putExtra("question", question);
                startActivity(intent);
            }

            @Override
            public void onLongQuestionClick(Question question) {
                // Long click functionality not implemented
            }
        });
        recyclerView.setAdapter(questionAdapter);
        
        // Enable swipe-to-delete for admin users
        if (currentUser.getIsAdmin()) {
            enableSwipeToDelete(recyclerView, questionAdapter);
        }

        // Set up add question button
        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Level1Admin.this, AddQuestion.class);
                intent.putExtra("levelNumber", 1);
                startActivity(intent);
            }
        });
    }

    /**
     * Loads questions from the database and filters for Level 1
     * Updates the RecyclerView with the filtered questions
     */
    private void loadQuestions() {
        databaseService.getQuestions(new DatabaseService.DatabaseCallback<List<Question>>() {
            @Override
            public void onCompleted(List<Question> questions) {
                // Filter only level 1 questions
                List<Question> level1Questions = new ArrayList<>();
                for (Question question : questions) {
                    if (question.getLevel() == 1) {
                        level1Questions.add(question);
                    }
                }
                questionAdapter.setQuestionList(level1Questions);
            }

            @Override
            public void onFailed(Exception e) {
                // Error handling not implemented
            }
        });
    }

    /**
     * Reloads questions when the activity resumes
     * Ensures the list is up to date after returning from other screens
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadQuestions();
    }

    /**
     * Enables swipe-to-delete functionality for questions
     * Allows admin users to delete questions by swiping left
     * @param recyclerView The RecyclerView to enable swipe on
     * @param adapter The adapter containing the questions
     */
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
                        // Error handling not implemented
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