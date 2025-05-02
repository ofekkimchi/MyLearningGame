package com.example.mylearninggame.Screens;

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

public class UserTable extends AppCompatActivity {
    RecyclerView rv;
    DatabaseService databaseService;

    UserAdapter userAdapter;

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

        databaseService= DatabaseService.getInstance();

        rv = findViewById(R.id.rv_users_all);
        rv.setLayoutManager(new LinearLayoutManager(this));

        this.userAdapter = new UserAdapter(this, new UserAdapter.OnUserClick() {
            @Override
            public void onClick(User user) {

            }
        });
        rv.setAdapter(userAdapter);

        databaseService.getUsers(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> userList) {
                userAdapter.setUsers(userList);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }
}