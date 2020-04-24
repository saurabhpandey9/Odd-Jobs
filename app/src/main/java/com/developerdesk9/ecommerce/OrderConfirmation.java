package com.developerdesk9.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class OrderConfirmation extends AppCompatActivity {

    // Its pre order confirmation window

    private Toolbar toolbar8;

    private TextView tvName, tvPName, tvPPrice, tvAdd;
    private ImageView imageView11;
    private Button buybutton;

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private String user_id;
    private String email;
    private String name;
    private String address;

    private String product_key;
    private String product_name;
    private String product_price;
    private String product_description;
    private String seller_name;
    private String product_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        toolbar8 = findViewById(R.id.toolbar8);
        toolbar8.setTitle("Order Summary");
        toolbar8.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar8);
        toolbar8.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar8.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvName = findViewById(R.id.tvName);
        tvPName = findViewById(R.id.tvPName);
        tvPPrice = findViewById(R.id.tvPPrice);
        tvAdd = findViewById(R.id.tvAdd);
        buybutton = findViewById(R.id.buybutton);
        imageView11 = findViewById(R.id.imageView11);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        //product_key = bundle.get("product_key").toString();
        product_name = bundle.get("product_name").toString();
        product_price = bundle.get("product_price").toString();
        product_description = bundle.get("product_description").toString();
        seller_name = bundle.get("seller_name").toString();
        product_image = bundle.get("product_image").toString();
        name = bundle.get("name").toString();
        address = bundle.get("address").toString();


        if (currentUser==null){
            sendToLogin();
        }
        else if (bundle==null){
            onBackPressed();
        }
        else {

            Picasso.get().load(product_image).into(imageView11);
            tvAdd.setText(address);
            tvPName.setText(product_name);
            tvName.setText(name);
            tvPPrice.setText("₹ " +product_price);
            tvAdd.setText(address);

            user_id = currentUser.getUid();

            buybutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirectPrefinalPayment();
                }
            });

        }

    }


    private void redirectPrefinalPayment(){

        Intent singleproducIntent = new Intent(OrderConfirmation.this, Pre_payment_Activity.class);
        singleproducIntent.putExtra("name", name);
        singleproducIntent.putExtra("address", address);
        singleproducIntent.putExtra("total_product_count", "1");
        singleproducIntent.putExtra("total_price", product_price);
        singleproducIntent.putExtra("product_image", product_image);
        singleproducIntent.putExtra("product_name", product_name);
        singleproducIntent.putExtra("product_description", product_description);
        singleproducIntent.putExtra("company_name", seller_name);
        singleproducIntent.putExtra("order_typeflag", "single");
        startActivity(singleproducIntent);
        finish();

    }

    //Todo :: isko final payment me add karenge

    private void nextStep() {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("product_image", product_image);
        dataMap.put("product_name", product_name);
        dataMap.put("product_price", product_price);
        dataMap.put("product_description", product_description);
        dataMap.put("company_name", seller_name);
        dataMap.put("name", name);
        dataMap.put("address", address);
        dataMap.put("email", email);
        dataMap.put("user_id", user_id);

        //TODO 1. Here we will implement upi payment gateway then order placed successfully
        //TODO 2. we will do it later

        mDatabase.child("orders").child(user_id).push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Product purchased successfully", Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(OrderConfirmation.this, OrderActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(OrderConfirmation.this, login.class);
        startActivity(loginIntent);
        mAuth.signOut();
        finishAffinity();
    }
}
