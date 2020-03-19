package com.factor8.p1m1.View.Goals;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.factor8.p1m1.Model.EntityGoals;
import com.factor8.p1m1.R;
import com.factor8.p1m1.ViewModel.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class AutomobileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, PriceRangeFragment.OnInputListner {
    private String[] vehicleType = {"Bike", "Scooter"};
    private String[] autoCompanies = {"Bajaj Auto","Hero MotoCorp","Honda Motorcycle","Kawasaki Motors","KTM Industries","Mahindra Two Wheelers","Piaggio Vehicles",
                                                           "Suzuki Motorcycle","Royal Enfield","TVS Motor Company","UM Motorcycles","Yamaha Motor"};

    private String timePeriods[] = {"1-2 months", "2-4 months", "4-6 months", "6-9 months", "More than 9 months", "Not sure"};
    private String savingPercentages[] = {"Less than 20%", "35%", "50%", "More than 50%"};

    List<EntityGoals>list = new ArrayList<>();
    private Spinner mVehicalType, mBrand, mTimePeriod, mSavings;
    private TextView mPriceRange;
    private Button mSave;
    private ImageView mBack;

    private String currentBrand, currentType, currentTimePeriod, currentSavingPercentage, currentPrice = "0";
    private String TAG = "Autotag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile);

        mVehicalType = findViewById(R.id.spinner_automobile); mVehicalType.setOnItemSelectedListener(this);
        mBrand = findViewById(R.id.spinner_brand); mBrand.setOnItemSelectedListener(this);
        mTimePeriod = findViewById(R.id.spinner_timePeriod); mTimePeriod.setOnItemSelectedListener(this);
        mSavings = findViewById(R.id.spinner_saving); mSavings.setOnItemSelectedListener(this);
        mBack = findViewById(R.id.imageView_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setSpinnerAdapters();

        mSave = findViewById(R.id.button_save);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDataBaseEntery();
                Toast.makeText(AutomobileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        mPriceRange = findViewById(R.id.textView_set_price_range);
        mPriceRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PriceRangeFragment fragment = new PriceRangeFragment();
                fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog);
                fragment.show(getSupportFragmentManager(), "s");
            }
        });

    }

    private void setSpinnerAdapters() {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, vehicleType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mVehicalType.setAdapter(adapter);

        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, autoCompanies);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBrand.setAdapter(adapter2);

        ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, timePeriods);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimePeriod.setAdapter(adapter3);

        ArrayAdapter adapter4 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, savingPercentages);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSavings.setAdapter(adapter4);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId())
        {
            case R.id.spinner_automobile: currentType = vehicleType[i];break;
            case R.id.spinner_brand: currentBrand = autoCompanies[i]; break;
            case R.id.spinner_timePeriod: currentTimePeriod = timePeriods[i]; break;
            case R.id.spinner_saving: currentSavingPercentage = savingPercentages[i]; break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void sendInput(String l, String k, String h) {
        mPriceRange.setText("₹ " + l +"," + k +"," + h);
        currentPrice = l+k+h;
    }

    private void makeDataBaseEntery() {
        EntityGoals goal = new EntityGoals(3, 69, null, currentBrand, currentType, currentPrice, currentTimePeriod, null, null, System.currentTimeMillis()
                , currentSavingPercentage);
        ViewModel viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.insertGoal(goal);

        viewModel.getAllGoals().observe(this, new Observer<List<EntityGoals>>() {
            @Override
            public void onChanged(List<EntityGoals> entityGoals) {
                list = entityGoals;
            }
        });
        for(EntityGoals temp : list){
            Log.d(TAG, "makeDataBaseEntery: Brand" + temp.getBrand() +"\n");
            Log.d(TAG, "makeDataBaseEntery: Type" + temp.getType() +"\n");
            Log.d(TAG, "makeDataBaseEntery: Price" + temp.getPrice() +"\n");
            Log.d(TAG, "makeDataBaseEntery: time" + temp.getTimePeriod() +"\n");
            Log.d(TAG, "makeDataBaseEntery: saving%" + temp.getSavingPercentage() +"\n");
            Log.d(TAG, "makeDataBaseEntery: time" + temp.getGoalCreatedDate() +"\n");
        }
    }
}
