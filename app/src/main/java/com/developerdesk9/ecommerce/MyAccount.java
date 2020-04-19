package com.developerdesk9.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAccount extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView name;
    private TextView mobilenumber;
    private TextView email;
    private TextView tv_myordere;
    private TextView tv_prime_membership;
    private TextView tv_alladdress;
    private TextView tv_default__address;
    private TextView tv_business_account;
    private TextView tv_changepassword;
    private ImageView IV_edit_info;

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private String user_id=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        toolbar=findViewById(R.id.toolbar_myaccount);
        toolbar.setTitle("My Account");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });


        name=findViewById(R.id.name_my_account);
        mobilenumber=findViewById(R.id.tv_myaccount_mobile_number);
        email=findViewById(R.id.tv_myaccount_email_address);
        tv_myordere=findViewById(R.id.tv_my_account_myorder);
        tv_prime_membership=findViewById(R.id.tv_myaccount_prime_membership);
        tv_alladdress=findViewById(R.id.tv_myaccount_myaddress);
        tv_default__address=findViewById(R.id.tv_default_address_my_account);
        tv_business_account=findViewById(R.id.tv_myaccount_business_account);
        tv_changepassword=findViewById(R.id.tv_myaccount_change_password);
        IV_edit_info=findViewById(R.id.IV_myaccount_edit);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (currentUser == null) {
            sendToLogin();
        }

        user_id = currentUser.getUid();


        Accountinfo();

        tv_prime_membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_prime_membership.setText("Your are Not Prime Member");
                tv_prime_membership.setTextColor(Color.RED);
            }
        });

        tv_myordere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),OrderActivity.class));
            }
        });

        IV_edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAccountinfo();
                Toast.makeText(getApplicationContext(),"Edit Please",Toast.LENGTH_LONG).show();
            }
        });
        tv_alladdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MyaddressActivity.class));
            }
        });


    }

    private void updateAccountinfo(){

    }

    private void Accountinfo(){

        mDatabase.child("users").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String sname=dataSnapshot.child("name").getValue().toString();
                String smobilen=dataSnapshot.child("mobilenumber").getValue().toString();
                String semail=dataSnapshot.child("email").getValue().toString();
                String defaultname=dataSnapshot.child("DefaultAddress").child("name").getValue().toString();
                String defaultadd=dataSnapshot.child("DefaultAddress").child("address").getValue().toString();

                String finaladdress=defaultname+"\n"+defaultadd;

                name.setText(sname);
                mobilenumber.setText(smobilen);
                email.setText(semail);
                tv_default__address.setText(finaladdress);

                // Todo : Account type need to be added
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendToLogin() {
        mAuth.signOut();
        Intent loginIntent = new Intent(getApplicationContext(),login.class);
        startActivity(loginIntent);
        finish();
    }
}
