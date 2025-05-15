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

public class AdminPage extends AppCompatActivity implements View.OnClickListener {
    Button btnUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initviews();
    }

    private void initviews() {
        btnUT=findViewById(R.id.btnUT);
        btnUT.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnUT) {
            Intent intent = new Intent(getApplicationContext(), UserTable.class);
            startActivity(intent);
        }

    }
}