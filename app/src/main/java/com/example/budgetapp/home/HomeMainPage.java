package com.example.budgetapp.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.budgetapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeMainPage extends AppCompatActivity {


    BottomNavigationView bnv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main_page);


        bnv = findViewById(R.id.bottomNavigationView);

//        bnv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadFragment(new ProfileFragment());
//            }
//        });

        loadFragment(new BlankFragment());


    }

    private void loadFragment(Fragment blankFragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        transaction.replace(R.id.fragmentTab, blankFragment);
        transaction.commit();
    }
}