package com.factor8.p1m1.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.factor8.p1m1.BackgroundService;
import com.factor8.p1m1.Model.Entity;
import com.factor8.p1m1.Model.EntitySavings;
import com.factor8.p1m1.LoginRegistration.LoginActivity;
import com.factor8.p1m1.Network.VolleyMultipartRequest;
import com.factor8.p1m1.Network.VolleySingleton;
import com.factor8.p1m1.R;
import com.factor8.p1m1.UtilitiesClasses.Utils;
import com.factor8.p1m1.ViewModel.ViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.factor8.p1m1.LoginRegistration.Login2Activity.PREF_KEY_IS_LOGGED_IN;
import static com.factor8.p1m1.LoginRegistration.Login2Activity.PREF_KEY_IS_SERVICE_RUNNING;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivityTAG";

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    public static final String FRAGMENT_TAG_OVERVIEW = "OverView_Fragment";
    public static final String FRAGMENT_TAG_GOALS = "Goals_Fragment";
    public static final String FRAGMENT_TAG_PROFILE = "Profile_Fragment";
    public static final String FRAGMENT_TAG_TOOLS = "Tools_Fragment";

    public static List<String> mBankSender;
    private String mSender, mMessageBody, accountNumber, mTimeStamp;
    private List<Entity> mDataList = new ArrayList<>();
    private Double sum = (double) 0;
    private ViewModel viewModel;
    private boolean shouldUpdate = false;
    private volatile double sumOfCth = 0;
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    List<EntitySavings> entitySavingsG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        bottomNavigationView = findViewById(R.id.bottomNavigation_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
       sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", MODE_PRIVATE);
        prepSendersList();
        initFragment(getIntent());
        checkForPermissions();
        registerReceiverBroadcastReceiver();
      //  checkLogin();

        viewModel.getAllSavings().observe(this, new Observer<List<EntitySavings>>() {
            @Override
            public void onChanged(List<EntitySavings> entitySavings) {
                entitySavingsG = entitySavings;
                // Log.d(TAG, "onChanged: inside ViewModel Observer");
            }
        });

        viewModel.getSumCth().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                Log.d(TAG, "sumOfCth- Observer"+ aDouble);
                if(aDouble!=null){
                    updateSumCth(aDouble);
//                    if(entitySavingsG!=null){
//                        savingsEntry();
//                    }

                }
                Log.d(TAG, "sumOfCth- Global AVriable"+ sumOfCth);
            }
        });


    }
    void updateSumCth(double sum){
        this.sumOfCth = sum;
    }

    private void checkLogin() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LoginPreference", MODE_PRIVATE);
        if (!pref.getBoolean(PREF_KEY_IS_LOGGED_IN, false)) {
            Intent p = new Intent(MainActivity.this, LoginActivity.class);
            p.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            p.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(p);
            finish();
        }
    }

    private void checkForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.RECEIVE_SMS");
            permissionCheck += this.checkSelfPermission("Manifest.permission.permission.READ_SMS");
            permissionCheck += this.checkSelfPermission("Manifest.permission.INTERNET");

            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.INTERNET}, 910);

            } else {
                Log.d(TAG, "checkBTPermissions: " + " No permissions Required");
            }
        }
    }//Permission Check

    private void prepSendersList() {
        List<String> temp = new ArrayList<>();
        temp.add("VD-ICICIB");
        temp.add("AX-AxisBk");
        temp.add("AD-BOBTXN");
        temp.add("AD-iPaytm");
        temp.add("AD-OBCBNK");
        temp.add("TKSYNBNK");
        temp.add("VK-DBSBNK");
        temp.add("TK-INDUSB");
        temp.add("VK-ALBNK");
        temp.add("VD-UnionB");
        temp.add("VK-IDBIK");
        temp.add("JD-HDFCBK");
        temp.add("AD-PNBSMS");
        temp.add("AX-KOTAKB");
        temp.add("9654829994");
        temp.add("9910247478");
        temp.add("9620921237");
        temp.add("ICICIM");
        temp.add("ICICIB");
        temp.add("ANDBNK");
        temp.add("SBIUPI");
        temp.add("SBIPSG");
        temp.add("CBSSBI");
        temp.add("KOTAKB");
        temp.add("DBSBNK");
        temp.add("SBIINB");
        temp.add("SBIDGT");
        temp.add("ATMSBI");
        temp.add("AXISBK");
        temp.add("BOBTXN");
        temp.add("OBCBNK");
        temp.add("SYNBNK");
        temp.add("INDUSB");
        temp.add("ALBNK");
        temp.add("UNIONB");
        temp.add("IDBIK");
        temp.add("HDFCBK");
        temp.add("PNBSMS");
        //  temp.add("9910247478");
        //   temp.add("7827041909");
        mBankSender = temp;
    }


    private void registerReceiverBroadcastReceiver() {
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(smsReceiver, filter);
    }


    private void initFragment(Intent incomingIntent) {
        OverViewFragment fragment = new OverViewFragment(incomingIntent);
        doFragmentTransaction(fragment, FRAGMENT_TAG_OVERVIEW, false, "");
    }


    private void doFragmentTransaction(Fragment fragment, String tag, boolean addToBackStack, String s) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.frameLayout_container, fragment, tag);

        if (!s.equals(" ")) {
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.intent_message), s);
            fragment.setArguments(bundle);
        }

        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }


    /*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< BroadCast Receiver >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
    private BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ");
            String action = intent.getAction();
            if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();
              //  Log.d(TAG, "onReceive: Receiver Called --------------- " );
                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if (pdus.length == 0) {
                        return;
                    }//end of if
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < pdus.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        sb.append(messages[i].getMessageBody());
                    }//end of for
                    String number = messages[0].getOriginatingAddress();
                    mSender = number;
                    //  mTextViewSender.setText(number);
                    String messageText = sb.toString();
                    String timeStamp = String.valueOf(messages[0].getTimestampMillis());
                    mTimeStamp = timeStamp;
                    // mTextViewBody.setText(messageText);
                    mMessageBody = messageText;
                    Log.d(TAG, "onReceive: " + number);
                    Log.d(TAG, "onReceive: " + messageText);
                    if (senderCheck()){
                        Log.d(TAG, "onReceive: Sender matched:  CalllingA API--------");
                        networkCall();
                    }
                }//end of if(bundle!=null)
            }//end of if getAction

            // checkForPermissions();
        }
    };

    /*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< BroadCast Receiver >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
   //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Network Call <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void networkCall() {
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, "http://dass.io/kwkpay/inward.php", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String responseString = new String(response.data);
                try {
                    JSONArray arr = new JSONArray(responseString);
                    JSONObject obj =  arr.getJSONObject(0);
                    //Log.d(TAG, "onResponse: JSONObject resqest_successful" + obj.getString("request_successful"));
                    Log.d(TAG, "onResponse: JSONObject accountNumber " + obj.getString("accountNumber"));
                    Log.d(TAG, "onResponse: JSONObject typeOfTransaction" + obj.getString("typeOfTransaction"));
                    Log.d(TAG, "onResponse: JSONObject transactionAmount" + obj.getString("transactionAmount"));
                   // Log.d(TAG, "onResponse: JSONObject account" + obj.getString("acc_no"));
                    accountNumber = obj.getString("accountNumber");
                    if(!obj.getString("typeOfTransaction") .equals("credited")){
                        makeDatabaseEntry(obj.getString("transactionAmount"));
                    }
                } catch (Exception e) {
                    Log.d(TAG, "onResponse EXCEPTION: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
              //  map.put("method", "filterSMS");
                map.put("data", mMessageBody);
                return map;
            }
        };
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Network Call <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(smsReceiver);
            if(sharedPreferences.getBoolean(PREF_KEY_IS_SERVICE_RUNNING,false)){
                startService();
            }
        //    Log.d(TAG, "onStop: Starting Service");
        } catch (Exception e) {
            Log.d(TAG, "onPause: " + e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiverBroadcastReceiver();
        try {
            if(sharedPreferences.getBoolean(PREF_KEY_IS_SERVICE_RUNNING,false)) {
                stopService();
            }
       //     Log.d(TAG, "onStart: Stopping Service");
        } catch (Exception e) {
            Log.d(TAG, "onStart: " + e);
        }
       // checkLogin();
    }

    private boolean senderCheck() {
        boolean isThere = false;
        for (String sender : mBankSender) {
            if (mSender.contains(sender)) {
                isThere = true;
                return isThere;
            }
        }
     //   Log.d(TAG, "senderCheck: " + isThere);
        return isThere;
    }

    private void makeDatabaseEntry(String amount) {
        String localAmount = "0";
        String local_account_number = "";
             if(amount.equals("")){
                   localAmount  = "0";
             }else{
                 localAmount = amount;
             }
             if(accountNumber.equals("")){
                 local_account_number = "Acc. No. not available";
             }else{
                 local_account_number = accountNumber;
             }
        viewModel.insert(new Entity(mSender, Double.parseDouble(localAmount.replaceAll(",", "")), Entity.CATE_UNCATEGORISED, local_account_number, mTimeStamp, mMessageBody));
        viewModel.getAllEnteries().observe(this, new Observer<List<Entity>>() {
            @Override
            public void onChanged(List<Entity> entityList) {
                mDataList = entityList;
            }
        });
        Log.d(TAG, "Entity List: ----------------------------Start--------------------------------->>>>>>>>>>>>>>>>>");
        for (Entity temp : mDataList) {
            Log.d(TAG, "makeDatabaseEntry: " + temp.getSender() + " - " + temp.getAmount()
                    + " - Cate: " + temp.getCategory()+ "CTH: "+ temp.getCth()+ "AMOUNT: "+ temp.getAmount());
        }
        Log.d(TAG, "Entity List: ----------------------------Stop--------------------------------->>>>>>>>>>>>>>>>>");
        shouldUpdate = true;
        savingsEntry();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 910) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void proceedAfterPermission() {
        registerReceiverBroadcastReceiver();
    }

    private void startService() {
        Intent serviceIntent = new Intent(this, BackgroundService.class);
        startService(serviceIntent);
    }

    private void stopService() {
        Intent serviceIntent = new Intent(this, BackgroundService.class);
        stopService(serviceIntent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout_container);
        switch (item.getItemId()) {
            case R.id.app_bar_overView:
                if (!(currentFragment instanceof OverViewFragment)) {
                    fragment = new OverViewFragment(null);
                    doFragmentTransaction(fragment, FRAGMENT_TAG_OVERVIEW, false, "");
                    return true;
                }
                break;

            case R.id.app_bar_goals:
                if (!(currentFragment instanceof GoalsFragment)) {
                    fragment = new GoalsFragment();
                    doFragmentTransaction(fragment, FRAGMENT_TAG_GOALS, false, "");
                    return true;
                }
                break;
            case R.id.app_bar_profile:
                if (!(currentFragment instanceof ProfileFragment)) {
                    fragment = new ProfileFragment();
                    doFragmentTransaction(fragment, FRAGMENT_TAG_PROFILE, false, "");
                    return true;
                }
                break;
            case R.id.app_bar_tools:
                if (!(currentFragment instanceof ToolsFragment)) {
                    fragment = new ToolsFragment();
                    doFragmentTransaction(fragment, FRAGMENT_TAG_TOOLS, false, "");
                    return true;
                }
                break;
        }
        return false;
    }

    private void savingsEntry() {
        final String formattedDate = Utils.getTodaysDate00(0);
        final String formattedDateT = Utils.getTodaysDate00(1);
        if (entitySavingsG.size() == 0) {
            Log.d(TAG, "Saving Entry null,  Creating New Entry");
            //No Entry for today's saving exist, Create a New Entry
            Log.d(TAG, "Inserting SumofCTH: ----"+ sumOfCth);
            viewModel.insertSavingEntry(new EntitySavings(System.currentTimeMillis(), 250, 0, sumOfCth, sumOfCth));
            viewModel.getAllSavings().observe(this, new Observer<List<EntitySavings>>() {
                @Override
                public void onChanged(List<EntitySavings> entitySavings) {
                           if(entitySavings.size()!=0){
                               for(EntitySavings temp : entitySavings){
                                   Log.d(TAG, "New Savings Entry : --------------");
                                   Log.d(TAG, "Deduction: "+temp.getDeduction());
                                   Log.d(TAG, "Total Amount "+temp.getTotalExpense());
                               }
                           }
                }
            });
        } else {
            Log.d(TAG, "Entity Saving List: ----------------------------Start--------------------------------->>>>>>>>>>>>>>>>>");
            for (EntitySavings thisSaving : entitySavingsG) {
                //Entry Exist, Just update the current Entry
                if ((thisSaving.getTimeStamp() >= convertDateToTimeStamp(formattedDate, "dd-MM-yyyy"))
                        && (thisSaving.getTimeStamp() < convertDateToTimeStamp(formattedDateT, "dd-MM-yyyy")) && shouldUpdate) {
                    Log.d(TAG, "Saving Entry not null,  Updating Entry");
                    Log.d(TAG, "Inserting SumofCTH: ----"+ sumOfCth);
                    EntitySavings currentObject = thisSaving;
                    thisSaving.setTimeStamp(System.currentTimeMillis());
                    thisSaving.setCth(0);
                    thisSaving.setTotalExpense(sumOfCth);
                    thisSaving.setDeduction(Math.min(250, sumOfCth));
                    viewModel.updateEntitySavings(thisSaving);
                }
                Log.d(TAG, "SavingEntries :  " + thisSaving.getId());
                Log.d(TAG, "SavingEntries :  " + thisSaving.getTimeStamp());
                Log.d(TAG, "SavingEntries :  CTH: NULL " + thisSaving.getCth());
                Log.d(TAG, "SavingEntries :  Expenses: Sum of All Induced Savings " + thisSaving.getTotalExpense());
                Log.d(TAG, "SavingEntries :  Deduction: min( daily limit, Expense) " + thisSaving.getDeduction());
            }
            Log.d(TAG, "Entity Saving List: ----------------------------Stop--------------------------------->>>>>>>>>>>>>>>>>");
            shouldUpdate = false;
        }
    }

    private int calculateNearestHundred(double number) {
        return ((int) ((number + 99) / 100) * 100);
    }

    private long convertDateToTimeStamp(String dateString, String format) {
        DateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        Date date = null;
        try {
            date = (Date) formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long output = date.getTime() / 1000L;
        String str = Long.toString(output);
        long timestamp = Long.parseLong(str) * 1000;
      //  Log.d(TAG, "convertDateToTimeStamp: time Stamp: " + timestamp);
        return timestamp;
    }
}
