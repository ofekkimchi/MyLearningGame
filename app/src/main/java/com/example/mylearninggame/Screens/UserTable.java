package com.example.mylearninggame.Screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylearninggame.Adapters.UserAdapter;
import com.example.mylearninggame.Model.User;
import com.example.mylearninggame.R;
import com.example.mylearninggame.Services.DatabaseService;

import java.util.List;

/**
 * Admin screen that displays a table of all users in the system
 * Allows administrators to view and edit user information
 */
public class UserTable extends AppCompatActivity {
    // UI Components
    private RecyclerView rv;
    // Services and adapters
    private DatabaseService databaseService;
    private UserAdapter userAdapter;

    /**
     * Initializes the activity and sets up the UI components
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_table);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseService = DatabaseService.getInstance();

        rv = findViewById(R.id.rv_users_all);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Initialize user adapter with click listener for editing users
        this.userAdapter = new UserAdapter(this, new UserAdapter.OnUserClick() {
            @Override
            public void onClick(User user) {
                Intent intent = new Intent(UserTable.this, EditUser.class);
                intent.putExtra("userId", user.getId());
                intent.putExtra("userEmail", user.getEmail());
                intent.putExtra("userFName", user.getFname());
                intent.putExtra("userLName", user.getLname());
                intent.putExtra("userPhone", user.getPhone());
                intent.putExtra("userPassword", user.getPassword());
                intent.putExtra("userIsAdmin", user.getIsAdmin());
                startActivity(intent);
            }
        });
        rv.setAdapter(userAdapter);

        loadUsers();
    }

    /**
     * Reloads the user list when the activity resumes
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();
    }

    /**
     * Loads all users from the database and updates the RecyclerView
     */
    private void loadUsers() {
        databaseService.getUsers(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> userList) {
                userAdapter.setUsers(userList);
            }

            @Override
            public void onFailed(Exception e) {
                // Handle error if needed
            }
        });
    }
}