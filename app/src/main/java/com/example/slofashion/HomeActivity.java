package com.example.slofashion;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slofashion.databinding.ActivityHomeBinding;
import com.example.slofashion.datamodels.entities.Budget;
import com.example.slofashion.datamodels.entities.Expenditure;
import com.example.slofashion.datamodels.UsePrefs;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeActivity extends AppCompatActivity{
    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //storageTestingMethod();

        // Logic to setup notification channel
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = getString(R.string.channel_name);
                String description = getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel("1234", name, importance);
                channel.setDescription(description);
                // Register the channel with the system. You can't change the importance
                // or other notification behaviors after this.
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            // TODO: manually posting notifs for testing purposes
            // Enter notif sends in 5s, leave notif sends in 30s
            new Handler().postDelayed(() -> {
                Context context = getApplicationContext();
                Intent notifyIntent = new Intent(context, DialogueActivity.class);
                // Set the Activity to start in a new, empty task.
                notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                notifyIntent.putExtra(Intent.EXTRA_TEXT, DialogueActivity.DialogueType.ENTER_STORE.toString());
                // Create the PendingIntent.
                PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                        context, (int) (Math.random() * 50), notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1234");
                builder.setContentIntent(notifyPendingIntent);
                builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("You are entering a store")
                        .setContentText("Check up on your budget")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                notificationManager.notify((int) (Math.random() * 50), builder.build());
            }, 1000 * 5);

            new Handler().postDelayed(() -> {
                Context context = getApplicationContext();
                Intent notifyIntent = new Intent(context, DialogueActivity.class);
                // Set the Activity to start in a new, empty task.
                notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                notifyIntent.putExtra(Intent.EXTRA_TEXT, DialogueActivity.DialogueType.LEAVE_STORE.toString());
                // Create the PendingIntent.
                PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                        context, (int) (Math.random() * 50), notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1234");
                builder.setContentIntent(notifyPendingIntent);
                builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("You left the store")
                        .setContentText("Update your budget")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                notificationManager.notify((int) (Math.random() * 50), builder.build());
            }, 1000 * 30);
        }




        //HOME SCREEN LOGIC
        TextView receivedMoneyBudget = findViewById(R.id.receivedMoneyBudget);
        TextView receivedItemBudget = findViewById(R.id.receivedItemBudget);
//        View div_moneyBudget = findViewById(R.id.div_moneyBudget);
//        View div_itemBudget = findViewById(R.id.div_itemBudget);
//        ImageView moneyImg = findViewById(R.id.moneyImg);


//        ViewGroup.MarginLayoutParams moneyBudget_layoutParams = (ViewGroup.MarginLayoutParams) div_moneyBudget.getLayoutParams();
//        ViewGroup.MarginLayoutParams itemBudget_layoutParams = (ViewGroup.MarginLayoutParams) div_itemBudget.getLayoutParams();
//        ViewGroup.LayoutParams moneyImg_layoutParams = moneyImg.getLayoutParams();

        //NEW SLIDER IMPLEMENTATION
        Intent i = getIntent();
        String moneyBudget = i.getStringExtra("money_budget");
        String itemBudget = i.getStringExtra("item_budget");
        String moneySpent = i.getStringExtra("money_spent");
        String itemBought = i.getStringExtra("item_bought");

        float sliderValue1 = i.getFloatExtra("SLIDER_VALUE_1", 0f);
        float sliderValue2 = i.getFloatExtra("SLIDER_VALUE_2", 0f);

        ImageView clothesPile = findViewById(R.id.clothesImg);
        ViewGroup.LayoutParams params = clothesPile.getLayoutParams();

        //DANNY"S MENU OVERHAUL
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.add);

        bottomNavigationView.setOnItemSelectedListener(item ->{
            int itemId = item.getItemId();
            if (itemId == R.id.add) {
                Intent manualIntent = new Intent(this, ManualEntryActivity.class);
                manualIntent.putExtra("SLIDER_VALUE_2", sliderValue2);
                manualIntent.putExtra("SLIDER_VALUE_1", sliderValue1);
                startActivity(manualIntent);
                return true;
            } else if (itemId == R.id.recap) {
                Intent recapIntent = new Intent(this, MonthlyRecapActivity.class);
                startActivity(recapIntent);
                return true;
            } else if (itemId == R.id.budget) {
                Intent budgetIntent = new Intent(this, BudgetSetupActivity.class);
                startActivity(budgetIntent);
                return true;
            }
            return false;
        });


        if(i != null){
//            List<Budget> budgets = UsePrefs.getAllBudgets(getApplicationContext());
//            budgets.add(new Budget(
//                    (int)Float.parseFloat(moneyBudget),
//                    (int)Float.parseFloat(itemBudget)
//            ));
//            UsePrefs.saveAllBudgets(getApplicationContext(), budgets);
//
//
//            int moneyIntBudget = budgets.get(budgets.size()-1).getCostBudget();
//            int itemIntBudget = budgets.get(budgets.size()-1).getClothesBudget();


//            moneyBudget_layoutParams.setMargins(0, 0, 0, moneyIntBudget*4);
//            itemBudget_layoutParams.setMargins(0,0 , 0, itemIntBudget*35);


            receivedMoneyBudget.setText("$"+(float)((int)(sliderValue1*100)) / 100);
            receivedItemBudget.setText("Items Bought: " + (int)sliderValue2);
            params.height = ((int)sliderValue2 * 100) + 10;
            clothesPile.setLayoutParams(params);
            Log.d("myTag", String.valueOf(params.height));
            if(params.height > 1000) {
                params.height = 1000;
                clothesPile.setLayoutParams(params);
            }
        }
        else{
            int moneyIntBudget = 0;
            int itemIntBudget = 0;
            List<Budget> budgets = UsePrefs.getAllBudgets(getApplicationContext());
            if(budgets.size() >= 1){
                 moneyIntBudget = budgets.get(budgets.size()-1).getCostBudget();
                 itemIntBudget = budgets.get(budgets.size()-1).getClothesBudget();
            }

//            moneyBudget_layoutParams.setMargins(0, 0, 0, moneyIntBudget*4);
//            itemBudget_layoutParams.setMargins(0,0 , 0, itemIntBudget*35);

            receivedMoneyBudget.setText("$"+moneyIntBudget);
            receivedItemBudget.setText("Items Bought: "+itemIntBudget);
        }
        if(moneySpent != null && itemBought != null){

            List<Expenditure> expenditures = UsePrefs.getAllExpenditures(getApplicationContext());
            expenditures.add(new Expenditure(
                    (int)Float.parseFloat(moneySpent),
                    (int)Float.parseFloat(itemBought)
            ));
            UsePrefs.saveAllExpenditures(getApplicationContext(), expenditures);

            int totalSpendings = 0;
            int totalItems = 0;

            for(int index = 0; index< expenditures.size(); index++){
                totalSpendings += expenditures.get(index).getCost();
                totalItems += expenditures.get(index).getClothes();
            }

//            moneyImg_layoutParams.height = totalSpendings*4;
//            clothesImg_layoutParams.height = totalItems*35;

            //receivedMoneyBudget.setText("money spent: "+totalSpendings);
            //receivedItemBudget.setText("item bought: "+totalItems);

        }
        else{
            int totalSpendings = 50;
            int totalItems = 50;
            List<Expenditure> expenditures = UsePrefs.getAllExpenditures(getApplicationContext());
            if(expenditures.size() >= 1){
                for(int index = 0; index< expenditures.size(); index++){
                    totalSpendings += expenditures.get(index).getCost();
                    totalItems += expenditures.get(index).getClothes();
                }
            }

            //receivedMoneyBudget.setText("total spendings: "+totalSpendings);
            //receivedItemBudget.setText("total items: "+totalItems);

//            moneyImg_layoutParams.height = totalSpendings;
//            clothesImg_layoutParams.height = totalItems;

        }

        //storageTestingMethod();

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
        TextView receivedItemBudget = findViewById(R.id.receivedItemBudget);

        int totalSpendings = 0;
        for(int i = 0; i< expenditures.size(); i++){
            totalSpendings += expenditures.get(i).getCost();
        }


        receivedMoneyBudget.setText("total Spendings: "+totalSpendings);
        receivedItemBudget.setText("expenditure info: "+expenditures.get(0).toString());

        // how to use helper methods
//        int totalCost = UsePrefs.getAllExpendituresForCurrentMonth(getApplicationContext()).stream().mapToInt(e -> e.cost).sum();
//        int totalClothes = UsePrefs.getAllExpendituresForCurrentMonth(getApplicationContext()).stream().mapToInt(e -> e.clothes).sum();
//        Optional<Budget> budget = UsePrefs.getBudgetForCurrentMonth(getApplicationContext());
    }

//    public void toMonthlyRecap(View v){
//        Intent i = new Intent(this, MonthlyRecapActivity.class);
//        startActivity(i);
//    }
//
//    public void toModifyBudget(View v){
//        Intent i = new Intent(this, BudgetSetupActivity.class);
//        startActivity(i);
//    }
//
//    public void toManualEntry(View v){
//        Intent i = new Intent(this, ManualEntryActivity.class);
//        startActivity(i);
//    }

}
