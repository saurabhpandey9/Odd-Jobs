package com.developerdesk9.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;
public class ApplyForBusinessAccActivity extends AppCompatActivity {

    Toolbar toolbar;

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private String user_id;

    private TextInputLayout tv_apply_business1;
    private TextInputLayout tv_apply_business2;
    private TextInputLayout tv_apply_business3;
    private TextInputLayout tv_apply_business4;
    private TextInputLayout tv_apply_business5;
    private TextInputLayout tv_apply_business6;
    private Button bt_apply_business;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_business_acc);

        toolbar=findViewById(R.id.toolbar_apply_business_accout);
        toolbar.setTitle("Apply for Business Account");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_apply_business1=findViewById(R.id.newbusiness_account1);
        tv_apply_business2=findViewById(R.id.newbusiness_account2);
        tv_apply_business3=findViewById(R.id.newbusiness_account3);
        tv_apply_business4=findViewById(R.id.newbusiness_account4);
        tv_apply_business5=findViewById(R.id.newbusiness_account5);
        tv_apply_business6=findViewById(R.id.newbusiness_account6);
        bt_apply_business=findViewById(R.id.btn_newbusiness_account);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        if (currentUser==null){
            sendToLogin();
        }else {
            user_id=currentUser.getUid();
            bt_apply_business.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    apply_businessacc();
                }
            });

        }

    }

    private void apply_businessacc(){

        String string_apply_business1=tv_apply_business1.getEditText().getText().toString().trim();
        String string_apply_business2=tv_apply_business2.getEditText().getText().toString().trim();
        String string_apply_business3=tv_apply_business3.getEditText().getText().toString().trim();
        String string_apply_business4=tv_apply_business4.getEditText().getText().toString().trim();
        String string_apply_business5=tv_apply_business5.getEditText().getText().toString().trim();
        String string_apply_business6=tv_apply_business6.getEditText().getText().toString().trim();

        if (string_apply_business1.isEmpty()){
            tv_apply_business1.setError("Can't be Empty");
            return;

        }
         if (string_apply_business2.isEmpty()){
            tv_apply_business2.setError("Can't be Empty");
            return;

        }
         if (string_apply_business3.isEmpty()){
            tv_apply_business3.setError("Can't be Empty");
            return;

        }
         if (string_apply_business4.isEmpty()){
            tv_apply_business4.setError("Can't be Empty");
            return;

        }
         if (string_apply_business5.isEmpty()){
            tv_apply_business5.setError("Can't be Empty");
            return;

        }
         if (string_apply_business6.isEmpty()){
            tv_apply_business6.setError("Can't be Empty");
            return;

        }
        else if (string_apply_business6.length()<=4){
            tv_apply_business6.setError("Please Enter Strong Password");
            return;
        }

        Map<String,Object> data=new HashMap();
        data.put("company_owner",string_apply_business1);
        data.put("company_name",string_apply_business2);
        data.put("office_address",string_apply_business3);
        data.put("office_phoneno",string_apply_business4);
        data.put("gstin",string_apply_business5);
        data.put("transactionpass",string_apply_business6);

        data.put("isapproved","no");
        data.put("company_key",user_id);

        mDatabase.child("BusinessAccounts").child(user_id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Applied Successfully",Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),"Please Wait for 15 day until we verify your data",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),SplashScreen.class));
                    finishAffinity();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
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
