package com.example.slofashion.datamodels.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
public class Budget {
    @ColumnInfo(name = "costBudget")
    public int costBudget;
    @ColumnInfo(name = "clothesBudget")
    public int clothesBudget;
    @ColumnInfo(name = "yearMonth")
    public LocalDate yearMonth;

    public int getCostBudget() {
        return costBudget;
    }
    public int getClothesBudget(){
        return clothesBudget;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "costBudget=" + costBudget +
                ", clothesBudget=" + clothesBudget +
                ", yearMonth=" + yearMonth +
                '}';
    }

    public Budget(int costBudget, int clothesBudget) {
        this.costBudget = costBudget;
        this.clothesBudget = clothesBudget;
        this.yearMonth = LocalDate.now().withDayOfMonth(1);
    }
}
