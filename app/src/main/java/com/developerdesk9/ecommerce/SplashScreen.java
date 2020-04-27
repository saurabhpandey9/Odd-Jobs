package com.developerdesk9.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    private ImageView logo;
    private int versionCode;
    private String versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo=findViewById(R.id.splashlogo);

        versionCode = BuildConfig.VERSION_CODE;
        versionName = BuildConfig.VERSION_NAME;


        Toast.makeText(getApplicationContext(),versionCode+versionName,Toast.LENGTH_LONG).show();


        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotation);
        logo.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//
//                startActivity(new Intent(getApplicationContext(),login.class));
//                finishAffinity();

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
}
