package com.example.authapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static android.util.Patterns.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private TextView back, register;
    private EditText name, email, password, age;
    private ProgressBar progressBar2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();
        back = (TextView) findViewById(R.id.banner);
        back.setOnClickListener(this);
        register = (Button) findViewById(R.id.registerUser);
        register.setOnClickListener(this);
        name = (EditText) findViewById(R.id.FullName);
        email = (EditText) findViewById(R.id.email);
        age = (EditText) findViewById(R.id.age);
        password = (EditText) findViewById(R.id.password);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                register();
                break;

        }
    }

    private void register() {

        String emailv = email.getText().toString().trim();
        String usernamev = name.getText().toString().trim();
        String passv = password.getText().toString().trim();
        String age1 = age.getText().toString().trim();
        if (usernamev.isEmpty()) {
            name.setError("name is required");
            name.requestFocus();
            return;
        }
        if (age1.isEmpty()) {
            age.setError("Age is required");
            age.requestFocus();
            return;
        }

        if (emailv.isEmpty()) {
            email.setError("email is required");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailv).matches()) {
            email.setError("please provide a valid email");
            email.requestFocus();
            return;
        }
        if (passv.isEmpty()) {
            password.setError("password is required");
            password.requestFocus();
            return;
        }
        if (passv.length() < 6) {
            password.setError("password should be more than 6 digits");
            password.requestFocus();
            return;
        }
        progressBar2.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailv, passv)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(usernamev, passv);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, "user has been registered successfuly", Toast.LENGTH_LONG).show();
                                        progressBar2.setVisibility(View.GONE);
                                        startActivity(new Intent(RegisterUser.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterUser.this, "not registered", Toast.LENGTH_LONG).show();
                                        //Login here
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterUser.this, "not registered", Toast.LENGTH_LONG).show();
                        }
                    }

                });


    }
}