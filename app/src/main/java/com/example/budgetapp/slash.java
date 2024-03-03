package com.example.budgetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.budgetapp.home.HomeMainPage;

public class slash extends AppCompatActivity {

    SharedPreferences sh;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash);

        @SuppressLint({"MissInflatedId","LocalSuppress"})
        ImageView iv = findViewById(R.id.imageView);

        sh = getSharedPreferences("UserData",MODE_PRIVATE);

        Glide.with(this)
                .load(R.drawable.slash)
                .into(iv);

        boolean isLogin = sh.getBoolean("isLogin",false);

        Handler h=new Handler();

        h.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i;
                if(isLogin){
                    i = new Intent(getApplicationContext(), HomeMainPage.class);
                }
                else{
                    i = new Intent(getApplicationContext(), MainActivity.class);
                }
                startActivity(i);
                finishAffinity();
            }
        },5000);

    }
}