package com.example.mylearninggame.Screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mylearninggame.Model.User;
import com.example.mylearninggame.R;
import com.example.mylearninggame.Services.DatabaseService;
import com.example.mylearninggame.utils.SharedPreferencesUtil;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfileActivity extends AppCompatActivity {
    private TextInputEditText etFirstName, etLastName, etEmail, etPhone, etPassword;
    private Button btnSave;
    private LinearLayout profilePicturesContainer;
    private ImageView ivDefaultProfile;
    private User currentUser;
    private int selectedProfilePictureId;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initViews();
        currentUser = SharedPreferencesUtil.getUser(this);
        if (currentUser == null) {
            Toast.makeText(this, "שגיאה בטעינת פרטי המשתמש", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        databaseService = DatabaseService.getInstance();
        loadUserData();
        setupClickListeners();
        loadPurchasedProfilePictures();
    }

    private void initViews() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnSave = findViewById(R.id.btnSave);
        profilePicturesContainer = findViewById(R.id.profilePicturesContainer);
        ivDefaultProfile = findViewById(R.id.ivDefaultProfile);
    }

    private void loadUserData() {
        etFirstName.setText(currentUser.getFname());
        etLastName.setText(currentUser.getLname());
        etEmail.setText(currentUser.getEmail());
        etPhone.setText(currentUser.getPhone());
        etPassword.setText(currentUser.getPassword());
        selectedProfilePictureId = currentUser.getProfilePictureId();
        updateProfilePictureSelection();
    }

    private void setupClickListeners() {
        ivDefaultProfile.setOnClickListener(v -> {
            selectedProfilePictureId = R.drawable.default_profile;
            updateProfilePictureSelection();
        });

        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void loadPurchasedProfilePictures() {
        // Add purchased profile pictures to the container
        if (currentUser.hasPurchasedProfilePicture(R.drawable.profile1)) {
            addProfilePictureToContainer(R.drawable.profile1);
        }
        if (currentUser.hasPurchasedProfilePicture(R.drawable.profile2)) {
            addProfilePictureToContainer(R.drawable.profile2);
        }
        if (currentUser.hasPurchasedProfilePicture(R.drawable.profile3)) {
            addProfilePictureToContainer(R.drawable.profile3);
        }
        if (currentUser.hasPurchasedProfilePicture(R.drawable.profile4)) {
            addProfilePictureToContainer(R.drawable.profile4);
        }
        if (currentUser.hasPurchasedProfilePicture(R.drawable.profile5)) {
            addProfilePictureToContainer(R.drawable.profile5);
        }
    }

    private void addProfilePictureToContainer(int pictureId) {
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) getResources().getDimension(R.dimen.profile_picture_size),
                (int) getResources().getDimension(R.dimen.profile_picture_size)
        );
        params.setMarginEnd((int) getResources().getDimension(R.dimen.profile_picture_margin));
        imageView.setLayoutParams(params);
        imageView.setImageResource(pictureId);
        imageView.setClickable(true);
        imageView.setFocusable(true);
        imageView.setBackgroundResource(R.drawable.profile_picture_background);
        imageView.setOnClickListener(v -> {
            selectedProfilePictureId = pictureId;
            updateProfilePictureSelection();
        });
        profilePicturesContainer.addView(imageView);
    }

    private void updateProfilePictureSelection() {
        // Reset all profile picture selections
        ivDefaultProfile.setAlpha(1.0f);
        for (int i = 0; i < profilePicturesContainer.getChildCount(); i++) {
            View child = profilePicturesContainer.getChildAt(i);
            if (child instanceof ImageView) {
                child.setAlpha(1.0f);
            }
        }

        // Highlight selected profile picture
        if (selectedProfilePictureId == R.drawable.default_profile) {
            ivDefaultProfile.setAlpha(0.5f);
        } else {
            for (int i = 0; i < profilePicturesContainer.getChildCount(); i++) {
                View child = profilePicturesContainer.getChildAt(i);
                if (child instanceof ImageView) {
                    ImageView imageView = (ImageView) child;
                    if (imageView.getDrawable().getConstantState() == 
                        getResources().getDrawable(selectedProfilePictureId, null).getConstantState()) {
                        imageView.setAlpha(0.5f);
                        break;
                    }
                }
            }
        }
    }

    private void saveChanges() {
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();

        if (!validateInput(firstName, lastName, email, phone, password)) {
            return;
        }

        currentUser.setFname(firstName);
        currentUser.setLname(lastName);
        currentUser.setEmail(email);
        currentUser.setPhone(phone);
        currentUser.setPassword(password);
        currentUser.setProfilePictureId(selectedProfilePictureId);

        databaseService.createNewUser(currentUser, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void unused) {
                runOnUiThread(() -> {
                    SharedPreferencesUtil.saveUser(EditProfileActivity.this, currentUser);
                    Toast.makeText(EditProfileActivity.this, "השינויים נשמרו בהצלחה", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                });
            }

            @Override
            public void onFailed(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(EditProfileActivity.this, "שגיאה בשמירת השינויים", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private boolean validateInput(String firstName, String lastName, String email, String phone, String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "כל השדות חובה", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}