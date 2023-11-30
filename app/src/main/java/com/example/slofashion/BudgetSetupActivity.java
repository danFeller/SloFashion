package com.example.slofashion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slofashion.datamodels.UsePrefs;
import com.example.slofashion.datamodels.entities.Budget;
import com.google.android.material.slider.Slider;

import java.util.List;

public class BudgetSetupActivity extends AppCompatActivity {
    private float sliderValue1 = 0;
    private float sliderValue2 = 0;
    private Slider budgetMoney;
    private Slider budgetClothes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgetsetup);
//        EditText prevMoneyBudget = findViewById(R.id.editText_prevMoneyBudget);
//        EditText prevItemBudget = findViewById(R.id.editText_prevItemBudget);

        int moneyIntBudget = 0;
        int itemIntBudget = 0;
        List<Budget> budgets = UsePrefs.getAllBudgets(getApplicationContext());
        if(budgets.size() >= 1){
            moneyIntBudget = budgets.get(budgets.size()-1).getCostBudget();
            itemIntBudget = budgets.get(budgets.size()-1).getClothesBudget();
        }

        Button submitButton = findViewById(R.id.button_setUp_submit);
        budgetMoney = findViewById(R.id.modifySpendingBudget);
        budgetClothes = findViewById(R.id.modifyClothesBudget);
        sliderValue1 = budgetMoney.getValue();
        sliderValue2 = budgetClothes.getValue();
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
                    Toast.makeText(BudgetSetupActivity.this, "Values must be greater than 0", Toast.LENGTH_SHORT).show();
                    submitButton.setEnabled(false);
                }
            }
        });


        budgetMoney.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser) {
                // Update the class-level variable sliderValue1 when the slider value changes
                sliderValue1 = value;
                submitButton.setEnabled(true);
            }
        });
        budgetClothes.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser) {
                // Update the class-level variable sliderValue1 when the slider value changes
                sliderValue2 = value;
                submitButton.setEnabled(true);
            }
        });
    }

    public void toHome (View v){
//        EditText setMoneyBudget = findViewById(R.id.editText_setMoneyBudget);
//        EditText setItemBudget = findViewById(R.id.editText_setNumberBudget);
//        EditText prevMoneyBudget = findViewById(R.id.editText_prevMoneyBudget);
//        EditText prevItemBudget = findViewById(R.id.editText_prevItemBudget);

//        String moneyBudget = setMoneyBudget.getText().toString();
//        String itemBudget = setItemBudget.getText().toString();

        //NEW SLIDER IMPLEMENTATION
        budgetMoney = findViewById(R.id.modifySpendingBudget);
        budgetClothes = findViewById(R.id.modifyClothesBudget);
        sliderValue1 = budgetMoney.getValue();
        sliderValue2 = budgetClothes.getValue();

//        prevMoneyBudget.setText("Previous Monetary Budget: "+sliderValue1);
//        prevItemBudget.setText("Previous Item Budget: "+ sliderValue2);

        Intent i = new Intent(this, HomeActivity.class);
//        i.putExtra("money_budget", moneyBudget);
//        i.putExtra("item_budget", itemBudget);
          i.putExtra("SLIDER_VALUE_1", sliderValue1);
          i.putExtra("SLIDER_VALUE_2", 0);
          i.putExtra("RESET", sliderValue2);



        startActivity(i);
    }
}