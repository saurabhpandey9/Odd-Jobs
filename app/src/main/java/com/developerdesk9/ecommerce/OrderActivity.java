package com.developerdesk9.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private Toolbar toolbar10;

    private TextView textView34;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;

    private String user_id;

    private RecyclerView orderRV;
    private List<Order> orderList = new ArrayList<>();
    private OrderAdapter mOrderAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);



        mContext =getApplicationContext();
//
//        toolbar10 = findViewById(R.id.toolbar10);
//        toolbar10.setTitle("Your orders");
//        setSupportActionBar(toolbar10);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        toolbar10.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_launcher));
//        toolbar10.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

        textView34 = findViewById(R.id.textView34);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        orderRV = findViewById(R.id.orderRV);

        orderRV = findViewById(R.id.orderRV);
        mOrderAdapter = new OrderAdapter(orderList, mContext);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        orderRV.setLayoutManager(layoutManager);
        orderRV.setAdapter(mOrderAdapter);

        if (currentUser == null) {

        } else {
            user_id = currentUser.getUid();
            mDatabase.child("orders").child(user_id).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.exists()) {
                        textView34.setVisibility(View.INVISIBLE);
                        Order order = dataSnapshot.getValue(Order.class);
                        orderList.add(order);
                        mOrderAdapter.notifyDataSetChanged();
                    } else {
                        textView34.setVisibility(View.VISIBLE);
                    }


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }
}
