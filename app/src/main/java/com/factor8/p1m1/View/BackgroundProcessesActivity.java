package com.factor8.p1m1.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.factor8.p1m1.BackgroundService;
import com.factor8.p1m1.R;
import com.factor8.p1m1.databinding.ActivityBackgroundProcessesBinding;
import com.google.gson.internal.$Gson$Preconditions;

import static com.factor8.p1m1.LoginRegistration.Login2Activity.PREF_KEY_IS_SERVICE_RUNNING;

public class BackgroundProcessesActivity extends AppCompatActivity {
    ActivityBackgroundProcessesBinding binding;
    private static final String TAG = "BackgroundProcessesActi";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_background_processes);
        sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", MODE_PRIVATE);

        binding.switchBackgroundService.setChecked(sharedPreferences.getBoolean(PREF_KEY_IS_SERVICE_RUNNING,false));
        binding.switchBackgroundService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d(TAG, "onCheckedChanged:  state:    " + b);
                editor = sharedPreferences.edit();
                editor.putBoolean(PREF_KEY_IS_SERVICE_RUNNING, b);
                editor.apply();
                startStopService(b);
            }
        });
        binding.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void startStopService(boolean b) {
             if(b){
                 Intent serviceIntent = new Intent(getApplicationContext(), BackgroundService.class);
                 startService(serviceIntent);
                 binding.textViewBackgroundService.setText("Turn Off background Service");
             }else{
                 Intent serviceIntent = new Intent(getApplicationContext(), BackgroundService.class);
                 stopService(serviceIntent);
                 binding.textViewBackgroundService.setText("Turn On background Service");
        }
    }

}
