package com.example.slofashion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ManualEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualentry);
    }

    public void toHome(View v){

        EditText setMoneySpent = findViewById(R.id.editText_enterMoney);
        EditText setItemBought = findViewById(R.id.editText_enterNumber);

        String moneySpent = setMoneySpent.getText().toString();
        String itemBought = setItemBought.getText().toString();

        int moneyIntSpent = (int)Float.parseFloat(moneySpent);
        int itemIntBought = (int)Float.parseFloat(itemBought);

        Intent i;
        if(moneyIntSpent/itemIntBought < 10){
            i = new Intent(this, FastFashionStatsActivity.class);

        }
        else{
            i = new Intent(this, HomeActivity.class);
            i.putExtra("money_spent", moneySpent);
            i.putExtra("item_bought", itemBought);

        }
        startActivity(i);
    }
}