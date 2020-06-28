package com.factor8.p1m1.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EntitySavings {
    @PrimaryKey (autoGenerate = true)
    int id;
    long timeStamp;
    double dailyLimit, cth, deduction, totalExpense;

    public EntitySavings(long timeStamp, double dailyLimit, double cth, double deduction, double totalExpense) {
        this.timeStamp = timeStamp; //timeStamp of the transaction
        this.dailyLimit = dailyLimit; // Daily limit of the day
        this.cth = cth; // NULL
        this.deduction = deduction;  // Which ever is minimum ( Daily limit OR Sum of Induced Saving)
        this.totalExpense = totalExpense; // --------- Sum of Induced Savings
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(double dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public double getCth() {
        return cth;
    }

    public void setCth(double cth) {
        this.cth = cth;
    }

    public double getDeduction() {
        return deduction;
    }

    public void setDeduction(double deduction) {
        this.deduction = deduction;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }
}
