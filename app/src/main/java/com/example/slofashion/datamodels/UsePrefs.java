package com.example.slofashion.datamodels;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.slofashion.R;
import com.example.slofashion.datamodels.entities.Budget;
import com.example.slofashion.datamodels.entities.Expenditure;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class used to interact with the KV pairs in shared preferences
 *
 * @implNote any new data models should have a get/save method created here
 * as well as a key set in the strings.xml file
 */
public class UsePrefs {
    public static List<Budget> getAllBudgets(Context context) {
        return getSavedObjectFromPreference(context,
                context.getString(R.string.preference_file_key),
                context.getString(R.string.preference_file_Budgets),
                (Type) Budget.class);
    }

    public static void saveAllBudgets(Context context, List<Budget> budgets) {
        saveObjectToSharedPreference(context,
                context.getString(R.string.preference_file_key),
                context.getString(R.string.preference_file_Budgets),
                budgets,
                (Type) Budget.class);
    }

    public static List<Expenditure> getAllExpenditures(Context context) {
        return getSavedObjectFromPreference(context,
                context.getString(R.string.preference_file_key),
                context.getString(R.string.preference_file_Expenditures),
                (Type) Expenditure.class);
    }

    public static void saveAllExpenditures(Context context, List<Expenditure> expenditures) {
        saveObjectToSharedPreference(context,
                context.getString(R.string.preference_file_key),
                context.getString(R.string.preference_file_Expenditures),
                expenditures,
                (Type) Expenditure.class);
    }

    // Helpers to get some easy data

    public static Optional<Budget> getBudgetForCurrentMonth(Context context) {
        List<Budget> budgets = getAllBudgets(context);
        return budgets.stream()
                .filter(b -> b.yearMonth.query(UsePrefs::isCurrentMonth))
                .findFirst();
    }

    public static List<Expenditure> getAllExpendituresForCurrentMonth(Context context) {
        List<Expenditure> expenditures = getAllExpenditures(context);
        return expenditures.stream()
                .filter(e -> e.createdAt.atZone(ZoneId.systemDefault()).query(UsePrefs::isCurrentMonth))
                .collect(Collectors.toList());
    }

    public static boolean isCurrentMonth(TemporalAccessor temporal) {
        LocalDate now = LocalDate.now();
        LocalDate other = LocalDate.from(temporal);
        return Month.from(now) == Month.from(other) && Year.from(now).equals(Year.from(other));
    }

    // Reference for below: https://stackoverflow.com/a/39435730

    private static <GenericClass> List<GenericClass> getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Type classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = Converters.registerAll(new GsonBuilder()).create();
            Type typeOfT = TypeToken.getParameterized(List.class, classType).getType();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), typeOfT);
        }
        return new ArrayList<>();
    }

    private static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, List<?> object, Type classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = Converters.registerAll(new GsonBuilder()).create();
        Type typeOfT = TypeToken.getParameterized(List.class, classType).getType();
        String serializedObject = gson.toJson(object, typeOfT);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();
    }
}
