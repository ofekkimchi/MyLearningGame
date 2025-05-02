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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private Context context;
    private OnUserClick onUserClick;

    public interface OnUserClick {
        public void onClick(User user);
    }

    public UserAdapter(Context context, OnUserClick onUserClick) {
        this.context = context;
        this.userList = new ArrayList<>();
        this.onUserClick = onUserClick;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        if (user == null) return;
        holder.userIdTextView.setText(user.getFname() + " " + user.getLname().trim());
        holder.userNameTextView.setText(user.getPhone());

        holder.itemView.setOnClickListener(v -> onUserClick.onClick(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setUsers(List<User> userList) {
        this.userList.clear();
        this.userList.addAll(userList);
        notifyDataSetChanged();
    }

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
