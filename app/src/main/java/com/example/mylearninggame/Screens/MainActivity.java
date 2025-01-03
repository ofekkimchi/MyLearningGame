package com.example.mylearninggame.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mylearninggame.R;
import com.example.mylearninggame.Services.AuthenticationService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private AuthenticationService authenticationService;
    Button btnSignOut, btnLevels;

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
        authenticationService = AuthenticationService.getInstance();
        initviews();


        if (!authenticationService.isUserSignedIn()) {
            Log.d(TAG, "User not signed in, redirecting to LandingActivity");
            Intent landingIntent = new Intent(MainActivity.this, Landing.class);
            startActivity(landingIntent);
            finish();
        }

    }

    private void initviews() {
        btnSignOut=findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(this);
        btnLevels=findViewById(R.id.btnLevels);
        btnLevels.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view == btnSignOut) {
            AuthenticationService.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Landing.class);
            startActivity(intent);
        }
        if (view == btnLevels) {
            Intent intent = new Intent(getApplicationContext(), Levels.class);
            startActivity(intent);
        }
    }
}