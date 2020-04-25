package com.developerdesk9.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChecksumActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private String total_price = " ";
    private String total_product_count = " ";
    private String user_id = null;
    public String recipient_name;
    public String recipient_address;

    private String product_key = "";
    private String product_name = "";
    private String product_price = "";
    private String product_description = "";
    private String seller_name = "";
    private String product_image = "";
    private String product_category = "";
    private String from_cart;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checksum);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        user_id=currentUser.getUid();


        textView=findViewById(R.id.tv_checksum);

        // bucket flag to check buynow/ cart redirect
        Bundle bundle = getIntent().getExtras();
        from_cart = bundle.get("from_cart").toString();

        if (from_cart.equals("no")) {
            product_name = bundle.get("product_name").toString();
            product_price = bundle.get("product_price").toString();
            product_description = bundle.get("product_description").toString();
            seller_name = bundle.get("company_name").toString();
            product_image = bundle.get("product_image").toString();
        }

        else if (from_cart.equals("yes")){
            total_price = bundle.get("total_price").toString();
            total_product_count = bundle.get("total_product_count").toString();
        }


        if (currentUser == null) {
            user_id = currentUser.getUid();
        } else {

            textView.setText("Fetching Details");

            mDatabase.child("users").child(user_id).child("DefaultAddress").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){
                        recipient_name=dataSnapshot.child("name").getValue().toString();
                        recipient_address=dataSnapshot.child("address").getValue().toString();

                        checksumredirect(recipient_name,recipient_address);
                    }
                    else {
                        textView.setText("Address Not Found !\nPlease Update default address");
                        textView.setTextColor(Color.RED);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }

    private void checksumredirect(String rname,String raddress){



        if (rname.isEmpty()  && raddress.isEmpty()){
            textView.setText("Default Address Not found !\n Please ");
            textView.setTextColor(Color.RED);
        }

        else if (from_cart.equals("yes")) {
//
                //redirect to pre-order confirmation for cart items
                Intent cartBuyIntent = new Intent(getApplicationContext(), Preorder_confirmation_cartitems.class);
                cartBuyIntent.putExtra("total_product_count", String.valueOf(total_product_count));
                cartBuyIntent.putExtra("total_price", String.valueOf(total_price));
                cartBuyIntent.putExtra("name", rname);
                cartBuyIntent.putExtra("address", raddress);
                startActivity(cartBuyIntent);
                finish();


        }


        else if (from_cart.equals("no")){

                Intent productIntent = new Intent(getApplicationContext(), OrderConfirmation.class);
                //productIntent.putExtra("product_key", );
                productIntent.putExtra("product_name", product_name);
                productIntent.putExtra("product_price", product_price);
                productIntent.putExtra("product_image", product_image);
                productIntent.putExtra("product_description", product_description);
                productIntent.putExtra("company_name", seller_name);
                productIntent.putExtra("name", rname);
                productIntent.putExtra("address", raddress);
                startActivity(productIntent);
                finish();
        }

    }



}
