package com.example.mylearninggame.Screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mylearninggame.Model.User;
import com.example.mylearninggame.R;
import com.example.mylearninggame.Services.DatabaseService;
import com.example.mylearninggame.utils.SharedPreferencesUtil;

/**
 * Activity for editing user details by administrators
 * Allows modification of user information including personal details and admin status
 */
public class EditUser extends AppCompatActivity {

    // User object to store the user being edited
    User user;
    // UI Components for user information input
    EditText editEmail, editFName, editLName, editPhone, editPass;
    Button btnSave;
    DatabaseService databaseService;

    /**
     * Initializes the activity and sets up the UI
     * Creates user object from intent extras and initializes all components
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Create user object from intent extras
        String id = getIntent().getStringExtra("userId");
        String email = getIntent().getStringExtra("userEmail");
        String fname = getIntent().getStringExtra("userFName");
        String lname = getIntent().getStringExtra("userLName");
        String phone = getIntent().getStringExtra("userPhone");
        String password = getIntent().getStringExtra("userPassword");
        boolean isAdmin = getIntent().getBooleanExtra("userIsAdmin", false);

        user = new User(id, fname, lname, phone, email, password, isAdmin, 0, R.drawable.default_profile);

        databaseService = DatabaseService.getInstance();
        initviews();
        displayUserDetails();
        setupSaveButton();
    }

    /**
     * Initializes all UI components by finding their views
     */
    private void initviews() {
        editEmail = findViewById(R.id.editEmail);
        editFName = findViewById(R.id.editFName);
        editLName = findViewById(R.id.editLName);
        editPass = findViewById(R.id.editPass);
        editPhone = findViewById(R.id.editPhone);
        btnSave = findViewById(R.id.btnSave);
    }

    /**
     * Populates the form fields with the user's current information
     */
    private void displayUserDetails() {
        if (user != null) {
            editEmail.setText(user.getEmail());
            editFName.setText(user.getFname());
            editLName.setText(user.getLname());
            editPhone.setText(user.getPhone());
            editPass.setText(user.getPassword());
        }
    }

    /**
     * Sets up the save button click listener
     */
    private void setupSaveButton() {
        btnSave.setOnClickListener(v -> saveUserChanges());
    }

    /**
     * Saves the updated user information to the database
     * Updates the user object with new values and handles the database operation
     */
    private void saveUserChanges() {
        if (user == null) return;

        // Update user object with new values
        user.setEmail(editEmail.getText().toString());
        user.setFname(editFName.getText().toString());
        user.setLname(editLName.getText().toString());
        user.setPhone(editPhone.getText().toString());
        user.setPassword(editPass.getText().toString());

        // Disable save button while saving
        btnSave.setEnabled(false);

        // Save to database
        databaseService.createNewUser(user, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void unused) {
                runOnUiThread(() -> {
                    Toast.makeText(EditUser.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Set result to indicate successful update
                    finish();
                });
            }

            @Override
            public void onFailed(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(EditUser.this, "Failed to update user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true); // Re-enable save button on failure
                });
            }
        });
    }
}