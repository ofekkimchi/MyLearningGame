package com.example.mylearninggame.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylearninggame.Model.User;
import com.example.mylearninggame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * אדפטר להצגת משתמשים ברשימה
 * מטפל בהצגה ובאינטראקציה עם פריטי משתמש בממשק המנהל
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private Context context;
    private OnUserClick onUserClick;

    /**
     * ממשק לטיפול באירועי לחיצה על משתמשים
     * מספק קריאת חזרה כאשר פריט משתמש נלחץ
     */
    public interface OnUserClick {
        public void onClick(User user);
    }

    /**
     * בנאי לאדפטר המשתמשים
     * @param context ההקשר שבו האדפטר פועל
     * @param onUserClick מאזין לטיפול באירועי לחיצה על משתמשים
     */
    public UserAdapter(Context context, OnUserClick onUserClick) {
        this.context = context;
        this.userList = new ArrayList<>();
        this.onUserClick = onUserClick;
    }

    /**
     * יוצר מחזיק תצוגה חדש עבור פריט משתמש
     * @param parent קבוצת התצוגה האב
     * @param viewType סוג התצוגה ליצירה
     * @return מופע חדש של UserViewHolder
     */
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    /**
     * מקשר נתוני משתמש למחזיק התצוגה
     * מגדיר את הצגת המשתמש ומאזין לחיצה
     * @param holder מחזיק התצוגה לקישור נתונים
     * @param position מיקום המשתמש ברשימה
     */
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        if (user == null) return;
        holder.userIdTextView.setText(user.getFname() + " " + user.getLname().trim());
        holder.userNameTextView.setText(user.getPhone());

        holder.itemView.setOnClickListener(v -> onUserClick.onClick(user));
    }

    /**
     * מחזיר את המספר הכולל של משתמשים ברשימה
     * @return מספר המשתמשים
     */
    @Override
    public int getItemCount() {
        return userList.size();
    }

    /**
     * מעדכן את רשימת המשתמשים כולה
     * @param userList רשימת המשתמשים החדשה
     */
    public void setUsers(List<User> userList) {
        this.userList.clear();
        this.userList.addAll(userList);
        notifyDataSetChanged();
    }

    /**
     * מחלקת מחזיק תצוגה עבור פריטי משתמש
     * מחזיקה הפניות לתצוגות בפריט המשתמש
     */
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userIdTextView;
        TextView userNameTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userIdTextView = itemView.findViewById(R.id.userIdTextView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
        }
    }
}
