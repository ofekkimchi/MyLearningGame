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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private AuthenticationService authenticationService;
    private DatabaseService databaseService;
    Button btnSignOut, btnLevels, btnAddQuestion, btnLevel,button;
    boolean isAdmin;
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
        databaseService=DatabaseService.getInstance();
        initviews();

        if (!authenticationService.isUserSignedIn()) {
            Log.d(TAG, "User not signed in, redirecting to LandingActivity");
            Intent landingIntent = new Intent(MainActivity.this, Landing.class);
            startActivity(landingIntent);
            finish();
            return;
        }
        databaseService.getUser(authenticationService.getCurrentUserUid(), new DatabaseService.DatabaseCallback<User>() {
                @Override
                public void onCompleted(User user) {
                    if (user == null){
                        // should not ever happened (only when clearing DB)
                        authenticationService.signOut();
                        return;
                    }
                    SharedPreferencesUtil.saveUser(getApplicationContext(), user);
                    // update view
                }

                @Override
                public void onFailed(Exception e) {
                    Log.w(TAG, "getUser:failure", e);
                    Toast.makeText(getApplicationContext(), "Could not get user",
                            Toast.LENGTH_SHORT).show();
                }
            }
        );

        currentUser = SharedPreferencesUtil.getUser(this);
        Log.i(TAG, currentUser.toString());
        // update view
        assert currentUser != null;
        isAdmin =currentUser.getIsAdmin();
        if(!isAdmin){
            btnAddQuestion.setVisibility(View.GONE);
            btnLevel.setVisibility(View.GONE);
        }

    }

    private void initviews() {
        btnSignOut=findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(this);
        btnLevels=findViewById(R.id.btnLevels);
        btnLevels.setOnClickListener(this);
        btnAddQuestion=findViewById(R.id.btnAddQuestion);
        btnAddQuestion.setOnClickListener(this);
        btnLevel=findViewById(R.id.btnLevel);
        btnLevel.setOnClickListener(this);
        button=findViewById(R.id.button);
        button.setOnClickListener(this);

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
        if (view==btnAddQuestion && !isAdmin){
            Toast.makeText(this, "You are not an admin", Toast.LENGTH_SHORT).show();
        }
        if (view==btnAddQuestion && isAdmin) {
            Intent intent = new Intent(getApplicationContext(), AddQuestion.class);
            startActivity(intent);
        }
        if (view == btnLevel) {
            Intent intent = new Intent(getApplicationContext(), Level.class);
            startActivity(intent);
        }
        if (view == button) {
            Intent intent = new Intent(getApplicationContext(), UserTable.class);
            startActivity(intent);
        }
    }
}