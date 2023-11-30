package com.example.slofashion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

public class ManualEntryActivity extends AppCompatActivity {
    private float sliderValue1 = 0;
    private float sliderValue2 = 0;
    private Slider addClothes;
    private Slider addMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualentry);

        Button submitButton = findViewById(R.id.button_entry_submit);
        addMoney = findViewById(R.id.moneySpentSlider);
        addClothes = findViewById(R.id.clothesAmountSlider);
        sliderValue1 = addMoney.getValue();
        sliderValue2 = addClothes.getValue();
        submitButton.setEnabled(sliderValue1 > 0 && sliderValue2 > 0);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Access the class-level variable sliderValue1 here
                if (sliderValue1 > 0 && sliderValue2 > 0) {
                    // Your logic when sliderValue1 is greater than 0
                    toHome(v);
                } else {
                    // Your logic when sliderValue1 is not greater than 0
                    Toast.makeText(ManualEntryActivity.this, "Values must be greater than 0", Toast.LENGTH_SHORT).show();
                    submitButton.setEnabled(false);
                }
            }
        });


        addMoney.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser) {
                // Update the class-level variable sliderValue1 when the slider value changes
                sliderValue1 = value;
                submitButton.setEnabled(true);
            }
        });
        addClothes.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser) {
                // Update the class-level variable sliderValue1 when the slider value changes
                sliderValue2 = value;
                submitButton.setEnabled(true);
            }
        });

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
        Slider addMoney = findViewById(R.id.moneySpentSlider);
        Slider addClothes = findViewById(R.id.clothesAmountSlider);
        float sliderValue1 = addMoney.getValue();
        float sliderValue2 = addClothes.getValue();

        float update = remainingBudget - sliderValue1;
        float update2 = clothingAmount + (int)sliderValue2;

        Intent i = new Intent(this, HomeActivity.class);

        i.putExtra("SLIDER_VALUE_1", update);
        i.putExtra("SLIDER_VALUE_2", update2);
        startActivity(i);
    }
}