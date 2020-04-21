package com.developerdesk9.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddNewProductActivity extends AppCompatActivity{


    private Toolbar toolbar2;


    private EditText etAddProductName, etAddProductPrice, etAddProductDescription;
    private Button addbtn;
    private ImageView imageView5;
//    private ProgressDialog pd;
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
    private String company_key;
    private String product_name;
    private String product_price;
    private String product_description;
    private String item="Electronics";

    private LinearLayout llr;


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
        llr=findViewById(R.id.addll);
        etAddProductDescription = findViewById(R.id.etAddProductDescription);
        addbtn = findViewById(R.id.addbtn);
        imageView5 = findViewById(R.id.imageView5);
        sProductCategory = findViewById(R.id.sProductCategory); //spinner

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference();


//        List<String> categories = new ArrayList<String>();
//        categories.add("Select Product Category");
//        categories.add("Electronics");
//        categories.add("Appliances");
//        categories.add("Fashion");
//        categories.add("Furniture");
//        categories.add("Grocery");
//        categories.add("Beauty_Care");
//        categories.add("Sports");
//        categories.add("Books");
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_dropdown_item, categories);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sProductCategory.setAdapter(adapter);
//        sProductCategory.setOnItemClickListener(this);


        if (currentUser==null) {
            sendToLogin();
        } else {
            user_id = mAuth.getCurrentUser().getUid();


            mDatabase.child("users").child(user_id).child("account_type").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        String sts=dataSnapshot.getValue().toString().toLowerCase().trim();

                        if (sts=="customer"){
                            mAuth.signOut();
                            sendToLogin();
                            finishAffinity();
                        }
                        else {
                            llr.setVisibility(View.VISIBLE);

                        }
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Unauthorised!!",Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                        sendToLogin();
                        finishAffinity();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            imageView5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
                }
            });


            addbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"btn",Toast.LENGTH_LONG).show();
                    addproduct();
                }
            });


        }




    }

    private void addproduct(){

        product_name = etAddProductName.getText().toString();
        product_price = etAddProductPrice.getText().toString();
        product_description = etAddProductDescription.getText().toString();

        if (product_name.isEmpty()) {
            etAddProductName.setError("Enter product name");
        } else if (product_price.isEmpty()) {
            etAddProductPrice.setError("Enter product price");
        } else if (product_description.isEmpty()) {
            etAddProductDescription.setError("Enter product description");
        } else  if (item.equals("Select Category")) {
            Toast.makeText(getApplicationContext(), "Please select a category", Toast.LENGTH_LONG).show();
        } else {

            company_key=user_id;

            mDatabase.child("BusinessAccounts").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        try {
                            company_name=dataSnapshot.child("company_name").getValue().toString().trim();
                           uploadDetails();

                        }catch (Exception e){

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }



    // for image URL
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                Picasso.get().load(filePath).into(imageView5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadDetails() {
        if(filePath != null) {
//            pd.show();
            final Long ts_long = System.currentTimeMillis()/1000;
            final String ts = ts_long.toString();
            final StorageReference childRef = storageReference.child("product_images/" + ts + ".jpg");

            //uploading the image
            final UploadTask uploadTask = childRef.putFile(filePath);

            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
//                        pd.dismiss();
                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return childRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
//                                pd.dismiss();
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String mUri = downloadUri.toString();
                                    Toast.makeText(getApplicationContext(), mUri, Toast.LENGTH_LONG).show();
                                    final String product_key = mDatabase.child("products").child(item).push().getKey();
                                    final HashMap<String, Object> dataMap = new HashMap<>();
                                    dataMap.put("product_image", mUri);
                                    dataMap.put("product_name", product_name);
                                    dataMap.put("product_price", product_price);
                                    dataMap.put("product_description", product_description);
                                    dataMap.put("company_key", company_key);
                                    dataMap.put("company_name", company_name);
                                    dataMap.put("product_added_time", ts_long);
                                    dataMap.put("product_category", item);
                                    dataMap.put("product_key", product_key);
                                    mDatabase.child("products").child(item).child(product_key).setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mDatabase.child("business").child(company_key).child("products").child(product_key).setValue(dataMap);
                                                Toast.makeText(getApplicationContext(), "Product added successfully", Toast.LENGTH_LONG).show();
                                                Intent settingsIntent = new Intent(getApplicationContext(), AddNewProductActivity.class);
                                                startActivity(settingsIntent);
                                                Toast.makeText(getApplicationContext(), "Add More Product", Toast.LENGTH_LONG).show();
                                                finish();

                                            } else {
                                                String errMsg = task.getException().getMessage();
                                                Toast.makeText(getApplicationContext(), "Error: " + errMsg, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else {
                                    // Handle failures
                                    // ...
                                    String errMsg = task.getException().getMessage();
                                    Toast.makeText(getApplicationContext(), "Download Uri Error: " + errMsg, Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        String errMsg = task.getException().getMessage();
                        Toast.makeText(getApplicationContext(), "Error: " + errMsg, Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Upload Failed: " + e, Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Select an image", Toast.LENGTH_SHORT).show();
        }
    }


    private void sendToLogin() {
        Intent loginIntent = new Intent(getApplicationContext(), login.class);
        startActivity(loginIntent);
        mAuth.signOut();
        finishAffinity();
    }

}
