package com.reeyanto.androidhttprequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.reeyanto.androidhttprequest.adapters.UserAdapter;
import com.reeyanto.androidhttprequest.models.ResultUsers;
import com.reeyanto.androidhttprequest.models.User;
import com.reeyanto.androidhttprequest.services.ApiClient;
import com.reeyanto.androidhttprequest.services.UserService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvUsers;
    private FloatingActionButton fabAdd;
    private ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvUsers = findViewById(R.id.rv_users);
        fabAdd  = findViewById(R.id.fab_add);

        getUsersFromApi();
        fabAdd.setOnClickListener(view -> showAddActivity());
    }

    private void getUsersFromApi() {
        UserService userService = ApiClient.getInstance().create(UserService.class);
        Call<ResultUsers> usersCall = userService.getListUsers(1);
        usersCall.enqueue(new Callback<ResultUsers>() {
            @Override
            public void onResponse(Call<ResultUsers> call, Response<ResultUsers> response) {
                if (response.isSuccessful()) {
                    users.addAll(response.body().getData());
                    setRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<ResultUsers> call, Throwable t) {

            }
        });
    }

    private void setRecyclerView() {
        UserAdapter userAdapter = new UserAdapter(this, users);
        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(userAdapter);
    }

    private void showAddActivity() {
        Intent intent = new Intent(this, InsertEditActivity.class);
        startActivity(intent);
    }
}