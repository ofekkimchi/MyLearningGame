package com.example.mylearninggame.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylearninggame.Adapters.LevelAdapter;
import com.example.mylearninggame.Model.User;
import com.example.mylearninggame.R;
import com.example.mylearninggame.utils.SharedPreferencesUtil;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Main levels screen that displays all available game levels
 * Allows users to select and start different difficulty levels
 */
public class Levels extends AppCompatActivity implements View.OnClickListener, LevelAdapter.OnLevelClickListener {
    // UI Components
    private RecyclerView rvLevels;
    private LevelAdapter levelAdapter;
    private MaterialButton btnToMain;
    private ImageView ivProfileIconLevels;
    private TextView tvCoinsBalanceLevels;
    private User currentUser;

    /**
     * Initializes the activity and sets up the UI components
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_levels);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        initViews();
        setupRecyclerView();
    }

    /**
     * Updates the UI when the activity resumes
     * Refreshes user's coins balance and profile picture
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Update coins balance and profile picture
        User user = SharedPreferencesUtil.getUser(this);
        if (user != null) {
            tvCoinsBalanceLevels.setText(String.valueOf(user.getCoins()));
            ivProfileIconLevels.setImageResource(user.getProfilePictureId());
        }
    }

    /**
     * Initializes all UI components and sets up click listeners
     */
    private void initViews() {
        btnToMain=findViewById(R.id.btnToMain);
        btnToMain.setOnClickListener(this);
        rvLevels = findViewById(R.id.rvLevels);
        ivProfileIconLevels = findViewById(R.id.ivProfileIconLevels);
        tvCoinsBalanceLevels = findViewById(R.id.tvCoinsBalanceLevels);
        
        ivProfileIconLevels.setOnClickListener(v -> {
            Intent intent = new Intent(Levels.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Updates the UI with current user information
     */
    private void updateUI() {
        tvCoinsBalanceLevels.setText(String.valueOf(currentUser.getCoins()));
        ivProfileIconLevels.setImageResource(currentUser.getProfilePictureId());
        Log.d("LevelsActivity", "Profile picture ID: " + currentUser.getProfilePictureId());
    }

    /**
     * Sets up the RecyclerView with level items
     * Creates and configures the level adapter
     */
    private void setupRecyclerView() {
        List<LevelAdapter.LevelItem> levels = new ArrayList<>();
        levels.add(new LevelAdapter.LevelItem(1, "Basic vocabulary and simple phrases"));
        levels.add(new LevelAdapter.LevelItem(2, "Intermediate vocabulary and common expressions"));
        levels.add(new LevelAdapter.LevelItem(3, "Advanced vocabulary and complex phrases"));

        levelAdapter = new LevelAdapter(levels, this);
        rvLevels.setLayoutManager(new LinearLayoutManager(this));
        rvLevels.setAdapter(levelAdapter);
    }

    /**
     * Handles click events for buttons
     */
    @Override
    public void onClick(View view) {
        if (view == btnToMain){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Handles level click events from the adapter
     * @param levelNumber The number of the level that was clicked
     */
    @Override
    public void onLevelClick(int levelNumber) {
        startLevel(levelNumber);
    }

    /**
     * Starts the selected level by launching the appropriate activity
     * @param levelNumber The number of the level to start
     */
    private void startLevel(int levelNumber) {
        Intent intent;
        switch (levelNumber) {
            case 1:
                intent = new Intent(this, Level1.class);
                break;
            case 2:
                intent = new Intent(this, Level2.class);
                break;
            case 3:
                intent = new Intent(this, Level3.class);
                break;
            default:
                intent = new Intent(this, Level1.class);
        }
        startActivity(intent);
    }
}