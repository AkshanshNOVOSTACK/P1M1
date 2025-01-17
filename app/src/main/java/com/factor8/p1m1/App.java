package com.factor8.p1m1;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_1_ID = "ForegroundServiceNotificationChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
                NotificationManager manager = (NotificationManager) this.getSystemService(NotificationManager.class);
                assert manager != null;
                manager.createNotificationChannel(channel1);
        }
    }
}
