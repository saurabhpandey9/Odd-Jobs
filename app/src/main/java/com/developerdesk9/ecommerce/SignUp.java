package com.developerdesk9.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private EditText etREmail, etRPassword;
    private Button rbtn;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private String user_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etREmail = findViewById(R.id.etREmail);
        etRPassword = findViewById(R.id.etRPassword);
        rbtn = findViewById(R.id.rbtn);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();

//        if (currentUser != null) {
//            sendToMain();
//        }


        rbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressBar2.setVisibility(View.VISIBLE);
                final String email = etREmail.getText().toString();
                String password = etRPassword.getText().toString();

                if (email.isEmpty()) {
//                    progressBar2.setVisibility(View.INVISIBLE);
                    etREmail.setError("Enter Email");
                } else if (password.isEmpty()) {
//                    progressBar2.setVisibility(View.INVISIBLE);
                    etRPassword.setError("Enter Password");
                } else if (password.equals("password") || password.equals("12") || password.equals("pass1234")) {
//                    progressBar2.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Enter strong password", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
//                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Toast.makeText(getApplicationContext(), "Register Successfully", Toast.LENGTH_LONG).show();
//                                            Toast.makeText(getApplicationContext(), "Email verification sent", Toast.LENGTH_SHORT).show();
//
////                                            progressBar2.setVisibility(View.INVISIBLE);
//
//                                        } else {
//                                            String errMsg = task.getException().getMessage();
//                                            Toast.makeText(getApplicationContext(), "Error: " + errMsg, Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                });
                                Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),login.class));
                                finish();

                            } else {
//                                progressBar2.setVisibility(View.INVISIBLE);
                                String errMsg = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), "Error: " + errMsg, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }

            }
        });



    }
}
