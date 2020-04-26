package com.developerdesk9.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UpdateUserDetails extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private String user_id=null;

    private TextInputLayout update1;
    private TextInputLayout update2;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_details);


        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("Updating..");
        progressDialog.setCanceledOnTouchOutside(false);

        update1=findViewById(R.id.update_info_tv1);
        update2=findViewById(R.id.update_info_tv2);
        button=findViewById(R.id.btn_update_info);


        toolbar=findViewById(R.id.toolbar_updateinfo);
        toolbar.setTitle("Update Info");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (currentUser == null) {
            sendToLogin();
        }
        user_id = currentUser.getUid();
        progressDialog.show();

        mDatabase.child("users").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    update1.getEditText().setText(dataSnapshot.child("name").getValue().toString());
                    update2.getEditText().setText(dataSnapshot.child("mobilenumber").getValue().toString());
                    progressDialog.dismiss();
                }catch (Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateinfo();
            }
        });

    }

    private void updateinfo(){
        String supdate1=update1.getEditText().getText().toString().trim();
        String supdate2=update2.getEditText().getText().toString().trim();

        if (supdate1.isEmpty()){
            update1.setError("Enter name");
            return;
        }
        if (supdate2.isEmpty()){
            update2.setError("Enter Mobile Number");
            return;
        }
        else {
            if (supdate2.length()!=10){
                update2.setError("Invalid Number");
                return;
            }
        }
        progressDialog.show();

        Map<String, Object> data=new HashMap<>();
        data.put("name",supdate1);
        data.put("mobilenumber",supdate2);


        mDatabase.child("users").child(user_id).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    progressDialog.dismiss();
                    finish();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Failed!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendToLogin() {
        mAuth.signOut();
        Intent loginIntent = new Intent(getApplicationContext(),login.class);
        startActivity(loginIntent);
        finishAffinity();
    }
}
