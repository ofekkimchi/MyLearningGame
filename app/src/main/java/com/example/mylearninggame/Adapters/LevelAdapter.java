package com.example.mylearninggame.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylearninggame.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

/**
 * אדפטר להצגת רמות משחק ברשימה
 * מטפל בהצגה ובאינטראקציה עם רמות המשחק
 */
public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelViewHolder> {
    private List<LevelItem> levels;
    private OnLevelClickListener listener;

    /**
     * ממשק לטיפול באירועי לחיצה על רמות
     * מספק קריאת חזרה כאשר רמה נבחרת
     */
    public interface OnLevelClickListener {
        void onLevelClick(int levelNumber);
    }

    /**
     * בנאי לאדפטר הרמות
     * @param levels רשימת פריטי הרמות להצגה
     * @param listener מאזין לחיצה על פריטי רמה
     */
    public LevelAdapter(List<LevelItem> levels, OnLevelClickListener listener) {
        this.levels = levels;
        this.listener = listener;
    }

    /**
     * יוצר מחזיק תצוגה חדש עבור פריט רמה
     * @param parent קבוצת התצוגה האב
     * @param viewType סוג התצוגה ליצירה
     * @return מופע חדש של LevelViewHolder
     */
    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_level, parent, false);
        return new LevelViewHolder(view);
    }

    /**
     * מקשר נתוני רמה למחזיק התצוגה
     * מגדיר את הצגת הרמה ומאזין לחיצה
     * @param holder מחזיק התצוגה לקישור נתונים
     * @param position מיקום הרמה ברשימה
     */
    @Override
    public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {
        LevelItem level = levels.get(position);
        holder.tvLevelNumber.setText("Level " + level.levelNumber);
        holder.btnStartLevel.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLevelClick(level.levelNumber);
            }
        });
    }

    /**
     * מחזיר את המספר הכולל של רמות ברשימה
     * @return מספר הרמות
     */
    @Override
    public int getItemCount() {
        return levels.size();
    }

    /**
     * מחלקת מחזיק תצוגה עבור פריטי רמה
     * מחזיקה הפניות לתצוגות בפריט הרמה
     */
    public static class LevelViewHolder extends RecyclerView.ViewHolder {
        TextView tvLevelNumber;
        MaterialButton btnStartLevel;

        public LevelViewHolder(View itemView) {
            super(itemView);
            tvLevelNumber = itemView.findViewById(R.id.tvLevelNumber);
            btnStartLevel = itemView.findViewById(R.id.btnStartLevel);
        }
    }

    /**
     * מחלקת נתונים המייצגת פריט רמה
     * מכילה מידע על רמת משחק
     */
    public static class LevelItem {
        public int levelNumber;
        public String description;

        /**
         * בנאי לפריט רמה
         * @param levelNumber מספר הרמה
         * @param description תיאור תוכן הרמה
         */
        public LevelItem(int levelNumber, String description) {
            this.levelNumber = levelNumber;
            this.description = description;
        }
    }
} 