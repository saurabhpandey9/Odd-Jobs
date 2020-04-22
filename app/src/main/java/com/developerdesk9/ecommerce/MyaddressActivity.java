package com.developerdesk9.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyaddressActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView addnewadd_tv;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private String user_id;
    private RecyclerView addressRV;
    private List<Address> addressList = new ArrayList<>();
    private AddressAdapter maddressAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaddress);

        addnewadd_tv=findViewById(R.id.add_newaddress_tv);
        toolbar=findViewById(R.id.toolbar_myaddress);
        toolbar.setTitle("My Address");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mContext=getApplicationContext();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        user_id=currentUser.getUid();
//
        addressRV=(RecyclerView) findViewById(R.id.myaddressRV);
        maddressAdapter=new AddressAdapter(addressList,mContext);
        addressRV.setAdapter(maddressAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        addressRV.setLayoutManager(layoutManager);



        //Todo :: User authentication need to implement

        if ((currentUser==null)){
        sendToLogin();
        }


        addnewadd_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddnewAddressActivity.class));
            }
        });


        mDatabase.child("users").child(user_id).child("Address").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    Address addresss=dataSnapshot.getValue(Address.class);
                    addressList.add(addresss);
                    maddressAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                String key = dataSnapshot.getKey();
                for (Address c : addressList) {
                    if (key.equals(c.getAddress_key())) {
                        addressList.remove(c);
                        maddressAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                Intent refreshIntent = getIntent();
                refreshIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                refreshIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(refreshIntent);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendToLogin() {
        mAuth.signOut();
        finishAffinity();
        Intent loginIntent = new Intent(getApplicationContext(),login.class);
        startActivity(loginIntent);
        finish();
    }
}
