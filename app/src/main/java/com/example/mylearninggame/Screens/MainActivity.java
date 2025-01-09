package com.example.mylearninggame.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mylearninggame.Model.User;
import com.example.mylearninggame.R;
import com.example.mylearninggame.Services.AuthenticationService;
import com.example.mylearninggame.Services.DatabaseService;
import com.example.mylearninggame.utils.SharedPreferencesUtil;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private AuthenticationService authenticationService;
    private DatabaseService databaseService;
    Button btnSignOut, btnLevels, btnAddQuestion;
    boolean IsAdmin;
    User currentUser;



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

        databaseService.getUser(authenticationService.getCurrentUser(), (user){
            SharedPreferencesUtil.saveUser(MainActivity.this, user);
            currentUser = user;
            // call again update view
        });

        currentUser = SharedPreferencesUtil.getUser(this);
        // update view


    }

    private void initviews() {
        btnSignOut=findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(this);
        btnLevels=findViewById(R.id.btnLevels);
        btnLevels.setOnClickListener(this);
        btnAddQuestion=findViewById(R.id.btnAddQuestion);
        btnAddQuestion.setOnClickListener(this);

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
        if (view==btnAddQuestion && IsAdmin==false){
            Toast.makeText(this, "You are not an admin", Toast.LENGTH_SHORT).show();
        } else if (view==btnAddQuestion && IsAdmin==true) {
            Intent intent = new Intent(getApplicationContext(), AddQuestion.class);
            startActivity(intent);
        }
    }
}