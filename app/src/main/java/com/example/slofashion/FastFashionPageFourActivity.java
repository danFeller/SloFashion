package com.example.slofashion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class FastFashionPageFourActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fastfashionstats_4);
    }

    public void toHome(View v){
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    public void prevPage(View v){
        Intent i = new Intent(this, FastFashionPageThreeActivity.class);
        startActivity(i);
    }
}
