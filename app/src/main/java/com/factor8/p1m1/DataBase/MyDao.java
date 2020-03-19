package com.factor8.p1m1.DataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.factor8.p1m1.Model.Entity;
import com.factor8.p1m1.Model.EntityGoals;
import com.factor8.p1m1.Model.EntitySavings;

import java.util.List;

@Dao
public interface MyDao {

    @Insert
     void addEntry(Entity entity);

    @Query("SELECT * FROM Entity")
    List<Entity> getUserList();

    @Query("select SUM(cth) from Entity")
    LiveData<Double> getSumCth();

    @Query("select SUM(amount) from Entity")
    LiveData<Double> getSumL();


     @Query("select * from Entity order by timestamp DESC")
    LiveData<List<Entity>>  getUserListLiveData();

     @Query("select * from Entity where category = 69 order by timestamp DESC")
    LiveData<List<Entity>>  getUncategorisedList();

     @Update
    void updateEntery(Entity entery);

     @Query("select cth from Entity order by timestamp DESC")
     LiveData<List<Double>> getCthList();

//------ Entity = " Goals " Queries --------------------
    @Insert
    void addEntryGoals(EntityGoals goal);

    @Query("select * from EntityGoals order by goalCreatedDate DESC")
    LiveData<List<EntityGoals>>  getGoalsListLiveData();


//------ Entity = " Savings " Queries --------------------
      @Insert
      void insertSavingsEntry(EntitySavings entitySavings);

      @Update
      void updateSavingEntry(EntitySavings entitySavings);

      @Query("select * from EntitySavings where timeStamp >= :timeStampA and timeStamp < :timeStampB")
      LiveData<EntitySavings> getSavingEntry(double timeStampA, double timeStampB);

      @Query("select * from EntitySavings")
    LiveData<List<EntitySavings>> getAllSavingEntries();

    @Query("select * from EntitySavings")
    List<EntitySavings> getAllSavingEntriesX();

    @Query("select sum(deduction) from EntitySavings")
    LiveData<Double> getDeductionSum();

}
