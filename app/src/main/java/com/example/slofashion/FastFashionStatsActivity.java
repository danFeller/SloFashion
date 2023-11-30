package com.example.slofashion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class FastFashionStatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fastfashionstats);
    }

    public void toHome(View v){
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }
}