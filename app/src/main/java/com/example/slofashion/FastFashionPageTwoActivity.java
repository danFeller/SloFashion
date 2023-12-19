package com.example.slofashion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class FastFashionPageTwoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fastfashionstats_2);
    }

    public void nextPage(View v){
        Intent i = new Intent(this, FastFashionPageThreeActivity.class);
        startActivity(i);
    }

    public void prevPage(View v){
        Intent i = new Intent(this, FastFashionStatsActivity.class);
        startActivity(i);
    }
}
