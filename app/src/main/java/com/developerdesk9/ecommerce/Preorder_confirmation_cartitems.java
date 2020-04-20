package com.developerdesk9.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Preorder_confirmation_cartitems extends AppCompatActivity {

    private Toolbar toolbar13;
    private TextView tvCBName, tvCBAddress, tvCBTItems, tvCBTPrice;
    private Button button11;

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private String name;
    private String address;
    private String total_price;
    private String total_product_count;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preorder_confirmation_cartitems);

        Bundle bundle = getIntent().getExtras();
        name = bundle.get("name").toString();
        address = bundle.get("address").toString();
        total_price = bundle.get("total_price").toString();
        total_product_count = bundle.get("total_product_count").toString();

        toolbar13 = findViewById(R.id.toolbar13);
        toolbar13.setTitle("Pre-Order Confirmation");
        toolbar13.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar13);
        toolbar13.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar13.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvCBName = findViewById(R.id.tvCBName);
        tvCBAddress = findViewById(R.id.tvCBAddress);
        tvCBTItems = findViewById(R.id.tvCBTItems);
        tvCBTPrice = findViewById(R.id.tvCBTPrice);
        button11 = findViewById(R.id.button11);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            user_id = currentUser.getUid();
            button11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getProductShipToOrders();
                }
            });

        } else {
            sendToLogin();
        }
        
        tvCBName.setText(name);
        tvCBAddress.setText(address);
        tvCBTItems.setText(total_product_count);
        tvCBTPrice.setText("₹ " + total_price);
    }

    private void getProductShipToOrders() {
        mDatabase.child("cart").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object producut_name = map.get("product_name");
                    Object product_image = map.get("product_image");
                    Object seller_name = map.get("seller_name");
                    Object cart_key = map.get("cart_key");
                    Object product_description = map.get("product_description");
                    Object product_price = map.get("product_price");

                    mDatabase.child("orders").child(user_id).push().setValue(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child("cart").child(user_id).setValue(null);
        Toast.makeText(getApplicationContext(), "Product purchased successfully", Toast.LENGTH_LONG).show();

        // Todo 1. Ordered  Items list yet to built

        Intent orderIntent = new Intent(Preorder_confirmation_cartitems.this, OrderActivity.class);
        startActivity(orderIntent);
        finish();

    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(Preorder_confirmation_cartitems.this, login.class);
        startActivity(loginIntent);
        finish();
    }
}
