package com.example.slofashion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slofashion.datamodels.UsePrefs;
import com.example.slofashion.datamodels.entities.Expenditure;

import java.util.List;

public class MonthlyRecapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthlyrecap);
        getMonthlyInfo();
    }

    public void getMonthlyInfo() {
        List<Expenditure> expenditures = UsePrefs.getAllExpenditures(getApplicationContext());
        int totalSpendings = 0;
        int totalItems = 0;


        for(int index = 0; index < expenditures.size(); index++){
            // need to check if its in the month that we are currently in
            totalSpendings += expenditures.get(index).getCost();
            totalItems += expenditures.get(index).getClothes();
            Log.d("hi", expenditures.get(index).createdAt.toString());
        }

        TextView netMoneySpent = findViewById(R.id.netMoneySpent);
        TextView itemsPurchased = findViewById(R.id.itemsPurchased);

        netMoneySpent.setText(totalSpendings + "spent");
        itemsPurchased.setText(totalItems + "items purchased");
    }
}