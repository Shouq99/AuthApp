package com.example.authapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Button save;
    private ImageView back;
    ProgressBar progressBar3;
    private EditText editTextName, editTextPassword ;
            //editTextAge;
    private TextView txt_email;
    private FirebaseAuth mAlarm;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private String TAG = "maha";
    private String uid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        back=(ImageView) findViewById(R.id.img_back);
        editTextName = (EditText) findViewById(R.id.ed_nameuser);
        editTextPassword = (EditText) findViewById(R.id.ed_Password);
     //   editTextAge = (EditText) findViewById(R.id.edAge_user);
        txt_email = (TextView) findViewById(R.id.txt_email);
        save = (Button) findViewById(R.id.btnsave);
        mAlarm = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseUser=mDatabase.child("Users");
        uid = mAlarm.getCurrentUser().getUid();
        save.setOnClickListener(this);
        back.setOnClickListener(this);
        Log.e(TAG, "uid :"+uid );
        String email= mAlarm.getCurrentUser().getEmail();
        txt_email.setText(email);
        getUser();
    }

    private void getUser() {
        uid = mAlarm.getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   // for(DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                        Log.e(TAG, "getUser: "+ uid);
                        // Get Post object and use the values to update the UI
                        User user = dataSnapshot.getValue(User.class);
                          editTextName.setText(user.usernamev);
                          editTextPassword.setText(user.passv);
                        Log.e(TAG, "user:"+ user.usernamev);
                        progressBar3.setVisibility(View.GONE);
             //       }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnsave:{
                String usernamev = editTextName.getText().toString().trim();
                String passv = editTextPassword.getText().toString().trim();
              //  String age = editTextAge.getText().toString().trim();
                if (passv.isEmpty()) {
                    editTextPassword.setError("Password is required!");
                    editTextPassword.requestFocus();
                    return;
                }
                if (passv.length() < 6) {
                    editTextPassword.setError("Min password length is 6 characters!");
                    editTextPassword.requestFocus();
                    return;
                }
                if (usernamev.isEmpty()) {
                    editTextName.setError("name is required!");
                    editTextName.requestFocus();
                    return;
                }
//                if (age.isEmpty()) {
//                    editTextAge.setError("Age is required!");
//                    editTextAge.requestFocus();
//                    return;
//                }
                String name = editTextName.getText().toString();
                String password = editTextPassword.getText().toString();

                updateUserById(name,password);

                break;}
            case R.id.img_back:{
                onBackPressed();
                finish();
            }

        }
    }

    private void updateUserById(String name, String password) {
        uid = mAlarm.getCurrentUser().getUid();
        HashMap user = new HashMap();
        user.put("usernamev", name);
        user.put("passv", password);
        Log.e(TAG, "updateUserById: "+name +password );

        mDatabaseUser.child(uid).setValue(user);
        Toast.makeText(this,"Updated",Toast.LENGTH_SHORT).show();
    }
}

