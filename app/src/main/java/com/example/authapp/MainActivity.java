package com.example.authapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;
    private FirebaseAuth mAlarm;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);
        signIn = (Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(this);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        mAlarm = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.signIn:
                progressBar.setVisibility(View.VISIBLE);
                userLogin();
                break;

        }
    }
    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email!");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Min password length is 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAlarm.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();

                // if(user.isEmailVerified()){
                     //user profile
                 Handler handler = new Handler();
// Create and start a new Thread
                 new Thread(new Runnable() {
                     public void run() {
                         try{
                             Thread.sleep(5000);
                         }
                         catch (Exception e) { } // Just catch the InterruptedException
                         // Now we use the Handler to post back to the main thread
                         handler.post(new Runnable() {
                             public void run() {
                                 if (task.isSuccessful()){
                                 // Set the View's visibility back on the main UI Thread
                                 progressBar.setVisibility(View.GONE);
                                 startActivity(new Intent(MainActivity.this,MainActivity2.class));
                                 finish();
                              }else {
                                 //user.sendEmailVerification();
                                 progressBar.setVisibility(View.GONE);
                                 Toast.makeText(MainActivity.this, "check your email to verify account!",Toast.LENGTH_LONG).show();
                             }
                             }
                         });
                     }
                 }).start();

                // startActivity(new Intent(MainActivity.this,ProfileActivity.class));
             //}else {
                 //Toast.makeText(MainActivity.this,"failed to login ",Toast.LENGTH_LONG).show();
             //}
            }
        });
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(MainActivity.this,MainActivity2.class));
                finish();
            }
        }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAlarm.getCurrentUser();
        updateUI(currentUser);
    }
}