package com.factor8.p1m1;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.factor8.p1m1.Model.Entity;
import com.factor8.p1m1.Model.EntitySavings;
import com.factor8.p1m1.Network.VolleyMultipartRequest;
import com.factor8.p1m1.Network.VolleySingleton;
import com.factor8.p1m1.UtilitiesClasses.Utils;
import com.factor8.p1m1.View.MainActivity;
import com.factor8.p1m1.ViewModel.ViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import static com.factor8.p1m1.App.CHANNEL_1_ID;
import static com.factor8.p1m1.View.ListFragment.DIALOG_TYPE_SELECT_CATE;

public class BackgroundService extends Service {
    private String mSender, mMessageBody, mTimeStamp, accountNumber, amount;
    private ViewModel viewModel;
    private String TAG = "BGTagab";
    public static final int FOREGROUND_SERVICE_ID = 204;
    private int backgroundServiceRunCount;
    private  List<String> mBankSender;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        viewModel = new ViewModel(getApplication());
        prepSendersList();
        registerReceiverBroadcastReceiver();
        Log.d(TAG, "onCreate: ");
        backgroundServiceRunCount = 0;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Do Work here
       createNotification();
       return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(smsReceiver);
        } catch (Exception e) {
            Log.d(TAG, "Error in UnRegistering The recievers..");
        }

    }

    private void registerReceiverBroadcastReceiver() {
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(smsReceiver, filter);
    }

    /*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< BroadCast Receiver >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
    private BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();
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
                    if (senderCheck()){
                        Log.d("MainActivityTAG", "onReceive: Network Call from the service: ");
                        networkCall();
                    }

                }//end of if(bundle!=null)
            }//end of if getAction

            // checkForPermissions();
        }
    };
    /*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< BroadCast Receiver >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/

    //------------------------------------------------------  Network Call --------------------------------------------------------------------------

    public void networkCall() {
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, "http://dass.io/kwkpay/inward.php", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String responseString = new String(response.data);
                try {
                    JSONArray arr = new JSONArray(responseString);
                    JSONObject obj =  arr.getJSONObject(0);
//                    Log.d(TAG, "onResponse: JSONObject resqest_successful" + obj.getString("request_successful"));
//                    Log.d(TAG, "onResponse: JSONObject error" + obj.getString("error"));
//                    Log.d(TAG, "onResponse: JSONObject message" + obj.getString("message"));
//                    Log.d(TAG, "onResponse: JSONObject amount" + obj.getString("amount"));
//                    Log.d(TAG, "onResponse: JSONObject account" + obj.getString("acc_no"));
                    Log.d(TAG, "onResponse: JSONObject accountNumber " + obj.getString("accountNumber"));
                    Log.d(TAG, "onResponse: JSONObject typeOfTransaction" + obj.getString("typeOfTransaction"));
                    Log.d(TAG, "onResponse: JSONObject transactionAmount" + obj.getString("transactionAmount"));
                    accountNumber = obj.getString("accountNumber");
                    amount = obj.getString("transactionAmount");
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
               // map.put("method", "filterSMS");
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

    //------------------------------------------------------  Network Call --------------------------------------------------------------------------
    private boolean senderCheck() {
        boolean isThere = false;
        for (String sender : mBankSender) {
            if (mSender.contains(sender)) {
                isThere = true;
                return isThere;
            }
        }
        return isThere;
    }

    private void makeDatabaseEntry(String amount) {
//        String localAmount = "0";
//        String local_account_number = "";
//        if(amount.equals("")){
//            localAmount  = "0";
//        }else{
//            localAmount = amount;
//        }
//        if(accountNumber.equals("")){
//            local_account_number = "Acc. No. not available";
//        }else{
//            local_account_number = accountNumber;
//        }
        try{
            viewModel.insert(new Entity(mSender, Double.parseDouble((amount).replaceAll(",", "")), Entity.CATE_UNCATEGORISED, accountNumber, mTimeStamp, mMessageBody));
            backgroundServiceRunCount = backgroundServiceRunCount+1;
            createNotification();
        }catch(Exception e){
            Log.d(TAG, "makeDatabaseEntry: "+ e.getLocalizedMessage());
        }

    }


    private void createNotification(){
        Notification notification = null;
        if(backgroundServiceRunCount == 0){
            Intent notificationIntent = new Intent(this, MainActivity.class);
            //notificationIntent.putExtra("CalledFromService", DIALOG_TYPE_DETAILS);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

             notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                     .setContentTitle("Tracking Your Expenses")
                     .setContentText("Relax while I track your expense")
                     .setSmallIcon(R.drawable.ic_budget_pink)
                     .setContentIntent(pendingIntent)
                     .build();
         }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                Intent notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.putExtra("CalledFromService", DIALOG_TYPE_SELECT_CATE);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                        .setContentTitle("Tracking Your Expenses")
                        .setContentText("You just spent ₹ "+amount)
                        .setSmallIcon(R.drawable.ic_budget_pink)
                        .setLargeIcon(drawableToBitmap(getDrawable(R.drawable.ic_category_entertainment)))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(mMessageBody)
                                .setBigContentTitle("You just spent ₹ "+Double.parseDouble((amount).replaceAll(",", "")) +" From: Acc"+accountNumber)
                                .setSummaryText(mSender))
                        .setContentIntent(pendingIntent)
                        .build();
            }
            else{
                Intent notificationIntent = new Intent(this, MainActivity.class);
              //  notificationIntent.putExtra("CalledFromService", DIALOG_TYPE_DETAILS);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                        .setContentTitle("Tracking Your Expenses")
                        .setContentText("Relax while I track your expense")
                        .setSmallIcon(R.drawable.ic_budget_pink)
                        .setContentIntent(pendingIntent)
                        .build();
            }
        }


        startForeground(FOREGROUND_SERVICE_ID, notification);
        backgroundServiceRunCount = backgroundServiceRunCount + 1;
    }
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

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
}
