package com.reeyanto.androidhttprequest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.reeyanto.androidhttprequest.models.User;
import com.reeyanto.androidhttprequest.services.ApiClient;
import com.reeyanto.androidhttprequest.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertEditActivity extends AppCompatActivity {

    private TextView tvFirstName, tvLastName, tvEmail;
    private EditText etFirstName, etLastName, etEmail;
    private Button btnSave;
    private String firstName, lastName, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_edit);

        initComponents();
        btnSave.setOnClickListener(view -> saveData(null));

        // cek apakah ini sebagai form tambah atau form edit
        isFormEdit();
    }

    private void isFormEdit() {
        if (getIntent().hasExtra("EDIT_USER")) {
            User user = getIntent().getParcelableExtra("EDIT_USER");

            etFirstName.setText(user.getFirstName());
            etLastName.setText(user.getLastName());
            etEmail.setText(user.getEmail());

            btnSave.setText(getResources().getString(R.string.update));
            btnSave.setOnClickListener(view -> saveData(String.valueOf(user.getId())));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void saveData(@Nullable String sid) {
        // simpan data
        if (dataValidation()) {
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);

            UserService userService = ApiClient.getInstance().create(UserService.class);

            if (sid == null) {
                Call<User> userCall = userService.insertUsert(user);
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(InsertEditActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            } else {
                Call<User> userCall = userService.updateUser(user.getId(), firstName, lastName, email);
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(InsertEditActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        }
    }

    private boolean dataValidation() {
        firstName = etFirstName.getText().toString().trim();
        lastName  = etLastName.getText().toString().trim();
        email     = etEmail.getText().toString().trim();

        boolean isValid = true;

        if (firstName.isEmpty()) {
            etFirstName.setError("First name cannot be empty");
            isValid = false;
        }
        if (lastName.isEmpty()) {
            etLastName.setError("Last name cannot be empty");
            isValid = false;
        }
        if (email.isEmpty()) {
            etEmail.setError("Email cannot be empty");
            isValid = false;
        }
        if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Format email is not valid");
            isValid = false;
        }
        return isValid;
    }

    private void initComponents() {
        tvFirstName = findViewById(R.id.tv_first_name);
        tvLastName  = findViewById(R.id.tv_last_name);
        tvEmail     = findViewById(R.id.tv_email);

        etFirstName = findViewById(R.id.et_first_name);
        etLastName  = findViewById(R.id.et_last_name);
        etEmail     = findViewById(R.id.et_email);

        btnSave     = findViewById(R.id.btn_save);
    }
}