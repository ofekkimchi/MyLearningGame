package com.example.mylearninggame.Screens;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mylearninggame.Model.User;
import com.example.mylearninggame.R;
import com.example.mylearninggame.Services.DatabaseService;
import com.example.mylearninggame.utils.SharedPreferencesUtil;
import com.google.android.material.button.MaterialButton;

/**
 * Profile screen that displays user information and allows profile customization
 * Handles profile picture purchases and profile editing
 */
public class ProfileActivity extends AppCompatActivity {
    // UI Components
    private TextView tvUsername, tvCoinsBalance;
    private ImageView ivProfilePicture;
    private MaterialButton btnEditProfile, btnBuyProfile1, btnBuyProfile2, btnBuyProfile3,
            btnBuyProfile4, btnBuyProfile5;
    private User currentUser;
    private DatabaseService databaseService;

    // Profile picture prices
    private static final int PROFILE1_PRICE = 100;
    private static final int PROFILE2_PRICE = 250;
    private static final int PROFILE3_PRICE = 500;
    private static final int PROFILE4_PRICE = 750;
    private static final int PROFILE5_PRICE = 800;

    /**
     * Activity result launcher for handling profile edit results
     */
    private final ActivityResultLauncher<Intent> editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    currentUser = SharedPreferencesUtil.getUser(this);
                    if (currentUser != null) {
                        updateUI();
                    }
                }
            });

    /**
     * Initializes the activity and sets up the UI components
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        databaseService = DatabaseService.getInstance();
        initViews();
        currentUser = SharedPreferencesUtil.getUser(this);
        if (currentUser != null) {
            updateUI();
        }
        setupClickListeners();
    }

    /**
     * Initializes all UI components
     */
    private void initViews() {
        tvUsername = findViewById(R.id.tvUsername);
        tvCoinsBalance = findViewById(R.id.tvCoinsBalance);
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnBuyProfile1 = findViewById(R.id.btnBuyProfile1);
        btnBuyProfile2 = findViewById(R.id.btnBuyProfile2);
        btnBuyProfile3 = findViewById(R.id.btnBuyProfile3);
        btnBuyProfile4 = findViewById(R.id.btnBuyProfile4);
        btnBuyProfile5 = findViewById(R.id.btnBuyProfile5);
    }

    /**
     * Updates the UI with current user information
     */
    private void updateUI() {
        tvUsername.setText(currentUser.getFname());
        tvCoinsBalance.setText(String.valueOf(currentUser.getCoins()));
        ivProfilePicture.setImageResource(currentUser.getProfilePictureId());
        updateBuyButtonsState();
    }

    /**
     * Updates the state of profile picture purchase buttons
     * Disables buttons for already purchased pictures
     */
    private void updateBuyButtonsState() {
        btnBuyProfile1.setText(currentUser.hasPurchasedProfilePicture(R.drawable.profile1) ? "Owned" : "Buy");
        btnBuyProfile2.setText(currentUser.hasPurchasedProfilePicture(R.drawable.profile2) ? "Owned" : "Buy");
        btnBuyProfile3.setText(currentUser.hasPurchasedProfilePicture(R.drawable.profile3) ? "Owned" : "Buy");
        btnBuyProfile4.setText(currentUser.hasPurchasedProfilePicture(R.drawable.profile4) ? "Owned" : "Buy");
        btnBuyProfile5.setText(currentUser.hasPurchasedProfilePicture(R.drawable.profile5) ? "Owned" : "Buy");
        
        btnBuyProfile1.setEnabled(!currentUser.hasPurchasedProfilePicture(R.drawable.profile1));
        btnBuyProfile2.setEnabled(!currentUser.hasPurchasedProfilePicture(R.drawable.profile2));
        btnBuyProfile3.setEnabled(!currentUser.hasPurchasedProfilePicture(R.drawable.profile3));
        btnBuyProfile4.setEnabled(!currentUser.hasPurchasedProfilePicture(R.drawable.profile4));
        btnBuyProfile5.setEnabled(!currentUser.hasPurchasedProfilePicture(R.drawable.profile5));
    }

    /**
     * Sets up click listeners for all buttons
     */
    private void setupClickListeners() {
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            editProfileLauncher.launch(intent);
        });

        btnBuyProfile1.setOnClickListener(v -> handleProfilePurchase(R.drawable.profile1, PROFILE1_PRICE));
        btnBuyProfile2.setOnClickListener(v -> handleProfilePurchase(R.drawable.profile2, PROFILE2_PRICE));
        btnBuyProfile3.setOnClickListener(v -> handleProfilePurchase(R.drawable.profile3, PROFILE3_PRICE));
        btnBuyProfile4.setOnClickListener(v -> handleProfilePurchase(R.drawable.profile4, PROFILE4_PRICE));
        btnBuyProfile5.setOnClickListener(v -> handleProfilePurchase(R.drawable.profile5, PROFILE5_PRICE));
    }

    /**
     * Handles the purchase of a profile picture
     * @param profilePictureId The ID of the profile picture to purchase
     * @param price The price of the profile picture
     */
    private void handleProfilePurchase(int profilePictureId, int price) {
        if (currentUser.getCoins() < price) {
            Toast.makeText(this, "אין לך מספיק מטבעות!", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.setCoins(currentUser.getCoins() - price);
        currentUser.addPurchasedProfilePicture(profilePictureId);
        
        databaseService.createNewUser(currentUser, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void unused) {
                runOnUiThread(() -> {
                    SharedPreferencesUtil.saveUser(ProfileActivity.this, currentUser);
                    showProfilePurchaseConfirmationDialog(profilePictureId);
                    updateUI();
                });
            }

            @Override
            public void onFailed(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(ProfileActivity.this, "שגיאה בקניית התמונה", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    /**
     * Shows a confirmation dialog after purchasing a profile picture
     * Allows the user to immediately set the new picture as their profile picture
     * @param profilePictureId The ID of the newly purchased profile picture
     */
    private void showProfilePurchaseConfirmationDialog(int profilePictureId) {
        new AlertDialog.Builder(this)
                .setTitle("תמונה נרכשה בהצלחה!")
                .setMessage("האם תרצה להחליף את תמונת הפרופיל הנוכחית?")
                .setPositiveButton("כן", (dialog, which) -> {
                    currentUser.setProfilePictureId(profilePictureId);
                    databaseService.createNewUser(currentUser, new DatabaseService.DatabaseCallback<Void>() {
                        @Override
                        public void onCompleted(Void unused) {
                            runOnUiThread(() -> {
                                SharedPreferencesUtil.saveUser(ProfileActivity.this, currentUser);
                                updateUI();
                            });
                        }

                        @Override
                        public void onFailed(Exception e) {
                            runOnUiThread(() -> {
                                Toast.makeText(ProfileActivity.this, "שגיאה בהחלפת התמונה", Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                })
                .setNegativeButton("לא", null)
                .show();
    }
} 