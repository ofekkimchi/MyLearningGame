package com.example.mylearninggame.Screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.content.ContextCompat;
import android.content.res.ColorStateList;

import com.example.mylearninggame.Model.User;
import com.example.mylearninggame.R;
import com.example.mylearninggame.Services.DatabaseService;
import com.example.mylearninggame.utils.SharedPreferencesUtil;
import com.example.mylearninggame.Model.Question;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.graphics.Color;

/**
 * Level 2 game screen implementation
 * Handles the quiz gameplay mechanics including questions, timer, and scoring
 * Similar to Level 1 but with different questions and potentially different difficulty
 */
public class Level2 extends AppCompatActivity {
    // UI Components
    private TextView timerText;
    private TextView wordToTranslate;
    private Button[] answerButtons;
    private ImageView[] hearts;
    // Game state variables
    private CountDownTimer timer;
    private int remainingHearts = 3;
    private int currentQuestionIndex = 0;
    private List<Question> questions;
    private DatabaseService databaseService;
    private User currentUser;
    private int levelNumber = 2; // This is Level 2

    /**
     * Initializes the activity and sets up the game
     * Configures UI and loads questions
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_level2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseService = DatabaseService.getInstance();
        currentUser = SharedPreferencesUtil.getUser(this);

        initializeViews();
        setupQuestions();
    }

    /**
     * Initializes all UI components and game elements
     */
    private void initializeViews() {
        timerText = findViewById(R.id.timerText);
        wordToTranslate = findViewById(R.id.wordToTranslate);

        // Initialize answer buttons
        answerButtons = new Button[4];
        answerButtons[0] = findViewById(R.id.answer1);
        answerButtons[1] = findViewById(R.id.answer2);
        answerButtons[2] = findViewById(R.id.answer3);
        answerButtons[3] = findViewById(R.id.answer4);

        // Initialize hearts
        hearts = new ImageView[3];
        hearts[0] = findViewById(R.id.heart1);
        hearts[1] = findViewById(R.id.heart2);
        hearts[2] = findViewById(R.id.heart3);
    }

    /**
     * Loads questions from the database and filters for level 2
     * Shuffles questions and starts the game
     */
    private void setupQuestions() {
        questions = new ArrayList<>();
        databaseService.getQuestions(new DatabaseService.DatabaseCallback<List<Question>>() {
            @Override
            public void onCompleted(List<Question> allQuestions) {
                // Filter only level 2 questions
                for (Question question : allQuestions) {
                    if (question.getLevel() == 2) {
                        questions.add(question);
                    }
                }
                Log.d("Level2", "Loaded " + questions.size() + " questions for level 2.");
                Collections.shuffle(questions); // Shuffle questions
                startQuestion(); // Start the game after loading questions
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(Level2.this, "Error loading questions: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Starts a new question or shows level completion dialog
     * Sets up the question display and answer buttons
     */
    private void startQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            // Level completed successfully
            int earnedCoins = calculateCoins();
            updateUserCoins(earnedCoins);
            showLevelCompletedDialog(earnedCoins);
            return;
        }

        // Re-enable all buttons and reset their colors at the start of a new question
        for (Button button : answerButtons) {
            button.setEnabled(true);
            ((MaterialButton) button).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4A90E2")));
        }

        Question currentQuestion = questions.get(currentQuestionIndex);
        wordToTranslate.setText(currentQuestion.getWord());

        // Create a list of all answers (right and wrong) and shuffle them
        List<String> answers = new ArrayList<>();
        answers.add(currentQuestion.getRightAnswer());
        answers.add(currentQuestion.getWrongAnswer1());
        answers.add(currentQuestion.getWrongAnswer2());
        answers.add(currentQuestion.getWrongAnswer3());
        Collections.shuffle(answers);

        // Set answers to buttons
        for (int i = 0; i < answerButtons.length; i++) {
            final int buttonIndex = i;
            answerButtons[i].setText(answers.get(i));
            answerButtons[i].setOnClickListener(v -> checkAnswer(answers.get(buttonIndex), currentQuestion));
        }

        startTimer();
    }

    /**
     * Starts the countdown timer for the current question
     * Handles timer completion and game over conditions
     */
    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                remainingHearts--;
                updateHearts();
                if (remainingHearts <= 0) {
                    showGameOverDialog();
                } else {
                    for (Button button : answerButtons) {
                        button.setEnabled(false);
                    }
                }
            }
        }.start();
    }

    /**
     * Checks if the selected answer is correct
     * Handles correct/incorrect answer logic and game progression
     */
    private void checkAnswer(String selectedAnswer, Question question) {
        if (selectedAnswer.equals(question.getRightAnswer())) {
            timer.cancel();
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            // Set the correct answer button to its default color (blue)
            for (Button button : answerButtons) {
                if (button.getText().equals(selectedAnswer)) {
                    ((MaterialButton) button).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4A90E2")));
                    break;
                }
            }
            currentQuestionIndex++;
            startQuestion();
        } else {
            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
            // Find and disable only the clicked wrong answer button
            for (Button button : answerButtons) {
                if (button.getText().equals(selectedAnswer)) {
                    button.setEnabled(false);
                    ((MaterialButton) button).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A9A9A9"))); // Darker Gray
                    break;
                }
            }

            remainingHearts--;
            updateHearts();

            if (remainingHearts <= 0) {
                timer.cancel();
                showGameOverDialog();
            }
        }
    }

    /**
     * Updates the hearts display based on remaining lives
     */
    private void updateHearts() {
        for (int i = 0; i < hearts.length; i++) {
            hearts[i].setVisibility(i < remainingHearts ? View.VISIBLE : View.INVISIBLE);
        }
    }

    /**
     * Calculates coins earned based on remaining hearts
     * @return The number of coins earned
     */
    private int calculateCoins() {
        int baseCoins;
        switch (remainingHearts) {
            case 3:
                baseCoins = 150;
                break;
            case 2:
                baseCoins = 100;
                break;
            case 1:
                baseCoins = 75;
                break;
            default:
                baseCoins = 0;
                break;
        }
        // Adjust coins based on level number
        return baseCoins + (levelNumber - 1) * 25;
    }

    /**
     * Updates the user's coin balance in the database
     * @param earnedCoins The number of coins to add
     */
    private void updateUserCoins(int earnedCoins) {
        if (currentUser != null) {
            currentUser.setCoins(currentUser.getCoins() + earnedCoins);
            databaseService.createNewUser(currentUser, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {
                    SharedPreferencesUtil.saveUser(getApplicationContext(), currentUser);
                    Toast.makeText(Level2.this, "Coins updated!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(Level2.this, "Failed to update coins: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Shows the level completion dialog with earned coins
     * @param earnedCoins The number of coins earned
     */
    private void showLevelCompletedDialog(int earnedCoins) {
        new AlertDialog.Builder(this)
                .setTitle("Level Completed!")
                .setMessage("You earned " + earnedCoins + " coins!")
                .setPositiveButton("Go to Levels", (dialog, which) -> {
                    Intent intent = new Intent(Level2.this, Levels.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Replay Level", (dialog, which) -> resetLevel())
                .setCancelable(false)
                .show();
    }

    /**
     * Shows the game over dialog when player loses all hearts
     */
    private void showGameOverDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Game Over!")
                .setMessage("You failed this level.")
                .setPositiveButton("Go to Levels", (dialog, which) -> {
                    Intent intent = new Intent(Level2.this, Levels.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Replay Level", (dialog, which) -> resetLevel())
                .setCancelable(false)
                .show();
    }

    /**
     * Resets the level state for replay
     */
    private void resetLevel() {
        currentQuestionIndex = 0;
        remainingHearts = 3;
        updateHearts();
        setupQuestions(); // Re-load questions for replay
    }

    /**
     * Cleans up resources when the activity is destroyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
} 