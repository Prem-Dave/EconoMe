package com.example.budgetapp.starting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.budgetapp.MainActivity;
import com.example.budgetapp.R;

public class GetStarted extends AppCompatActivity {

    Button getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        getStarted = findViewById(R.id.getStarted);

        getStarted.setOnClickListener(v->{
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        });

    }
}