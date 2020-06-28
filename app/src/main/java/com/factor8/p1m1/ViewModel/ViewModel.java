package com.factor8.p1m1.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.factor8.p1m1.Model.Entity;
import com.factor8.p1m1.Model.EntityGoals;
import com.factor8.p1m1.Model.EntitySavings;
import com.factor8.p1m1.Repository.Repository;

import java.util.List;

public class ViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<Entity>> allEnteries;
    private LiveData<Double> sum;
    private LiveData<Double> sumCth;
    private LiveData<List<Entity>> unCategorisedEntries;
    private LiveData<List<EntityGoals>> allGoals;
    private LiveData<List<EntitySavings>> allSavingEntries;
    private LiveData<Double> deductionSum;
    private LiveData<List<Double>> getAllCth;

    public ViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allEnteries = repository.getAllEntries();
        sum = repository.getSum();
        unCategorisedEntries = repository.getUncategoriedEntries();
        allGoals = repository.getAllGoals();
        allSavingEntries = repository.getAllSavingEntity();
        deductionSum = repository.getDeductionSum();
        sumCth = repository.getSumCth();
        getAllCth = repository.getGetAllCth();
    }


    public void insert(Entity entity){
         repository.insert(entity);
     }

     public LiveData<List<Entity>> getAllEnteries(){
         return allEnteries;
     }

    public LiveData<Double> getSum() {
        return sum;
    }
    public LiveData<Double> getSumCth() {
        return sumCth;
    }

    public LiveData<List<Entity>> getUnCategorisedEntries(){
        return  unCategorisedEntries;
    }

    public void updateEntity(Entity entery){
           repository.updateEntity(entery);
    }

    public void insertGoal(EntityGoals goal){
        repository.insertGoal(goal);
    }

    public LiveData<List<EntityGoals>> getAllGoals(){
        return allGoals;
    }

    public LiveData<List<EntitySavings>> getAllSavings(){
        return allSavingEntries;
    }

    public void updateEntitySavings (EntitySavings savings){
         repository.updateEntitySaving(savings);
    }

    public List<EntitySavings> getAllSavingEntriesX(){
         return repository.getAllSavingEntriesX();
    }

    public void insertSavingEntry(EntitySavings saving){
        repository.insertSavingEntity(saving);
    }

    public LiveData<Double> getDeductionSum(){
        return deductionSum;
    }

    public LiveData<List<Double>> getGetAllCth(){
        return getAllCth;
    }
}
