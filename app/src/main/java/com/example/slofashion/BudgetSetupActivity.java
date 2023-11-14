package com.example.slofashion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class BudgetSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgetsetup);

    }

    public void toHome(View v){
        EditText setMoneyBudget = findViewById(R.id.editText_setMoneyBudget);
        EditText setItemBudget = findViewById(R.id.editText_setNumberBudget);

        String moneyBudget = setMoneyBudget.getText().toString();
        String itemBudget = setItemBudget.getText().toString();

        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra("money_budget", moneyBudget);
        i.putExtra("item_budget", itemBudget);

        startActivity(i);
    }
}