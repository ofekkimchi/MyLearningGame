package com.example.mylearninggame.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        if (this.question != null && currentUser.getIsAdmin()) {
            etWord.setText(question.getWord());
            etRightAnswer.setText(question.getRightAnswer());
            etWrongAnswer1.setText(question.getWrongAnswer1());
            etWrongAnswer2.setText(question.getWrongAnswer2());
            etWrongAnswer3.setText(question.getWrongAnswer3());
            btnSave.setText("Edit");
            tv =findViewById(R.id.tv1);
            tv.setText("Edit Question");
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


        String id;
        if (this.question == null)
            id = databaseService.generateNewQuestionId();
        else
            id = this.question.getId();

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