package com.factor8.p1m1.DataBase;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.factor8.p1m1.Model.Entity;
import com.factor8.p1m1.Model.EntityGoals;
import com.factor8.p1m1.Model.EntitySavings;

@androidx.room.Database(entities = {Entity.class, EntityGoals.class, EntitySavings.class }, version = 3 , exportSchema = false)
public abstract class Database extends RoomDatabase {
    private static Database instance;
    public abstract MyDao MyDoa();

    public static synchronized Database getDBInstance(Context context){
               if(instance == null){
                         instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "P1M1_database").fallbackToDestructiveMigration().build();
               }
               return instance;
    }
}
