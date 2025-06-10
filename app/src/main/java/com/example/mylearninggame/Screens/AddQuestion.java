package com.example.mylearninggame.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mylearninggame.Model.Question;
import com.example.mylearninggame.Model.User;
import com.example.mylearninggame.R;
import com.example.mylearninggame.Services.DatabaseService;
import com.example.mylearninggame.utils.SharedPreferencesUtil;

/**
 * Activity for adding or editing questions in the game
 * Allows admins to create new questions or modify existing ones
 */
public class AddQuestion extends AppCompatActivity {
    // UI Components
    private EditText etWord, etRightAnswer, etWrongAnswer1, etWrongAnswer2, etWrongAnswer3;
    private Button btnSave;
    private TextView tv;
    private RadioGroup levelRadioGroup;
    private RadioButton radioLevel1, radioLevel2, radioLevel3;
    DatabaseService databaseService;
    User currentUser;

    @Nullable
    Question question;

    /**
     * Initializes the activity and sets up the UI components
     * Handles both new question creation and question editing
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        // Initialize services and get current user
        databaseService = DatabaseService.getInstance();
        currentUser = SharedPreferencesUtil.getUser(this);

        // Get question data if editing an existing question
        this.question = getIntent().getSerializableExtra("question", Question.class);
        int levelNumberFromIntent = getIntent().getIntExtra("levelNumber", -1);

        // Initialize UI components
        initializeViews();

        // If editing an existing question, populate the fields
        if (this.question != null && currentUser.getIsAdmin()) {
            populateFieldsForEditing();
        } 
        // If adding a new question from a specific level, set the level radio button
        else if (levelNumberFromIntent != -1) {
            setLevelRadioButton(levelNumberFromIntent);
        }

        // Set up save button click listener
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuestion();
            }
        });
    }

    /**
     * Initializes all UI components by finding their views
     */
    private void initializeViews() {
        etWord = findViewById(R.id.etWord);
        etRightAnswer = findViewById(R.id.etRightAnswer);
        etWrongAnswer1 = findViewById(R.id.etWrongAnswer1);
        etWrongAnswer2 = findViewById(R.id.etWrongAnswer2);
        etWrongAnswer3 = findViewById(R.id.etWrongAnswer3);
        btnSave = findViewById(R.id.btnAddQuestion);
        levelRadioGroup = findViewById(R.id.levelRadioGroup);
        radioLevel1 = findViewById(R.id.radioLevel1);
        radioLevel2 = findViewById(R.id.radioLevel2);
        radioLevel3 = findViewById(R.id.radioLevel3);
    }

    /**
     * Populates the form fields with data from an existing question
     * Used when editing a question
     */
    private void populateFieldsForEditing() {
        etWord.setText(question.getWord());
        etRightAnswer.setText(question.getRightAnswer());
        etWrongAnswer1.setText(question.getWrongAnswer1());
        etWrongAnswer2.setText(question.getWrongAnswer2());
        etWrongAnswer3.setText(question.getWrongAnswer3());
        btnSave.setText("Edit");
        tv = findViewById(R.id.tv1);
        tv.setText("Edit Question");
        
        // Set the appropriate level radio button
        int level = question.getLevel();
        if (level == 1) {
            radioLevel1.setChecked(true);
        } else if (level == 2) {
            radioLevel2.setChecked(true);
        } else if (level == 3) {
            radioLevel3.setChecked(true);
        }
    }

    /**
     * Sets up the level radio buttons when adding a new question from a specific level
     * @param levelNumber The level number (1, 2, or 3)
     */
    private void setLevelRadioButton(int levelNumber) {
        // Disable all radio buttons first
        radioLevel1.setEnabled(false);
        radioLevel2.setEnabled(false);
        radioLevel3.setEnabled(false);

        // Enable and check only the relevant one
        if (levelNumber == 1) {
            radioLevel1.setEnabled(true);
            radioLevel1.setChecked(true);
        } else if (levelNumber == 2) {
            radioLevel2.setEnabled(true);
            radioLevel2.setChecked(true);
        } else if (levelNumber == 3) {
            radioLevel3.setEnabled(true);
            radioLevel3.setChecked(true);
        }
    }

    /**
     * Validates and saves the question data
     * Handles both new question creation and question updates
     */
    private void saveQuestion() {
        // Get input values
        String word = etWord.getText().toString().trim();
        String rightAnswer = etRightAnswer.getText().toString().trim();
        String wrong1 = etWrongAnswer1.getText().toString().trim();
        String wrong2 = etWrongAnswer2.getText().toString().trim();
        String wrong3 = etWrongAnswer3.getText().toString().trim();

        // Validate that all fields are filled
        if (word.isEmpty() || rightAnswer.isEmpty() || wrong1.isEmpty() || wrong2.isEmpty() || wrong3.isEmpty()) {
            Toast.makeText(this, "אנא מלא את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate that only letters are used (no numbers or special characters)
        String regex = "^[a-zA-Zא-ת\\s]+$";
        if (!word.matches(regex) || !rightAnswer.matches(regex) || !wrong1.matches(regex)
                || !wrong2.matches(regex) || !wrong3.matches(regex)) {
            Toast.makeText(this, "מותר להשתמש רק באותיות. אין מספרים או תווים מיוחדים.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Determine the selected level
        int selectedLevel = determineSelectedLevel();

        // Generate or use existing question ID
        String id = (this.question == null) ? databaseService.generateNewQuestionId() : this.question.getId();

        // Create new question object
        Question newQuestion = new Question(id, word, rightAnswer, wrong1, wrong2, wrong3, selectedLevel);

        // Save the question to the database
        saveQuestionToDatabase(newQuestion, selectedLevel);
    }

    /**
     * Determines which level is selected for the question
     * @return The selected level number (1, 2, or 3)
     */
    private int determineSelectedLevel() {
        if (this.question != null) {
            return this.question.getLevel();
        }
        
        int selectedRadioButtonId = levelRadioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId == R.id.radioLevel2) {
            return 2;
        } else if (selectedRadioButtonId == R.id.radioLevel3) {
            return 3;
        }
        return 1; // Default to level 1
    }

    /**
     * Saves the question to the database and handles the response
     * @param newQuestion The question to save
     * @param selectedLevel The level the question belongs to
     */
    private void saveQuestionToDatabase(Question newQuestion, int selectedLevel) {
        databaseService.createNewQuestion(newQuestion, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(AddQuestion.this, "השאלה נוספה בהצלחה!", Toast.LENGTH_SHORT).show();

                // Navigate to the appropriate admin page based on the level
                navigateToAdminPage(selectedLevel);
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(AddQuestion.this, "Failed to save question: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Navigates to the appropriate admin page based on the question's level
     * @param level The level number (1, 2, or 3)
     */
    private void navigateToAdminPage(int level) {
        Intent intent;
        if (level == 1) {
            intent = new Intent(AddQuestion.this, Level1Admin.class);
        } else if (level == 2) {
            intent = new Intent(AddQuestion.this, Level2Admin.class);
        } else {
            intent = new Intent(AddQuestion.this, Level3Admin.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}