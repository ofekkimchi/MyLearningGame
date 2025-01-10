package com.example.mylearninggame.Screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mylearninggame.Model.User;
import com.example.mylearninggame.R;
import com.example.mylearninggame.Services.AuthenticationService;
import com.example.mylearninggame.Services.DatabaseService;
import com.example.mylearninggame.utils.SharedPreferencesUtil;

public class Login extends AppCompatActivity implements View.OnClickListener {
    Button btnlog, btnBack;
    EditText etemail, etpass;

    String email, pass;
    AuthenticationService authenticationService;
    DatabaseService databaseService;

    public Login() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseService=DatabaseService.getInstance();
        authenticationService=AuthenticationService.getInstance();

        etemail = findViewById(R.id.etLoginEmail);
        etpass = findViewById(R.id.etLoginPassword);

        btnlog=findViewById(R.id.btnLogIn);
        btnlog.setOnClickListener(this);
        btnBack=findViewById(R.id.btnBackLogin);
        btnBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view==btnBack){
            Intent intent = new Intent(getApplicationContext(), Landing.class);
            startActivity(intent);
        }
        email = etemail.getText().toString();
        pass = etpass.getText().toString();

        if (!isValid(email, pass)) {
            Toast.makeText(getApplicationContext(), "The password or Email is invalid", Toast.LENGTH_LONG).show();
            return;
        }



        Log.d("TAG", "onClick:btnSignIn");
        authenticationService.signIn(email, pass, new AuthenticationService.AuthCallback() {
            @Override
            public void onCompleted(String uid) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "signInWithEmail:success");
                databaseService.getUser(uid, new DatabaseService.DatabaseCallback<User>() {
                    @Override
                    public void onCompleted(User user) {
                        SharedPreferencesUtil.saveUser(Login.this, user);
                        Intent go = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(go);
                    }

                    @Override
                    public void onFailed(Exception e) {
//                                // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithEmail:failure", e);
                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        authenticationService.signOut();
                    }
                });

            }

            @Override
            public void onFailed(Exception e) {
                Log.w("TAG", "signInWithEmail:failure", e);
                Toast.makeText(getApplicationContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean isValid(String email, String pass) {
        if (!email.contains("@")){
            Toast.makeText(getApplicationContext(),"כתובת האימייל לא תקינה", Toast.LENGTH_LONG).show();
            return false;
        }
        if(pass.length()<6){
            Toast.makeText(getApplicationContext(),"הסיסמה קצרה מדי", Toast.LENGTH_LONG).show();
            return false;
        }
        if(pass.length()>20){
            Toast.makeText(getApplicationContext(),"הסיסמה ארוכה מדי", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}