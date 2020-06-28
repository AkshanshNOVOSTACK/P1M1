package com.factor8.p1m1.Model;

import android.util.Log;

import androidx.room.PrimaryKey;

/* 1- Grocery
*   2- Entertainment
* 3 - Travel
* 4 - Installments
* 5- Utils
* 6 - Medical
* 69- uncatalogued*/

@androidx.room.Entity
public class Entity {
    public static final int CATE_GROCERY = 1;
    public static final int CATE_ENTERTAINMENT = 2;
    public static final int CATE_TRAVEL = 3;
    public static final int CATE_INSTALLMENTS = 4;
    public static final int CATE_UTILS = 5;
    public static final int CATE_MEDICAL = 6;
    public static final int CATE_UNCATEGORISED = 69;
    public static final int CATE_PRIVATE =7;
    private static final String TAG = "Entity";
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String Sender;
    private double amount;
    private double cth;
    private int category;
    private String messageBody;
    private String accountNumber;
    private String timestamp;
    private boolean isExpanded = false;

    public boolean isExpanded() {
        return isExpanded;
    }

    public double getCth() {
        return cth;
    }

    public void setCth(double cth) {
       this.cth =   (((int) ((cth + 99) / 100) * 100) - cth);
       // this.cth = cth;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public Entity() {
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Entity(String sender, double amount, int category, String accountNumber, String timestamp, String messageBody) {
        Sender = sender;
        this.amount = amount;
        this.category = category;
        this.accountNumber = accountNumber;
        this.timestamp = timestamp;
        this.messageBody = messageBody;
        this.cth = (((int) ((amount + 99) / 100) * 100) - amount);
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getSender() {
        return Sender;
    }

    public double getAmount() {
        return amount;
    }

    public int getCategory() {
        return category;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
