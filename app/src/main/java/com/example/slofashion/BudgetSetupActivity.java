package com.example.slofashion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slofashion.datamodels.UsePrefs;
import com.example.slofashion.datamodels.entities.Budget;

import java.util.List;

public class BudgetSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgetsetup);

        EditText prevMoneyBudget = findViewById(R.id.editText_prevMoneyBudget);
        EditText prevItemBudget = findViewById(R.id.editText_prevItemBudget);

        int moneyIntBudget = 0;
        int itemIntBudget = 0;
        List<Budget> budgets = UsePrefs.getAllBudgets(getApplicationContext());
        if(budgets.size() >= 1){
            moneyIntBudget = budgets.get(budgets.size()-1).getCostBudget();
            itemIntBudget = budgets.get(budgets.size()-1).getClothesBudget();
        }

        prevMoneyBudget.setText("Previous Monetary Budget: "+moneyIntBudget);
        prevItemBudget.setText("Previous Item Budget: "+itemIntBudget);

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