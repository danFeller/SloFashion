package com.example.slofashion.datamodels.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;

@Entity
public class Expenditure {
    @ColumnInfo(name = "cost")
    public int cost;

    public int getClothes() {
        return clothes;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Expenditure{" +
                "cost=" + cost +
                ", clothes=" + clothes +
                ", createdAt=" + createdAt +
                '}';
    }

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
