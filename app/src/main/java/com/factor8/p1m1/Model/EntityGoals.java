package com.factor8.p1m1.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EntityGoals {

    public static final int CATEGORY_ELECTRONICS = 1;
    public static final int CATEGORY_TRAVEL = 2;
    public static final int CATEGORY_AUTO = 3;
    public static final int CATEGORY_FESTIVAL = 4;

    // int value 69 will be considered as null

    @PrimaryKey (autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int category, numberOffamilyMembers;

    private String brand, type, price, timePeriod, destination, typeOfVehical, savingPercentage, personalGoalCategory;
    private long goalCreatedDate;


    public EntityGoals(int category, int numberOffamilyMembers, String personalGoalCategory, String brand, String type, String price, String timePeriod, String destination, String typeOfVehical, long goalCreatedDate, String savingPercentage) {
        this.category = category;
        this.numberOffamilyMembers = numberOffamilyMembers;
        this.personalGoalCategory = personalGoalCategory;
        this.brand = brand;
        this.type = type;
        this.price = price;
        this.timePeriod = timePeriod;
        this.destination = destination;
        this.typeOfVehical = typeOfVehical;
        this.goalCreatedDate = goalCreatedDate;
        this.savingPercentage = savingPercentage;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getNumberOffamilyMembers() {
        return numberOffamilyMembers;
    }

    public void setNumberOffamilyMembers(int numberOffamilyMembers) {
        this.numberOffamilyMembers = numberOffamilyMembers;
    }

    public String getPersonalGoalCategory() {
        return personalGoalCategory;
    }

    public void setPersonalGoalCategory(String personalGoalCategory) {
        this.personalGoalCategory = personalGoalCategory;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTypeOfVehical() {
        return typeOfVehical;
    }

    public void setTypeOfVehical(String typeOfVehical) {
        this.typeOfVehical = typeOfVehical;
    }

    public long getGoalCreatedDate() {
        return goalCreatedDate;
    }

    public void setGoalCreatedDate(long goalCreatedDate) {
        this.goalCreatedDate = goalCreatedDate;
    }

    public String getSavingPercentage() {
        return savingPercentage;
    }

    public void setSavingPercentage(String savingPercentage) {
        this.savingPercentage = savingPercentage;
    }
}
