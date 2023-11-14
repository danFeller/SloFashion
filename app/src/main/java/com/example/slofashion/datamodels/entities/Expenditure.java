package com.example.slofashion.datamodels.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;

@Entity
public class Expenditure {
    @ColumnInfo(name = "cost")
    public int cost;
    @ColumnInfo(name = "clothes")
    public int clothes;
    @ColumnInfo(name = "createdAt")
    public Instant createdAt;

    public Expenditure(int cost, int clothes) {
        this.cost = cost;
        this.clothes = clothes;
        this.createdAt = Instant.now();
    }
}
