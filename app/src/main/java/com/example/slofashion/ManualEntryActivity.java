package com.example.slofashion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

public class ManualEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualentry);
    }

    public void toHome(View v){

//        EditText setMoneySpent = findViewById(R.id.editText_enterNumber);
//        EditText setItemBought = findViewById(R.id.editText_enterNumber);
//
////        String moneySpent = setMoneySpent.getText().toString();
////        String itemBought = setItemBought.getText().toString();
////
////        int moneyIntSpent = (int)Float.parseFloat(moneySpent);
////        int itemIntBought = (int)Float.parseFloat(itemBought);

//NEW SLIDER IMPLEMENTATION
        float remainingBudget = getIntent().getFloatExtra("SLIDER_VALUE_1", 0);
        float clothingAmount = getIntent().getFloatExtra("SLIDER_VALUE_2", 0);
        Slider addClothes = findViewById(R.id.clothesAmountSlider);
        Slider addMoney = findViewById(R.id.moneySpentSlider);
        float sliderValue1 = addMoney.getValue();
        float sliderValue2 = addClothes.getValue();

        float update = remainingBudget - sliderValue1;
        float update2 = clothingAmount + sliderValue2;

        Intent i = new Intent(this, HomeActivity.class);

        i.putExtra("SLIDER_VALUE_1", update);
        i.putExtra("SLIDER_VALUE_2", update2);

        startActivity(i);
    }
}