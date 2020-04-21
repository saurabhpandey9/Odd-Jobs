package com.developerdesk9.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AddNewProductActivity extends AppCompatActivity{


    private Toolbar toolbar2;


    private EditText etAddProductName, etAddProductPrice, etAddProductDescription;
    private Button addbtn;
    private ImageView imageView5;
    private ProgressDialog pd;
    private Spinner sProductCategory;

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private StorageReference storageReference;

    private String user_id = null;
    private String email;
    private String ds_email;
    private final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;

    private String company_name;
    private String key;
    private String product_name;
    private String product_price;
    private String product_description;
    private String item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);


        toolbar2 = findViewById(R.id.toolbar2);
        toolbar2.setTitle("Add a new product");
        toolbar2.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar2.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        etAddProductName = findViewById(R.id.etAddProductName);
        etAddProductPrice = findViewById(R.id.etAddProductPrice);
        etAddProductDescription = findViewById(R.id.etAddProductDescription);
        addbtn = findViewById(R.id.addbtn);
        imageView5 = findViewById(R.id.imageView5);
        sProductCategory = findViewById(R.id.sProductCategory); //spinner

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference();

        Toast.makeText(getApplicationContext(),""+storageReference,Toast.LENGTH_LONG).show();

        List<String> categories = new ArrayList<String>();
        categories.add("Select Product Category");
        categories.add("Electronics");
        categories.add("Appliances");
        categories.add("Fashion");
        categories.add("Furniture");
        categories.add("Grocery");
        categories.add("Beauty_Care");
        categories.add("Sports");
        categories.add("Books");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sProductCategory.setAdapter(adapter);
//        sProductCategory.setOnItemClickListener(this);


        if (currentUser==null) {
            sendToLogin();
        } else {
//            user_id = mAuth.getCurrentUser().getUid();




////            getRegisteredUserDetails();
        }
    }


    private void sendToLogin() {
        Intent loginIntent = new Intent(getApplicationContext(), login.class);
        startActivity(loginIntent);
        finish();
    }

}
