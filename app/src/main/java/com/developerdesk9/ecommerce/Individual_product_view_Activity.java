package com.developerdesk9.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Individual_product_view_Activity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView tvPName, tvPPrice, tvPDesc, tvSName;
    private ImageView productIV;
    private Button button2,button;

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private String user_id;
    private String email_id;

    private String product_key;
    private String product_name;
    private String product_price;
    private String product_description;
    private String seller_name;
    private String product_image;
    private String product_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_product_view_);



        toolbar = findViewById(R.id.toolbar_individual_product);
        toolbar.setTitle("Product");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        tvPName = findViewById(R.id.tvPName);
        tvPPrice = findViewById(R.id.tvPPrice);
        tvPDesc = findViewById(R.id.tvPDesc);
        tvSName = findViewById(R.id.tvSName);
        productIV = findViewById(R.id.productIV);
        button2 = findViewById(R.id.button2);
        button = findViewById(R.id.button);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        product_key = bundle.get("product_key").toString().trim();
        product_category = bundle.get("product_category").toString().trim();


        if (currentUser == null) {
            sendToLogin();
        } else {
            if (bundle!= null) {
                user_id = currentUser.getUid();
                mDatabase.child("products").child(product_category).child(product_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        product_description = dataSnapshot.child("product_description").getValue().toString();
                        product_name = dataSnapshot.child("product_name").getValue().toString();
                        product_price = dataSnapshot.child("product_price").getValue().toString();
                        product_image = dataSnapshot.child("product_image").getValue().toString();
                        seller_name = dataSnapshot.child("company_name").getValue().toString();

                        showProduct();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productIntent = new Intent(Individual_product_view_Activity.this, ChecksumActivity.class);
                        //productIntent.putExtra("product_key", );
                        productIntent.putExtra("product_name", product_name);
                        productIntent.putExtra("product_price", product_price);
                        productIntent.putExtra("product_image", product_image);
                        productIntent.putExtra("product_description", product_description);
                        productIntent.putExtra("company_name", seller_name);
                        productIntent.putExtra("from_cart", "no");
                        startActivity(productIntent);
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveProductToCart();
                    }
                });
            } else {
                sendToMain();
            }
        }

    }

    private void showProduct() {
        Picasso.get().load(product_image).fit().into(productIV);
        toolbar.setTitle(product_name);
        tvPName.setText(product_name);
        tvSName.setText("by " + seller_name);
        String newNumber = CommaSeperate.getFormatedNumber(product_price);
        tvPPrice.setText("₹" + newNumber);
        tvPDesc.setText(product_description);
    }


    private void saveProductToCart() {
        String cart_key = mDatabase.child("cart").child(user_id).push().getKey();
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("product_name", product_name);
        dataMap.put("product_price", product_price);
        dataMap.put("product_image", product_image);
        dataMap.put("company_name", seller_name);
        dataMap.put("cart_key", cart_key);
        dataMap.put("product_description", product_description);
        mDatabase.child("cart").child(user_id).child(cart_key).updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100);
                View parentLayout = findViewById(android.R.id.content);
                Snackbar snackbar=Snackbar.make(parentLayout,product_name+" | Added",Snackbar.LENGTH_LONG);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setActionTextColor(Color.BLUE);
                snackbar.show();
            }
        });
    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(Individual_product_view_Activity.this, login.class);
        startActivity(loginIntent);
        finishAffinity();
    }

    private void sendToMain() {
        Intent registerIntent = new Intent(Individual_product_view_Activity.this, MainActivity.class);
        startActivity(registerIntent);
        finish();
    }
}
