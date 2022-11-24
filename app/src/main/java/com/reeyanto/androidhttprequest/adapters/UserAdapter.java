package com.reeyanto.androidhttprequest.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reeyanto.androidhttprequest.InsertEditActivity;
import com.reeyanto.androidhttprequest.R;
import com.reeyanto.androidhttprequest.models.User;
import com.reeyanto.androidhttprequest.services.ApiClient;
import com.reeyanto.androidhttprequest.services.UserService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private User user;
    private ArrayList<User> listUsers;

    public UserAdapter(Context context, ArrayList<User> listUsers) {
        this.context = context;
        this.listUsers = listUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        user = listUsers.get(position);

        holder.textViewUser.setText(user.getFirstName());
        holder.textViewUser.setText(user.getEmail());
        Glide.with(context).load(user.getAvatar()).into(holder.imageViewUser);

        holder.btnEdit.setOnClickListener(view -> editUser(listUsers.get(position)));
        holder.btnDelete.setOnClickListener(view -> deleteUser(listUsers.get(position).getId()));
    }

    private void deleteUser(int id) {
        UserService userService = ApiClient.getInstance().create(UserService.class);
        Call<User> userCall = userService.deleteUser(id);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Data deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void editUser(User user) {
        Intent intent = new Intent(context, InsertEditActivity.class);
        intent.putExtra("EDIT_USER", user);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewUser;
        private TextView textViewUser, textViewEmail;
        private Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewUser = itemView.findViewById(R.id.iv_user);
            textViewUser  = itemView.findViewById(R.id.tv_user);
            textViewEmail = itemView.findViewById(R.id.tv_email);

            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
