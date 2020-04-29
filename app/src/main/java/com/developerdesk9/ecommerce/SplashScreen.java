package com.developerdesk9.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    private ImageView logo;
    private int versionCode;
    private String versionName;
    private boolean version=false;
    private String mvcode="XX";
    private String mvname="XX";
    private DatabaseReference mdatabse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo=findViewById(R.id.splashlogo);


        versionCode = BuildConfig.VERSION_CODE;
        versionName = BuildConfig.VERSION_NAME;
        mdatabse=FirebaseDatabase.getInstance().getReference();



        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotation);
        logo.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {


                mdatabse.child("VersionDetails").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            mvcode=dataSnapshot.child("versionCode").getValue().toString();
                            mvname=dataSnapshot.child("versionName").getValue().toString();
                            isVersion(mvname,mvcode);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });










//                if (ConnectivityReceiver.isConnected()) {
//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                    finish();
//                } else {
//                    title.setText("Internet Unavailable!");
//                    title1.setText("Please connect to Internet");
//                    title2.setText("");
//                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void isVersion(String svname,String svcode){


        final String sversioncode=""+versionCode;

        if (versionName.equals(svname) && sversioncode.equals(svcode)){
            startActivity(new Intent(getApplicationContext(),login.class));
            finishAffinity();
        }
        else {

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Update!");
            builder.setMessage("You are not using the latest version please update");
            builder.setCancelable(false);

            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent viewIntent =new Intent("android.intent.action.VIEW",Uri.parse("https://drive.google.com/drive/folders/1dTutXI8P3fC8FkIJirtfLQD4enwtk9zj?usp=sharing"));
                    startActivity(viewIntent);

                }
            });


            AlertDialog alertDialog=builder.create();
            alertDialog.show();

        }



    }


}
