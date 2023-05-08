package com.example.authapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout update;
    private LinearLayout delet;
    private LinearLayout logout;
    private FirebaseAuth mAuth;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        update = (LinearLayout) findViewById(R.id.linear_update);
        delet = (LinearLayout) findViewById(R.id.linear_delet);
        logout = (LinearLayout) findViewById(R.id.linear_logout);
        builder = new AlertDialog.Builder(this);

        update.setOnClickListener(this);
        delet.setOnClickListener(this);
        logout.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_update:
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                break;
            case R.id.linear_delet:
                deleteaccount();
                break;
            case R.id.linear_logout:
                signOut();
                break;

        }
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(
                    this, "Log Out failed...",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            Intent intent =new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void deleteaccount() {
        builder.setMessage("Are you sure to delete the account?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
                                            .child("Users").child(user.getUid());
                                    mPostReference.removeValue();
                                    Toast.makeText(getApplicationContext(), "User account deleted.",
                                            Toast.LENGTH_SHORT).show();
                                    signOut();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
      //  alert.setTitle(R.string.app_name);
        alert.show();
    }

}