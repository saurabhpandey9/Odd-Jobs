package com.developerdesk9.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

public class Payment_final_Activity extends AppCompatActivity {

    private Toolbar toolbar;

    private Button button;

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private String user_id;

    private String name;
    private String address;
    private String total_price;
    private String total_product_count;
    private String Orderid;

    //This one is extra from cart
    private String ordertypeflag;
    private String product_image;
    private String product_name;
    private String product_description;
    private String company_name;


    private TextView tv_itemcount;
    private TextView tv_price1;
    private TextView tv_totalamount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_final_);


        toolbar=findViewById(R.id.toolbar_final_payment);
        toolbar.setTitle("Payments");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitle("ok hai");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
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

        //Payment Info show kiya hai
        tv_itemcount=findViewById(R.id.final_payment_item_count);
        tv_price1=findViewById(R.id.final_payment_price);
        tv_totalamount=findViewById(R.id.final_payment_amount_payable); // these both are same as delivery charges case is not


        tv_itemcount.setText(total_product_count);
        tv_price1.setText("₹"+total_price);
        tv_totalamount.setText("₹"+total_price);

        //over showoff


        button=findViewById(R.id.payment_btn);


        RadioGroup rb = (RadioGroup) findViewById(R.id.myRadioGroup);
        rb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                switch (checkedId) {

                    case R.id.radiobtn1:

                        Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radiobtn2:
                        Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();
                        break;
                     default:


                }
            }

        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upipayment();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void upipayment(){

        final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                .with(this)
                .setPayeeVpa("helpdesk@barodampay")
                .setPayeeName("Saurabh Pandey")
                .setTransactionId("1234567199uu454s5dd58884")
                .setTransactionRefId("1234567857845s45dd99435353")
                .setDescription("aail")
                .setAmount("2.00")
                .build();

        easyUpiPayment.startPayment();

        easyUpiPayment.setPaymentStatusListener(new PaymentStatusListener() {
            @Override
            public void onTransactionCompleted(TransactionDetails transactionDetails) {

                Log.d("saurabhp",""+transactionDetails);
            }

            @Override
            public void onTransactionSuccess() {

                Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTransactionSubmitted() {

                Toast.makeText(getApplicationContext(),"submitted",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTransactionFailed() {

                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTransactionCancelled() {

                Toast.makeText(getApplicationContext(),"Cancelled",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAppNotFound() {

                Toast.makeText(getApplicationContext(),"App not found",Toast.LENGTH_LONG).show();
            }
        });

    }







}
