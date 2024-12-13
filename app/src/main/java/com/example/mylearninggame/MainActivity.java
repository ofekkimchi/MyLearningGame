package com.example.mylearninggame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSingup, btnLogIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initviews();
    }

    private void initviews() {
        btnSingup=findViewById(R.id.btnGoSingup);
        btnSingup.setOnClickListener(this);
        btnLogIn=findViewById(R.id.btnGoSingin);
        btnLogIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==btnSingup){
            Intent gosingup =new Intent(getApplicationContext(),Singup.class);
            startActivity(gosingup);
        }
        if (view==btnLogIn){
            Intent gologin =new Intent(getApplicationContext(),Login.class);
            startActivity(gologin);
        }
    }
}