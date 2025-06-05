package com.example.mylearninggame.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylearninggame.R;

import java.util.ArrayList;
import java.util.List;

public class Levels extends AppCompatActivity {
    private RecyclerView rvLevels;
    private LevelAdapter levelAdapter;

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

    private void initViews() {
        rvLevels = findViewById(R.id.rvLevels);
    }

    private void setupRecyclerView() {
        List<LevelItem> levels = new ArrayList<>();
        levels.add(new LevelItem(1, "Basic vocabulary and simple phrases"));
        levels.add(new LevelItem(2, "Intermediate vocabulary and common expressions"));
        levels.add(new LevelItem(3, "Advanced vocabulary and complex phrases"));

        levelAdapter = new LevelAdapter(levels);
        rvLevels.setLayoutManager(new LinearLayoutManager(this));
        rvLevels.setAdapter(levelAdapter);
    }

    private class LevelItem {
        int levelNumber;
        String description;

        LevelItem(int levelNumber, String description) {
            this.levelNumber = levelNumber;
            this.description = description;
        }
    }

    private class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelViewHolder> {
        private List<LevelItem> levels;

        LevelAdapter(List<LevelItem> levels) {
            this.levels = levels;
        }

        @NonNull
        @Override
        public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_level, parent, false);
            return new LevelViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {
            LevelItem level = levels.get(position);
            holder.tvLevelNumber.setText("Level " + level.levelNumber);
            holder.btnStartLevel.setOnClickListener(v -> startLevel(level.levelNumber));
        }

        @Override
        public int getItemCount() {
            return levels.size();
        }

        private class LevelViewHolder extends RecyclerView.ViewHolder {
            TextView tvLevelNumber;
            Button btnStartLevel;

            LevelViewHolder(View itemView) {
                super(itemView);
                tvLevelNumber = itemView.findViewById(R.id.tvLevelNumber);
                btnStartLevel = itemView.findViewById(R.id.btnStartLevel);
            }
        }
    }

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