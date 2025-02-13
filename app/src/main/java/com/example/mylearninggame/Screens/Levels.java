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

public class Levels extends AppCompatActivity implements View.OnClickListener {
    Button btnEnglish, btnHebrew , level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_levels);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initviews();
    }

    private void initviews() {
        btnEnglish=findViewById(R.id.btnEnglishLevels);
        btnEnglish.setOnClickListener(this);
        btnHebrew=findViewById(R.id.btnHebrewLevels);
        btnHebrew.setOnClickListener(this);
        level=findViewById(R.id.btnLevel);
        level.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnEnglish) {
            Intent intent = new Intent(getApplicationContext(), EnglishLevels.class);
            startActivity(intent);
        }
        if (view == btnHebrew) {
            Intent intent = new Intent(getApplicationContext(), HebrewLevels.class);
            startActivity(intent);
        }

    }
}