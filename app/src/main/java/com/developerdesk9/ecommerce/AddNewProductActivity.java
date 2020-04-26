package com.developerdesk9.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddNewProductActivity extends AppCompatActivity {


    private Toolbar toolbar2;
    private EditText etAddProductName, etAddProductPrice, etAddProductDescription;
    private Button addbtn;
    private ImageView imageView5;


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
    private String product_key;
    private String product_name;
    private String product_price;
    private String product_description;
    private String item=null;
    private LinearLayout llr;

    private ArrayList<String> arrylist;
    private Spinner spinner;

    private ProgressDialog progressDoalog;

    private String imageURLstring=null;
    private int imagecompressfactor=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);


        progressDoalog = new ProgressDialog(AddNewProductActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please wait...");
        progressDoalog.setTitle("File is Uploading");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDoalog.setCanceledOnTouchOutside(false);


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
        spinner = findViewById(R.id.sProductCategory); //spinner

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference();


        arrylist = new ArrayList<String>();
        arrylist.add("Select Product Category");
        arrylist.add("Electronics");
        arrylist.add("Appliances");
        arrylist.add("Fashion");
        arrylist.add("Furniture");
        arrylist.add("Grocery");
        arrylist.add("Beauty_Care");
        arrylist.add("Sports");
        arrylist.add("Books");

        ArrayAdapter<String> adapter;
        adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,arrylist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (currentUser==null) {
            sendToLogin();
        } else {
            user_id = mAuth.getCurrentUser().getUid();


            mDatabase.child("BusinessAccounts").child(user_id).child("isapproved").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        String sts=dataSnapshot.getValue().toString().toLowerCase().trim();

                        if (sts.equals("no")){
                            mAuth.signOut();
                            sendToLogin();
                            finishAffinity();
                        }
                        else if(sts.equals("yes")) {
                            llr.setVisibility(View.VISIBLE);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please Contact Customer Care",Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            sendToLogin();
                            finishAffinity();
                        }
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Please Contact Customer Care",Toast.LENGTH_LONG).show();
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
                    addproduct();
                }
            });


        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item=String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
        } else  if (item.equals("Select Product Category")) {
            Toast.makeText(getApplicationContext(), "Please select a category", Toast.LENGTH_LONG).show();
        }else  {

            progressDoalog.show();

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
                Picasso.get().load(filePath).fit().centerCrop().into(imageView5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadDetails() {
        if(filePath != null) {

            byte [] data=null;

            // image compression
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath); // getting image from gallery
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG,imagecompressfactor, baos);
                data = baos.toByteArray();
            }catch (Exception e){

            }

            //image compression done

            product_key = mDatabase.child("products").child(item).push().getKey();

            final Long ts_long = System.currentTimeMillis()/1000;
            final String ts = ts_long.toString();
            final StorageReference childRef = storageReference.child("product_images/" + product_key+ ".jpg");
            final UploadTask uploadTask = childRef.putBytes(data);

            // Progress dialog box implemented
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    int current_progress = (int) progress;
                    progressDoalog.setProgress(current_progress);
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDoalog.dismiss();
                }
            });


            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
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
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String mUri = downloadUri.toString();

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
                                                mDatabase.child("BusinessAccounts").child(company_key).child("products").child(product_key).setValue(dataMap);
                                                progressDoalog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Product added successfully", Toast.LENGTH_LONG).show();
                                                Intent settingsIntent = new Intent(getApplicationContext(), AddNewProductActivity.class);
                                                startActivity(settingsIntent);
                                                Toast.makeText(getApplicationContext(), "Add More Product", Toast.LENGTH_LONG).show();
                                                finish();

                                            } else {
                                                String errMsg = task.getException().getMessage();
                                                Toast.makeText(getApplicationContext(), "Error: " + errMsg, Toast.LENGTH_LONG).show();
                                                progressDoalog.dismiss();
                                            }
                                        }
                                    });
                                } else {
                                    // Handle failures
                                    // ...
                                    String errMsg = task.getException().getMessage();
                                    Toast.makeText(getApplicationContext(), "Download Uri Error: " + errMsg, Toast.LENGTH_LONG).show();
                                    progressDoalog.dismiss();
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
                    progressDoalog.dismiss();
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Please select Product image", Toast.LENGTH_SHORT).show();
            progressDoalog.dismiss();
        }
    }


    private void sendToLogin() {
        Intent loginIntent = new Intent(getApplicationContext(), login.class);
        startActivity(loginIntent);
        mAuth.signOut();
        finishAffinity();
    }

}
