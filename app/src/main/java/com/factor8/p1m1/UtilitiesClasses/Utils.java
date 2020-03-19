package com.factor8.p1m1.UtilitiesClasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class Utils {
    private static final String TAG = "Utils";
    private static ProgressDialog loading;

    public static OkHttpClient okClient(Context context) {
        //  int cacheSize = 10 * 1024 * 1024; // 10 MB
        //  Cache cache = new Cache(context.getCacheDir(), cacheSize);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                //   .cache(cache)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }


    public static void showProgress(Context context, String message) {
        // loading = new ProgressDialog(context,R.style.ProgressDialog);
        loading = new ProgressDialog(context);
        loading.setMessage(message);
        loading.setProgressStyle(android.R.style.Widget_DeviceDefault_ProgressBar_Small);
        loading.setCancelable(false);
        loading.show();
    }


    public static final boolean isInternetOn(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) { // connected to the internet
            //Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
      //  Utils.toast(context, context.getResources().getString(R.string.no_internet_connection));
        return false;
    }

    public static final void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void dismissProgress() {
        try {
            if (loading.isShowing()) {
                loading.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", e + "");
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();
        /*
         * If no view is focused, an NPE will be thrown
         *
         * Maxim Dm
         */
        if (focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    inputMethodManager.HIDE_NOT_ALWAYS);
        }
        //   inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String currencyFormatter(double value){
        DecimalFormat formatter = new DecimalFormat("##,##,##,###");
        Log.d(TAG, "currencyFormatter: " + formatter.format(value));
        return formatter.format(value);
        
    }

    public static String getTodaysDate00(int plusDays){
        Calendar calender = Calendar.getInstance();
        calender.add(Calendar.DAY_OF_YEAR, plusDays);
        Date today = calender.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return df.format(today);
    }

}
