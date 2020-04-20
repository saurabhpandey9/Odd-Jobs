package com.developerdesk9.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddnewAddressActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextInputLayout address1;
    private TextInputLayout address2;
    private TextInputLayout address3;
    private TextInputLayout address4;
    private TextInputLayout address5;
    private TextInputLayout address6;
    private TextInputLayout address7;
    private Button button_save_address;


    private String saddressnew1;
    private String saddressnew2;
    private String saddressnew3;
    private String saddressnew4;
    private String saddressnew5;
    private String saddressnew6;
    private String saddressnew7;


    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew_address);



        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user_id=currentUser.getUid();


        toolbar=findViewById(R.id.toolbar_addnewaddress);
        toolbar.setTitle("Add a new Address");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


         address1=findViewById(R.id.address_new_tv_1);
         address2=findViewById(R.id.address_new_tv_2);
         address3=findViewById(R.id.address_new_tv_3);
         address4=findViewById(R.id.address_new_tv_4);
         address5=findViewById(R.id.address_new_tv_5);
         address6=findViewById(R.id.address_new_tv_6);
         address7=findViewById(R.id.address_new_tv_7);
        button_save_address=findViewById(R.id.btn_save_new_address);


        button_save_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendaddress();
            }
        });


    }

    public void sendaddress(){

        saddressnew1=address1.getEditText().getText().toString();
        saddressnew2=address2.getEditText().getText().toString();
        saddressnew3=address3.getEditText().getText().toString();
        saddressnew4=address4.getEditText().getText().toString();
        saddressnew5=address5.getEditText().getText().toString();
        saddressnew6=address6.getEditText().getText().toString();
        saddressnew7=address7.getEditText().getText().toString();



        if(saddressnew1.isEmpty()){
            address1.setError("Can't be empty");
            return;
        }
        if(saddressnew2.isEmpty()){
            address2.setError("Can't be empty");
            return;
        }
        else {
            if(saddressnew2.length()!=10){
                address2.setError("Mobile Number is Incorrect");
                return;
            }
        }

        if(saddressnew3.isEmpty()){
            address3.setError("Can't be empty");
            return;
        }
        if(saddressnew4.isEmpty()){
            address4.setError("Can't be empty");
            return;
        }
        if(saddressnew5.isEmpty()){
            address5.setError("Can't be empty");
            return;
        }
        if(saddressnew6.isEmpty()){
            address6.setError("Can't be empty");
            return;
        }
        if(saddressnew7.isEmpty()){
            address7.setError("Can't be empty");
            return;
        }


        String key=mDatabase.child("users").child(user_id).child("Address").push().getKey();

        String address=saddressnew3+",\n"+saddressnew4+","+saddressnew5+",\n"+saddressnew6+"-"+saddressnew7+"\n+91 "+saddressnew2;
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", saddressnew1);
        dataMap.put("address", address);
        dataMap.put("address_key", key);



        mDatabase.child("users").child(user_id).child("Address").child(key).updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Address Add",Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Failed...",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
