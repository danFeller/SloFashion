package com.example.slofashion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slofashion.datamodels.UsePrefs;
import com.example.slofashion.datamodels.entities.Budget;
import com.google.android.material.slider.Slider;

import java.util.List;

public class BudgetSetupActivity extends AppCompatActivity {

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



    }

    public void toHome(View v){
//        EditText setMoneyBudget = findViewById(R.id.editText_setMoneyBudget);
//        EditText setItemBudget = findViewById(R.id.editText_setNumberBudget);
//        EditText prevMoneyBudget = findViewById(R.id.editText_prevMoneyBudget);
//        EditText prevItemBudget = findViewById(R.id.editText_prevItemBudget);

//        String moneyBudget = setMoneyBudget.getText().toString();
//        String itemBudget = setItemBudget.getText().toString();

        //NEW SLIDER IMPLEMENTATION
        Slider budgetMoney = findViewById(R.id.modifySpendingBudget);
        Slider budgetClothes = findViewById(R.id.modifyClothesBudget);
        float sliderValue1 = budgetMoney.getValue();
        float sliderValue2 = budgetClothes.getValue();

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