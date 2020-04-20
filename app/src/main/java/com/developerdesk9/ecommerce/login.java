package com.developerdesk9.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class login extends AppCompatActivity {

    private EditText etLEmail, etLPassword;
    private Button lbtn, lrbtn;

    private TextView signup_tv,tv_forget_password;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLEmail = findViewById(R.id.etLEmail);
        etLPassword = findViewById(R.id.etLPassword);
        lbtn = findViewById(R.id.lbtn);
        signup_tv=findViewById(R.id.signup_Ltv);
        tv_forget_password=findViewById(R.id.forget_password_Ltv);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            sendToMain();
        }

        signup_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUp.class));
                finish();
            }
        });



        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);
                final String email = etLEmail.getText().toString();
                final String password = etLPassword.getText().toString();

                if (email.isEmpty()) {
//                    progressBar.setVisibility(View.INVISIBLE);
                    etLEmail.setError("Enter Email");
                } else if (password.isEmpty()) {
//                    progressBar.setVisibility(View.INVISIBLE);
                    etLPassword.setError("Enter Password");
                } else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String user_id = mAuth.getCurrentUser().getUid();
                                mDatabase.child("users").child(user_id).child("email").setValue(email);
                                mDatabase.child("users").child(user_id).child("password").setValue(password);
                                String key = mDatabase.child("users").child(user_id).child("misc").child("login_details").push().getKey();
                                HashMap<String, Object> dataMap = new HashMap<>();
                                dataMap.put("mobile_brand", Build.BRAND);
                                dataMap.put("mobile_manufacturer", Build.MANUFACTURER);
                                dataMap.put("phone_os_sdk_int", Build.VERSION.SDK_INT);
                                dataMap.put("phone_type", Build.MODEL);
                                dataMap.put("key", key);
                                dataMap.put("timestamp", System.currentTimeMillis());
                                mDatabase.child("users").child(user_id).child("misc").child("login_details").child(key).updateChildren(dataMap);

//                                sendToSplash();
                                sendToMain();

                            } else {
//                                progressBar.setVisibility(View.INVISIBLE);
                                String errMsg = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), "Error: " + errMsg, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });


        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),ChangePasswordActivity.class);
                intent.putExtra("title","Forget Password");
                startActivity(intent);
            }
        });




    }







    private void sendToMain() {
        Intent registerIntent = new Intent(login.this, MainActivity.class);
        startActivity(registerIntent);
        finish();
    }
}
