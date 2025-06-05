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

public class AddQuestion extends AppCompatActivity {
    private EditText etWord, etRightAnswer, etWrongAnswer1, etWrongAnswer2, etWrongAnswer3;
    private Button btnSave;
    private TextView tv;
    private RadioGroup levelRadioGroup;
    private RadioButton radioLevel1, radioLevel2, radioLevel3;
    DatabaseService databaseService;
    User currentUser;

    @Nullable
    Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        databaseService = DatabaseService.getInstance();
        currentUser = SharedPreferencesUtil.getUser(this);

        this.question = getIntent().getSerializableExtra("question", Question.class);

        // קישור ל-XML
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

        if (this.question != null && currentUser.getIsAdmin()) {
            etWord.setText(question.getWord());
            etRightAnswer.setText(question.getRightAnswer());
            etWrongAnswer1.setText(question.getWrongAnswer1());
            etWrongAnswer2.setText(question.getWrongAnswer2());
            etWrongAnswer3.setText(question.getWrongAnswer3());
            btnSave.setText("Edit");
            tv =findViewById(R.id.tv1);
            tv.setText("Edit Question");
            // Set selected level radio button
            int level = question.getLevel();
            if (level == 1) {
                radioLevel1.setChecked(true);
            } else if (level == 2) {
                radioLevel2.setChecked(true);
            } else if (level == 3) {
                radioLevel3.setChecked(true);
            }
        }

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
        String regex = "^[a-zA-Zא-ת\\s]+$";
        if (!word.matches(regex) || !rightAnswer.matches(regex) || !wrong1.matches(regex)
                || !wrong2.matches(regex) || !wrong3.matches(regex)) {
            Toast.makeText(this, "מותר להשתמש רק באותיות. אין מספרים או תווים מיוחדים.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected level
        int selectedLevel = 1; // Default to level 1
        int selectedRadioButtonId = levelRadioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId == R.id.radioLevel2) {
            selectedLevel = 2;
        } else if (selectedRadioButtonId == R.id.radioLevel3) {
            selectedLevel = 3;
        }

        String id;
        if (this.question == null) {
            id = databaseService.generateNewQuestionId();
        } else {
            id = this.question.getId();
        }

        Question newQuestion = new Question(id, word, rightAnswer, wrong1, wrong2, wrong3, selectedLevel);

        int finalSelectedLevel = selectedLevel;
        databaseService.createNewQuestion(newQuestion, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(AddQuestion.this, "השאלה נוספה בהצלחה!", Toast.LENGTH_SHORT).show();

                // Redirect to the appropriate admin page based on the selected level
                Intent intent;
                if (finalSelectedLevel == 1) {
                    intent = new Intent(AddQuestion.this, Level1Admin.class);
                } else if (finalSelectedLevel == 2) {
                    intent = new Intent(AddQuestion.this, Level2Admin.class);
                } else if (finalSelectedLevel == 3) {
                    intent = new Intent(AddQuestion.this, Level3Admin.class);
                } else {
                    intent = new Intent(AddQuestion.this, Level1Admin.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(AddQuestion.this, "Failed to save question: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}