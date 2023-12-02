package com.example.slofashion;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
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
import com.example.slofashion.notifications.GeofenceBroadcastReceiver;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.stream.IntStream;

import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    private PendingIntent geofencePendingIntent;
    private static final int LOC_NOTI_REQUEST_CODE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");
            Intent budgetIntent = new Intent(this, BudgetSetupActivity.class);
            startActivity(budgetIntent);

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        } else
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

            checkLocNotiPerms();
            setGeofences(false);

            // TODO: manually posting notifs for testing purposes
            // Enter notif sends in 5s, leave notif sends in 30s
//            new Handler().postDelayed(() -> {
//                GeofenceBroadcastReceiver.sendNotification(this, DialogueActivity.DialogueType.ENTER_STORE);
//            }, 1000 * 5);
//
//            new Handler().postDelayed(() -> {
//                GeofenceBroadcastReceiver.sendNotification(this, DialogueActivity.DialogueType.LEAVE_STORE);
//            }, 1000 * 30);
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
                Intent dialogueIntent = new Intent(this, DialogueActivity.class);
                dialogueIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                dialogueIntent.putExtra(Intent.EXTRA_TEXT, DialogueActivity.DialogueType.LEAVE_STORE.toString());
                startActivity(dialogueIntent);
                finish();
                return true;
            } else if (itemId == R.id.recap) {
                Intent recapIntent = new Intent(this, MonthlyRecapActivity.class);
                startActivity(recapIntent);
                finish();
                return true;
            } else if (itemId == R.id.budget) {
                Intent budgetIntent = new Intent(this, BudgetSetupActivity.class);
                startActivity(budgetIntent);
                finish();
                return true;
            }
            return false;
        });


        if(i != null && moneyBudget != null && itemBudget != null){
            List<Budget> budgets = UsePrefs.getAllBudgets(getApplicationContext());
            budgets.add(new Budget(
                    (int)Float.parseFloat(moneyBudget),
                    (int)Float.parseFloat(itemBudget)
            ));
            UsePrefs.saveAllBudgets(getApplicationContext(), budgets);
//
//
//            int moneyIntBudget = budgets.get(budgets.size()-1).getCostBudget();
//            int itemIntBudget = budgets.get(budgets.size()-1).getClothesBudget();


//            moneyBudget_layoutParams.setMargins(0, 0, 0, moneyIntBudget*4);
//            itemBudget_layoutParams.setMargins(0,0 , 0, itemIntBudget*35);

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
//        Budget budget;
//        try{
//            budget = UsePrefs.getBudgetForCurrentMonth(getApplicationContext()).get();
//        } catch(NoSuchElementException e){
//            Intent budgetIntent = new Intent(this, BudgetSetupActivity.class);
//            startActivity(budgetIntent);
//            finish();
//            return;
//        }

        Budget budget = UsePrefs.getBudgetForCurrentMonth(getApplicationContext()).orElse(new Budget(120, 10));

        int totalCost = UsePrefs.getAllExpendituresForCurrentMonth(getApplicationContext()).stream().mapToInt(e -> e.cost).sum();
        int totalClothes = UsePrefs.getAllExpendituresForCurrentMonth(getApplicationContext()).stream().mapToInt(e -> e.clothes).sum();
        receivedMoneyBudget.setText("$" + (budget.costBudget - totalCost));
        receivedItemBudget.setText("Items Bought: " + totalClothes);
        params.height =(int)(((float)totalClothes / budget.clothesBudget) * 800);


        params.height = Math.max(Math.min(params.height, 1000), 100);
        Log.d("myTag", String.valueOf(params.height));
        clothesPile.setLayoutParams(params);
        //storageTestingMethod();

        }

    private void checkLocNotiPerms() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        || ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED
        || ActivityCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // You can directly ask for the permission.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.POST_NOTIFICATIONS },
                        LOC_NOTI_REQUEST_CODE);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestPermissions(
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        LOC_NOTI_REQUEST_CODE);
            } else {
                requestPermissions(
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        LOC_NOTI_REQUEST_CODE);
            }
        }
    }

    @Deprecated
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

    // https://developer.android.com/develop/sensors-and-location/location/geofencing
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        // Android 12 (sad face) https://www.flybuy.com/android-12-pendingintent-mutability-and-geofences
        geofencePendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.
                FLAG_MUTABLE | FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    // https://developer.android.com/develop/sensors-and-location/location/geofencing
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(GeofenceBroadcastReceiver.getGeofencesList());
        return builder.build();
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

    public void toStats(View v) {
        // Button submitButton = findViewById(R.id.button_fast_fashion_stats);
        Intent i = new Intent(this, FastFashionStatsActivity.class);
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOC_NOTI_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && IntStream.of(grantResults)
                        .allMatch(grantRes -> grantRes == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    setGeofences(true);
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the feature requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Toast.makeText(this, "Background alerts when entering or leaving shopping districts not enabled.", Toast.LENGTH_LONG).show();
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    private void setGeofences(boolean fromORPR) {
        GeofencingClient geofencingClient = LocationServices.getGeofencingClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnSuccessListener(unused -> Log.i("GeofenceSetup", "geofences added"))
                    .addOnFailureListener(err -> {
                        Log.e("GeofenceSetup", "error adding geofences");
                        Log.e("GeofenceSetup", Log.getStackTraceString(err));
                    });
        }

        // Android 34+ workaround, need to request fine perms first, THEN get background perms
        // https://stackoverflow.com/questions/64246883/android-11-users-can-t-grant-background-location-permission
        if (fromORPR) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            LOC_NOTI_REQUEST_CODE
                    );
                }
            }
        }
    }

}
