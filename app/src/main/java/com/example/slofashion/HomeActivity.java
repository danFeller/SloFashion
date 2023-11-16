package com.example.slofashion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Intent;  // Import statement for Intent
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button historyBtn = findViewById(R.id.history);
        TextView numberTextView = findViewById(R.id.account);
        ImageView imageView = findViewById(R.id.clothesPile);


        final int[] number = {100};  // example number
        numberTextView.setText("$" + String.valueOf(number[0]));
        historyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(HomeActivity.this, FastFashionStatsActivity.class);
                startActivity(intent);
            }
        });
        Button increaseButton = findViewById(R.id.increaseButton);
        increaseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                number[0]++;  // Increment the number

                float scale = imageView.getScaleX();
                numberTextView.setText("$" + String.valueOf(number[0]));  // Update the TextView
                imageView.setScaleX(scale + 0.1f);  // Increase scale by 10%
                imageView.setScaleY(scale + 0.1f);  // Keep aspect ratio
            }
        });
        Button decreaseButton = findViewById(R.id.decreaseButton);
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                number[0]--;  // Increment the number

                float scale = imageView.getScaleX();
                numberTextView.setText("$" + String.valueOf(number[0]));  // Update the TextView
                imageView.setScaleX(scale - 0.1f);  // Increase scale by 10%
                imageView.setScaleY(scale - 0.1f);  // Keep aspect ratio
            }
        });
    }

}