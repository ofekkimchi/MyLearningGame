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

import com.example.mylearninggame.R;

public class Landing extends AppCompatActivity implements View.OnClickListener {
    Button btnLoginLanding, btnSignUpLanding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initviews();
    }

    private void initviews() {
        btnSignUpLanding = findViewById(R.id.btnSignUpLanding);
        btnSignUpLanding.setOnClickListener(this);
        btnLoginLanding = findViewById(R.id.btnLogInLanding);
        btnLoginLanding.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view == btnSignUpLanding) {
            Intent signup = new Intent(getApplicationContext(), Singup.class);
            startActivity(signup);
        }
        if (view == btnLoginLanding) {
            Intent login = new Intent(getApplicationContext(), Login.class);
            startActivity(login);
        }
    }
}


