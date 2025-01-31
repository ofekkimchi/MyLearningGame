package com.example.mylearninggame.Screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mylearninggame.Model.User;
import com.example.mylearninggame.R;
import com.example.mylearninggame.Services.AuthenticationService;
import com.example.mylearninggame.Services.DatabaseService;
import com.example.mylearninggame.utils.SharedPreferencesUtil;

public class Singup extends AppCompatActivity implements View.OnClickListener {
    EditText etFName, etLName, etPhone, etEmail, etPass;
    Button btnReg;

    String fName,lName, phone, email, pass;
    AuthenticationService authenticationService;
    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_singup);
        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();

        init_views();

        btnReg.setOnClickListener(this);
    }

    private void init_views() {
        btnReg=findViewById(R.id.btnRegister);
        etFName=findViewById(R.id.etFname);
        etLName=findViewById(R.id.etLname);
        etPhone=findViewById(R.id.etPhone);
        etEmail=findViewById(R.id.etEmail);
        etPass=findViewById(R.id.etPassword);
    }

    @Override
    public void onClick(View view) {
        fName=etFName.getText().toString();
        lName=etLName.getText().toString();
        phone=etPhone.getText().toString();
        email=etEmail.getText().toString();
        pass=etPass.getText().toString();


        //check if registration is valid
        boolean isValid=true;
        if (fName.length()<2){
            Toast.makeText(Singup.this,"שם פרטי קצר מדי", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if (lName.length()<2){
            Toast.makeText(Singup.this,"שם משפחה קצר מדי", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if (phone.length()<9||phone.length()>10){
            Toast.makeText(Singup.this,"מספר הטלפון לא תקין", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if (!email.contains("@")){
            Toast.makeText(Singup.this,"כתובת האימייל לא תקינה", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if(pass.length()<6){
            Toast.makeText(Singup.this,"הסיסמה קצרה מדי", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if(pass.length()>20){
            Toast.makeText(Singup.this,"הסיסמה ארוכה מדי", Toast.LENGTH_LONG).show();
            isValid = false;
        }

        authenticationService.signUp(email, pass, new AuthenticationService.AuthCallback() {
            @Override
            public void onCompleted(String uid) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "createUserWithEmail:success");

                User newUser=new User(uid, fName, lName, phone, email,pass, false);
                databaseService.createNewUser(newUser, new DatabaseService.DatabaseCallback<Void>() {
                    @Override
                    public void onCompleted(Void object) {
                        SharedPreferencesUtil.saveUser(getApplicationContext(), newUser);
                        Intent goLog=new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(goLog);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", e);
                        Toast.makeText(Singup.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        authenticationService.signOut();
                    }
                });

            }

            @Override
            public void onFailed(Exception e) {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "createUserWithEmail:failure", e);
                Toast.makeText(Singup.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}