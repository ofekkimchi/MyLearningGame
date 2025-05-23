package com.example.mylearninggame.Screens;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mylearninggame.Model.User;
import com.example.mylearninggame.R;
import com.example.mylearninggame.utils.SharedPreferencesUtil;

public class EditUser extends AppCompatActivity {

    User user;
    EditText editEmail, editFName, editLName,editPhone, editPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = getIntent().getSerializableExtra("user", User.class);
        if (user == null) {
            user = SharedPreferencesUtil.getUser(this);
        }
        initviews();
    }

    private void initviews() {
        editEmail=findViewById(R.id.editEmail);
        editFName=findViewById(R.id.editFName);
        editLName=findViewById(R.id.editLName);
        editPass=findViewById(R.id.editPass);
        editPhone=findViewById(R.id.editPhone);
    }

}