package com.example.slofashion;

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

            receivedMoneyBudget.setText("money budget: "+moneyBudget);
            receivedItemBudget.setText("item budget: "+itemBudget);
        }
        if(moneySpent != null && itemBought != null){

            moneyImg_layoutParams.height = (int)Float.parseFloat(moneySpent)*4;
            clothesImg_layoutParams.height = (int)Float.parseFloat(itemBought)*35;

            receivedMoneyBudget.setText("money spent: "+moneySpent);
            receivedItemBudget.setText("item bought: "+itemBought);

        }
        else{
            moneyBudget_layoutParams.setMargins(0, 0, 0, 0);
            itemBudget_layoutParams.setMargins(0,0 , 0, 0);
            moneyImg_layoutParams.height = 1;
            clothesImg_layoutParams.height = 1;


        }


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

