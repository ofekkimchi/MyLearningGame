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

/**
 * Main admin page that provides navigation to different admin sections
 * Allows access to user management and level-specific question management
 */
public class AdminPage extends AppCompatActivity implements View.OnClickListener {
    // UI Components for navigation buttons
    Button btnUT, btnAdminL1, btnAdminL2, btnAdminL3;

    /**
     * Initializes the activity and sets up the UI
     * Configures edge-to-edge display and initializes all views
     */
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

    /**
     * Initializes all UI components and sets up click listeners
     * Links all admin navigation buttons to their respective actions
     */
    private void initviews() {
        // Initialize User Table management button
        btnUT = findViewById(R.id.btnUT);
        btnUT.setOnClickListener(this);

        // Initialize Level 1 admin button
        btnAdminL1 = findViewById(R.id.btnAdminL1);
        btnAdminL1.setOnClickListener(this);

        // Initialize Level 2 admin button
        btnAdminL2 = findViewById(R.id.btnAdminL2);
        btnAdminL2.setOnClickListener(this);

        // Initialize Level 3 admin button
        btnAdminL3 = findViewById(R.id.btnAdminL3);
        btnAdminL3.setOnClickListener(this);
    }

    /**
     * Handles all button click events in the admin page
     * Navigates to the appropriate admin section based on the clicked button
     * @param v The view that was clicked
     */
    @Override
    public void onClick(View v) {
        // Navigate to User Table management
        if (v == btnUT) {
            Intent intent = new Intent(getApplicationContext(), UserTable.class);
            startActivity(intent);
        }
        // Navigate to Level 1 question management
        if (v == btnAdminL1) {
            Intent intent = new Intent(getApplicationContext(), Level1Admin.class);
            startActivity(intent);
        }
        // Navigate to Level 2 question management
        if (v == btnAdminL2) {
            Intent intent = new Intent(getApplicationContext(), Level2Admin.class);
            startActivity(intent);
        }
        // Navigate to Level 3 question management
        if (v == btnAdminL3) {
            Intent intent = new Intent(getApplicationContext(), Level3Admin.class);
            startActivity(intent);
        }
    }
}