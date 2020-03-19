package com.factor8.p1m1.Repository;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;

import com.factor8.p1m1.DataBase.Database;
import com.factor8.p1m1.Model.Entity;
import com.factor8.p1m1.Model.EntityGoals;
import com.factor8.p1m1.Model.EntitySavings;
import com.factor8.p1m1.DataBase.MyDao;
import java.util.List;


public class Repository {
    private MyDao myDao;
    private LiveData<List<Entity>> allEntries;
    private LiveData<Double> sum;
    private LiveData<Double> sumCth;
    private LiveData<List<Entity>> uncategoriedEntries;

    private LiveData<List<EntityGoals>> allGoals;
    private LiveData<Double> deductionSum;
    private String TAG = "RepoTAG";

    private LiveData<List<EntitySavings>> getAllSavingEntries;
    private LiveData<List<Double>> getAllCth;
    String accountNumber,amount;
    Application application;


    public Repository(Application application) {
        this.application = application;
        Database database = Database.getDBInstance(application); //Database Object Creation
        myDao = database.MyDoa();
        allEntries = myDao.getUserListLiveData();
        sum = myDao.getSumL();
        uncategoriedEntries = myDao.getUncategorisedList();
        allGoals = myDao.getGoalsListLiveData();
        getAllSavingEntries = myDao.getAllSavingEntries();
        deductionSum = myDao.getDeductionSum();
        sumCth = myDao.getSumCth();
        getAllCth = myDao.getCthList();
    }
    /*--------------------------------Data Getter and Setter Methods--------------------------------------*/
    public void insert(Entity entity){
              new InsertEntityAsyncTask(myDao).execute(entity);
    }
    public LiveData<List<Entity>> getAllEntries(){
         return allEntries;
    }
    public LiveData<Double> getSum(){
        return sum;
    }
    public LiveData<Double> getSumCth(){
        return sumCth;
    }

    public LiveData<List<Entity>> getUncategoriedEntries(){
        return uncategoriedEntries;
    }

    public void updateEntity(Entity entry){
         new UpdateEntityAsyncTask(myDao).execute(entry);
    }

    public LiveData<List<EntityGoals>> getAllGoals(){
        return allGoals;
    }
    public void insertGoal(EntityGoals goal){
         new InsertEntityGoalAsyncTask(myDao).execute(goal);
    }

    public LiveData<List<EntitySavings>> getAllSavingEntity(){
        return getAllSavingEntries ;
    }
    public void updateEntitySaving(EntitySavings entry){
        new UpdateEntitySavingsAsyncTask(myDao).execute(entry);
    }

    public List<EntitySavings> getAllSavingEntriesX(){
         return myDao.getAllSavingEntriesX();
    }
    public void insertSavingEntity(EntitySavings entry){
           new InsertEntitySavingsAsyncTask(myDao).execute(entry);
    }

    public LiveData<Double> getDeductionSum(){
        return deductionSum;
    }

    public LiveData<List<Double>> getGetAllCth(){
        return getAllCth;
    }
    /*--------------------------------Data Getter and Setter Methods--------------------------------------*/

    //--------------------------------DataBase Query calls in Multi-Thread ----------------------------------
    private static class InsertEntityAsyncTask extends AsyncTask<Entity, Void, Void>{
           MyDao myDao;

           InsertEntityAsyncTask(MyDao myDao){
               this.myDao = myDao;
           }
        @Override
        protected Void doInBackground(Entity... entities) {
             myDao.addEntry(entities[0]);
            return null;
        }
    }

    private static class UpdateEntityAsyncTask extends AsyncTask<Entity, Void, Void>{
          MyDao myDao;

        public UpdateEntityAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(Entity... entities) {
            myDao.updateEntery(entities[0]);
            return null;
        }
    }

    private static class InsertEntityGoalAsyncTask extends AsyncTask<EntityGoals, Void, Void>{
          MyDao myDao;

        public InsertEntityGoalAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(EntityGoals... entityGoals) {
            myDao.addEntryGoals(entityGoals[0]);
            return null;
        }
    }

    private static class UpdateEntitySavingsAsyncTask extends AsyncTask<EntitySavings, Void, Void>{
        MyDao myDao;

        public UpdateEntitySavingsAsyncTask(MyDao myDao) {
            this.myDao = myDao;
        }

        @Override
        protected Void doInBackground(EntitySavings... entities) {
            myDao.updateSavingEntry(entities[0]);
            return null;
        }
    }

//    private static class GetAllEntriesXAsyncTAsk extends AsyncTask<Void, Void, Void>{
//        MyDao myDao;
//
//        public GetAllEntriesXAsyncTAsk(MyDao myDao) {
//            this.myDao = myDao;
//        }
//
//        @Override
//        protected Void doInBackground(EntitySavings... entities) {
//            myDao.updateSavingEntry(entities[0]);
//            return null;
//        }
//    }

    private static class InsertEntitySavingsAsyncTask extends AsyncTask<EntitySavings, Void, Void>{
        MyDao myDao;

        InsertEntitySavingsAsyncTask(MyDao myDao){
            this.myDao = myDao;
        }
        @Override
        protected Void doInBackground(EntitySavings... entities) {
            myDao.insertSavingsEntry(entities[0]);
            return null;
        }
    }


    //--------------------------------DataBase Query calls in Multi-Thread ----------------------------------

    /*>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>   Network Calls  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<*/



    /*>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>   Network Calls  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<*/
}
