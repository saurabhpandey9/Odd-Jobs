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
    private String businessflag=null;

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
        businesscheck();

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

        tv_alladdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MyaddressActivity.class));
            }
        });

        tv_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),ChangePasswordActivity.class);

                intent.putExtra("title","Change Password");
                startActivity(intent);
            }
        });

        IV_edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UpdateUserDetails.class));
            }
        });

        tv_business_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                business_page_redirect();

            }
        });

    }


    private void businesscheck(){
        mDatabase.child("BusinessAccounts").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    String accsts=dataSnapshot.child("isapproved").getValue().toString().toLowerCase().trim();

                    businessflag=accsts;

                    if (accsts.equals("yes")){
                        tv_business_account.setText("MANAGE YOUR ACCOUNT");
                        tv_business_account.setVisibility(View.VISIBLE);
                        tv_business_account.setClickable(true);
                    }
                    else if (accsts.equals("no")){
                        tv_business_account.setText("Your Account is yet to Approve\nPlease Wait");
                        tv_business_account.setTextColor(Color.GREEN);
                        tv_business_account.setClickable(false);
                        tv_business_account.setVisibility(View.VISIBLE);
                    }
                    else {
                        tv_business_account.setText("Apply for Business Account");
                        tv_business_account.setTextColor(Color.GREEN);
                        tv_business_account.setClickable(true);
                        tv_business_account.setVisibility(View.VISIBLE);
                    }


                }catch (Exception e){
                    tv_business_account.setText("Apply for Business Account");
                    tv_business_account.setTextColor(Color.GRAY);
                    tv_business_account.setClickable(true);
                    tv_business_account.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    private void Accountinfo(){

        mDatabase.child("users").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                // for User profile
                try {
                    String sname=dataSnapshot.child("name").getValue().toString();
                    String smobilen=dataSnapshot.child("mobilenumber").getValue().toString();

                    name.setText(sname);
                    mobilenumber.setText("+91 "+smobilen);



                }catch (Exception e){
                    name.setText("Update user details");
                    mobilenumber.setText("+91 "+"Not Found");
                }

                // for email

                try {
                    String semail=dataSnapshot.child("email").getValue().toString();
                    email.setText(semail);

                }catch (Exception e){

                    email.setText("Not Found");
                }


                // for address

                try {
                    String defaultname=dataSnapshot.child("DefaultAddress").child("name").getValue().toString();
                    String defaultadd=dataSnapshot.child("DefaultAddress").child("address").getValue().toString();
                    String finaladdress=defaultname+"\n"+defaultadd;
                    tv_default__address.setText(finaladdress);


                }catch (Exception e){
                    tv_default__address.setText("Please Update Address");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void business_page_redirect(){



        if (businessflag.equals("yes")){
            Toast.makeText(getApplicationContext(),"Welcome to Business Area",Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(),AddNewProductActivity.class));
        }
        else if (businessflag.equals("no")){
            Toast.makeText(getApplicationContext(),"Your Account is yet to Approve\nPlease Wait",Toast.LENGTH_LONG).show();

        }
        else {
            tv_business_account.setText("Apply for Business Account");
            tv_business_account.setTextColor(Color.GRAY);
            tv_business_account.setClickable(true);
            tv_business_account.setVisibility(View.VISIBLE);
            startActivity(new Intent(MyAccount.this,ApplyForBusinessAccActivity.class));
        }

    }

    private void sendToLogin() {
        mAuth.signOut();
        finishAffinity();
        Intent loginIntent = new Intent(getApplicationContext(),login.class);
        startActivity(loginIntent);
        finish();
    }
}
