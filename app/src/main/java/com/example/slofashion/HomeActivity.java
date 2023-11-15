package com.example.slofashion;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slofashion.datamodels.entities.Budget;
import com.example.slofashion.datamodels.entities.Expenditure;
import com.example.slofashion.datamodels.UsePrefs;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        storageTestingMethod();


        TextView receivedMoneyBudget = findViewById(R.id.receivedMoneyBudget);
        TextView receivedItemBudget = findViewById(R.id.receivedItemBudget);
        View div_moneyBudget = findViewById(R.id.div_moneyBudget);
        View div_itemBudget = findViewById(R.id.div_itemBudget);
        ImageView moneyImg = findViewById(R.id.moneyImg);
        ImageView clothesImg = findViewById(R.id.clothesImg);


        ViewGroup.MarginLayoutParams moneyBudget_layoutParams = (ViewGroup.MarginLayoutParams) div_moneyBudget.getLayoutParams();
        ViewGroup.MarginLayoutParams itemBudget_layoutParams = (ViewGroup.MarginLayoutParams) div_itemBudget.getLayoutParams();
        ViewGroup.LayoutParams moneyImg_layoutParams = moneyImg.getLayoutParams();
        ViewGroup.LayoutParams clothesImg_layoutParams = clothesImg.getLayoutParams();


        Intent i = getIntent();
        String moneyBudget = i.getStringExtra("money_budget");
        String itemBudget = i.getStringExtra("item_budget");
        String moneySpent = i.getStringExtra("money_spent");
        String itemBought = i.getStringExtra("item_bought");


        if(moneyBudget != null && itemBudget != null){
            moneyBudget_layoutParams.setMargins(0, 0, 0, (int)Float.parseFloat(moneyBudget)*4);
            itemBudget_layoutParams.setMargins(0,0 , 0, (int)Float.parseFloat(itemBudget)*35);

            //receivedMoneyBudget.setText("money budget: "+moneyBudget);
            //receivedItemBudget.setText("item budget: "+itemBudget);
            //receivedMoneyBudget.setText("money margin: "+moneyBudget_layoutParams.bottomMargin);
            //receivedItemBudget.setText("item margin: "+itemBudget_layoutParams.bottomMargin);
        }
        else{
            moneyBudget_layoutParams.setMargins(0, 0, 0, 0);
            itemBudget_layoutParams.setMargins(0,0 , 0, 0);
        }
        if(moneySpent != null && itemBought != null){

            moneyImg_layoutParams.height = (int)Float.parseFloat(moneySpent)*4;
            clothesImg_layoutParams.height = (int)Float.parseFloat(itemBought)*35;

            //receivedMoneyBudget.setText("money spent: "+moneySpent);
            //receivedItemBudget.setText("item bought: "+itemBought);

        }
        else{
            moneyImg_layoutParams.height = 1;
            clothesImg_layoutParams.height = 1;

        }

        storageTestingMethod();

        }

    private void storageTestingMethod() {
        //getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE).edit().clear().apply();
        List<Budget> budgets = UsePrefs.getAllBudgets(getApplicationContext());
        budgets.add(new Budget(
                123,
                42
        ));

        List<Expenditure> expenditures = UsePrefs.getAllExpenditures(getApplicationContext());
        expenditures.add(new Expenditure(64, 16));
        expenditures.add(new Expenditure(33, 5));

        UsePrefs.saveAllBudgets(getApplicationContext(), budgets);
        UsePrefs.saveAllExpenditures(getApplicationContext(), expenditures);

        TextView receivedMoneyBudget = findViewById(R.id.receivedMoneyBudget);
        receivedMoneyBudget.setText("spent budget: "+expenditures.get(0).getCost());
    }

    public void toMonthlyRecap(View v){
        Intent i = new Intent(this, MonthlyRecapActivity.class);
        startActivity(i);
    }

    public void toModifyBudget(View v){
        Intent i = new Intent(this, BudgetSetupActivity.class);
        startActivity(i);
    }

    public void toManualEntry(View v){
        Intent i = new Intent(this, ManualEntryActivity.class);
        startActivity(i);
    }

}

