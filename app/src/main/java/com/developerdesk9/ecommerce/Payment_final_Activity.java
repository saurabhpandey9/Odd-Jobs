package com.developerdesk9.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Payment_final_Activity extends AppCompatActivity {

    private Toolbar toolbar;



    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private String user_id;

    private String name;
    private String address;
    private String total_price;
    private String total_product_count;
    private String Orderid="Cancelled";

    //This one is extra from cart
    private String ordertypeflag;
    private String product_image;
    private String product_name;
    private String product_description;
    private String company_name;


    private TextView tv_itemcount;
    private TextView tv_price1;
    private TextView tv_totalamount;
    private TextView tv_orderID;
    private Button button;
    private String radioflag=null;

    private String order_status;

    private EasyUpiPayment easyUpiPayment;
    private LinearLayout linearLayout;

    private ProgressDialog progressDialog;
    private String currentDateTimeString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_final_);

        currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Please don't close App or press the back button");
        progressDialog.setCanceledOnTouchOutside(false);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        toolbar=findViewById(R.id.toolbar_final_payment);
        toolbar.setTitle("Payments");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                back_cancel_press_btn_response();
            }
        });

        Bundle bundle = getIntent().getExtras();
        name = bundle.get("name").toString();
        address = bundle.get("address").toString();
        total_price = bundle.get("total_price").toString();
        total_product_count = bundle.get("total_product_count").toString();
        Orderid=bundle.get("order_id").toString();
        ordertypeflag=bundle.get("order_typeflag").toString();
        product_image=bundle.get("product_image").toString();
        product_name=bundle.get("product_name").toString();
        product_description=bundle.get("product_description").toString();
        company_name=bundle.get("company_name").toString();

        if (currentUser==null){
            sendToLogin();

        }
        else if (bundle.isEmpty()){
            onBackPressed();
        }

        else {
            user_id=currentUser.getUid();
        }


        tv_itemcount=findViewById(R.id.final_payment_item_count);
        tv_price1=findViewById(R.id.final_payment_price);
        tv_totalamount=findViewById(R.id.final_payment_amount_payable); // these both are same as delivery charges case is not
        tv_orderID=findViewById(R.id.final_payment_order_id);
        tv_itemcount.setText(total_product_count);
        tv_price1.setText("₹"+total_price);
        tv_totalamount.setText("₹"+total_price);
        tv_orderID.setText("TID"+Orderid);

        //over showoff

        button=findViewById(R.id.payment_btn);

        RadioGroup rb = findViewById(R.id.myRadioGroup);
        rb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                switch (checkedId) {

                    case R.id.radiobtn1:
                        button.setText("Place Order");
                        button.setVisibility(View.VISIBLE);
                        radioflag="COD";
                        break;
                    case R.id.radiobtn2:
                        button.setText("Pay");
                        button.setVisibility(View.VISIBLE);
                        radioflag="UPI";
                        break;
                     default:
                         radioflag="Nothing Selected";

                }
            }

        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                orderprocessing();
            }
        });

    }

    private void orderprocessing(){

        if (radioflag.equals("COD")){
            order_status="COD";
            callcode();
        }
        else if (radioflag.equals("UPI")){

            upipayment();

        }

    }


    private void callcode(){

        // When you will usse COD then this function will revoke

        if (ordertypeflag.equals("single")){

            Map<String ,Object> data=new HashMap<>();
            data.put("order_status",order_status);

            mDatabase.child("TransactionDetails").child(Orderid).updateChildren(data);

            singleproductorder();
        }

        else if (ordertypeflag.equals("cart")){

            Map<String ,Object> data=new HashMap<>();
            data.put("order_status",order_status);
            mDatabase.child("TransactionDetails").child(Orderid).updateChildren(data);
            orderbycart();
        }
    }



    // This method will invoke in case of success/ failure of upi payment


    private void callpayment(){

        if (ordertypeflag.equals("single")){

            singleproductorder();
        }

        else if (ordertypeflag.equals("cart")){

            orderbycart();
        }

    }



    private void singleproductorder(){

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("product_image", product_image);
        dataMap.put("product_name", product_name);
        dataMap.put("product_price", total_price);
        dataMap.put("product_description", product_description);
        dataMap.put("company_name", company_name);
        dataMap.put("name", name);
        dataMap.put("address", address);
        dataMap.put("user_id", user_id);
        dataMap.put("order_status",order_status);
        dataMap.put("order_id",Orderid);
        dataMap.put("order_date",currentDateTimeString);

        Map<String ,Object> data=new HashMap<>();
        data.put("order_status",order_status);
        data.put("tr_date",currentDateTimeString);

        mDatabase.child("TransactionDetails").child(Orderid).updateChildren(data);

     // here orderID is unique key and acting as transaction ID too
        mDatabase.child("orders").child(user_id).child(Orderid).setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Order Placed successfully", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                Intent mainIntent = new Intent(Payment_final_Activity.this, TransactionActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

    }


    private void orderbycart(){

        Map<String ,Object> data=new HashMap<>();
        data.put("order_status",order_status);
        data.put("tr_date",currentDateTimeString);
        mDatabase.child("TransactionDetails").child(Orderid).updateChildren(data);
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

                    map.put("order_status",order_status);
                    map.put("address",address);
                    map.put("name",name);
                    map.put("order_id",Orderid);
                    map.put("order_date",currentDateTimeString);
                    mDatabase.child("orders").child(user_id).push().setValue(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child("cart").child(user_id).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Order Placed successfully", Toast.LENGTH_LONG).show();
        Intent orderIntent = new Intent(Payment_final_Activity.this, TransactionActivity.class);
        startActivity(orderIntent);
        finish();

    }

    @Override
    public void onBackPressed() {
        order_status="Cancelled";
        back_cancel_press_btn_response();
    }

    private void upipayment(){

         easyUpiPayment = new EasyUpiPayment.Builder()
                .with(Payment_final_Activity.this)
                .setPayeeVpa("helpdesk@barodampay")
                .setPayeeName("Saurabh Pandey")
                .setTransactionId("TI"+Orderid)
                .setTransactionRefId("TRI"+Orderid)
                .setDescription("Paying to Odd Jobs Pvt Ltd")
                .setAmount((total_price+".00").trim())
                .build();

        easyUpiPayment.startPayment();
        progressDialog.show();

        easyUpiPayment.setPaymentStatusListener(new PaymentStatusListener() {
            @Override
            public void onTransactionCompleted(TransactionDetails transactionDetails) {

            }

            @Override
            public void onTransactionSuccess() {


                order_status="SUCCESS";
                easyUpiPayment.detachListener();
                callpayment();
            }

            @Override
            public void onTransactionSubmitted() {

                Toast.makeText(getApplicationContext(),"Transaction Started",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTransactionFailed() {

                order_status="FAILED";
                easyUpiPayment.detachListener();
                transaction_fail_cancel_case();
                Toast.makeText(getApplicationContext(),"Transaction Failed",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTransactionCancelled() {
                progressDialog.dismiss();


                easyUpiPayment.detachListener();
                Toast.makeText(getApplicationContext(),"Payment Cancelled",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAppNotFound() {
                progressDialog.dismiss();

                Toast.makeText(getApplicationContext(),"App not found Please Try another option",Toast.LENGTH_LONG).show();
            }
        });

    }


    private void transaction_fail_cancel_case(){
        progressDialog.show();
        Map<String ,Object> data=new HashMap<>();
        data.put("order_status",order_status);
        data.put("tr_date",currentDateTimeString);
        mDatabase.child("TransactionDetails").child(Orderid).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Transaction Failed..",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(),TransactionActivity.class));
                    finish();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    public void back_cancel_press_btn_response(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Transaction!");
        builder.setMessage("Are you sure you want cancel transaction");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                order_status="Cancelled";
                transaction_fail_cancel_case();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();  // Show the Alert Dialog box

    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(Payment_final_Activity.this, login.class);
        startActivity(loginIntent);
        finishAffinity();
    }


}
