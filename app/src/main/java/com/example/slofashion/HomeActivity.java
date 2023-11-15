package com.example.slofashion;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slofashion.datamodels.entities.Budget;
import com.example.slofashion.datamodels.entities.Expenditure;
import com.example.slofashion.datamodels.UsePrefs;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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

    }

    public void toMonthlyRecap(View v){
        Intent i = new Intent(this, MonthlyRecapActivity.class);
        startActivity(i);
    }
}