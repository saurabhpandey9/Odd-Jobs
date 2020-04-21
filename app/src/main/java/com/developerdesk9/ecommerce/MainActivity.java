package com.developerdesk9.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView tv_noitem;

    private RecyclerView recyclerview;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;

    private ProductsAdapter productsAdapter;
    private List<Products> productsLists = new ArrayList<>();
    private Context mContext;
    public DrawerLayout drawer;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_noitem=findViewById(R.id.tv_main_activity_no_item);

        Toolbar toolbar = findViewById(R.id.toolbar); // toolbar initialization
        setSupportActionBar(toolbar);   // setting it on action bar

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // cart activity Implemented on floating button
                Intent orderIntent = new Intent(MainActivity.this, CartListActivity.class);
                startActivity(orderIntent);
                // redirect to cart

            }
        });

         drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,drawer,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        // new code implementation
        mContext=getApplicationContext();
        // Todo ::  home layout implementation

        recyclerview = findViewById(R.id.recyclerview);
        productsAdapter = new ProductsAdapter(productsLists, mContext);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(productsAdapter);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        if(savedInstanceState==null){
            product_listing("Electronics");
            navigationView.setCheckedItem(R.id.nav_electronics);
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            product_listing("Electronics"); // this will execute when page rotation/ refresh take place
        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {


            case R.id.action_logout_tn:
                startActivity(new Intent(getApplicationContext(),login.class));
                finishAffinity();
                mAuth.signOut();
                return true;
            case R.id.action_notification:
                Toast.makeText(getApplicationContext(),"NO notification",Toast.LENGTH_LONG).show();
                return true;

            case  R.id.action_my_account:
                startActivity(new Intent(getApplicationContext(),MyAccount.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(),OrderActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

       switch (item.getItemId()){
           case R.id.nav_electronics:
               product_listing("Electronics");
               break;
           case R.id.nav_tv_appliances:
               product_listing("Appliances");
               break;
           case R.id.nav_fashion :
               product_listing("Fashion");
               break;
           case R.id.nav_home_furniture :
               product_listing("Furniture");
               break;
           case R.id.nav_grocery :
               product_listing("Grocery");
               break;
           case R.id.nav_beauty_care :
               product_listing("Beauty_Care");
               break;
           case R.id.nav_sports:
               product_listing("Sports");
               break;
           case R.id.nav_books:
               product_listing("Books");
               break;


           case R.id.nav_mycart:
               startActivity(new Intent(getApplicationContext(),CartListActivity.class));
               break;
           case R.id.nav_myorder:
               startActivity(new Intent(getApplicationContext(),OrderActivity.class));
               break;
           case R.id.nav_my_account:
               startActivity(new Intent(getApplicationContext(),MyAccount.class));
               break;
           case R.id.nav_logout:
               startActivity(new Intent(getApplicationContext(),login.class));
               finishAffinity();
               mAuth.signOut();
               break;


           case R.id.nav_legal:
               break;
           case R.id.nav_about_developer:

               break;

           default:
       }


       drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void product_listing(String product_type){
        productsLists.clear();
        productsAdapter.notifyDataSetChanged();

        tv_noitem.setVisibility(View.VISIBLE);

        getSupportActionBar().setTitle(product_type.toUpperCase()); // change the Actionbar title

        if (product_type != null) {
            mDatabase.child("products").child(product_type).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.exists()) {
                        tv_noitem.setVisibility(View.INVISIBLE);
                        Products products = dataSnapshot.getValue(Products.class);
                        productsLists.add(products);
                        productsAdapter.notifyDataSetChanged();
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
