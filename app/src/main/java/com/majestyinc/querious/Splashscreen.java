package com.majestyinc.querious;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splashscreen extends AppCompatActivity {
    ImageView logo;
    TextView appname, appdetail;
    long animeTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);


        logo = findViewById(R.id.imageView);
        appname = findViewById(R.id.app_name);
        appdetail = findViewById(R.id.description);

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(logo, "y", 300f);
        ObjectAnimator animatorName = ObjectAnimator.ofFloat(appname, "x", 0f);
        animatorY.setDuration(animeTime);
        animatorName.setDuration(animeTime);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorY, animatorName);
        animatorSet.start();


    }

    @Override
    protected void onStart() {
        super.onStart();


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(user!=null)
                {
                    Intent intent = new Intent(Splashscreen.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                else
                {

                    Intent intentL = new Intent(Splashscreen.this,LoginActivity.class);
                    startActivity(intentL);

                }

            }
        },4000);



    }



}
