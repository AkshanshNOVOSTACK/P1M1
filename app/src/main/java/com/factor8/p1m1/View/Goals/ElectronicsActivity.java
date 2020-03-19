package com.factor8.p1m1.View.Goals;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;


public class ElectronicsActivity extends AppCompatActivity implements PriceRangeFragment.OnInputListner, AdapterView.OnItemSelectedListener {
    private TextView mPriceRange;
    Spinner mBrandSpinner, mTypeSpinner, mTimePeriodSpinner, mSavingsSpinner;
    private MaterialButton mSave;

    private String brands[] = {"Acer", "Apple", "Asus", "Audio-Technica", "Beats", "Bose", "Canon", "Dell", "DJI", "Fitbit", "Fuji", "Google", "GoPro", "Haier", "Harman/Kardon", "Hitachi", "HP", "HyperX", "Jabra",
            "JBL", "JVC", "Klipsch", "Kodak", "Koss", "Lenovo", "LG Electronics", "Logitech", "Microsoft", "Mophie", "Motorola", "MSI", "Nikon", "Nintendo", "•Panasonic", "Philips", "Pioneer", "Plantronics",
            "Samsung", "SanDisk", "Seagate", "Sega", "Sennheiser", "Sharp", "Skullcandy", "Sonos", "Sony", "TCL", "Toshiba", "TP-Link"};
    List<EntityGoals>list = new ArrayList<>();
    private String type1[] = {"Laptop", "Desktop"};
    private String typeApple[] = {"iPhone", "iPad", "Macbook", "Macintosh", "Watch"};
    private String type2[] = {"Earphones", "HeadPhones"};
    private String type3[] = {"Camera", "Accessory"};
    private String type4[] = {"Camera", "Drone", "Accessory"};
    private String type5[] = {"Smartbands", "Watches"};
    private String type6[] = {"Pixel Phone", "Google Home"};
    private String type7[] = {"TV", "Refrigerator", "Washing Machine", "Air-Conditioner", "Air-Purifier"};
    private String type8[] = {"Audio-Systems", "Accessory"};
    private String type9[] = {"Smartbands", "Watches"};
    private String type10[] = {"Peripherals"};

    private String currentTypeMaster[] = new String[20];

    private String timePeriods[] = {"1-2 months", "2-4 months", "4-6 months", "6-9 months", "More than 9 months", "Not sure"};
    private String savingPercentages[] = {"Less than 20%", "35%", "50%", "More than 50%"};


    private String currentBrand, currentType, currentTimePeriod, currentSavingPercentage, currentPrice = "0";
    private ImageView mBack;
    private String TAG = "Electronicstag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electronics);
        mPriceRange = findViewById(R.id.textView_set_price_range);

        mBrandSpinner = findViewById(R.id.spinner_brand);
        mBrandSpinner.setOnItemSelectedListener(this);

        mTypeSpinner = findViewById(R.id.spinner_type);
        mTypeSpinner.setOnItemSelectedListener(this);

        mTimePeriodSpinner = findViewById(R.id.spinner_timePeriod);
        mTimePeriodSpinner.setOnItemSelectedListener(this);

        mSavingsSpinner = findViewById(R.id.spinner_saving);
        mSavingsSpinner.setOnItemSelectedListener(this);

        mBack = findViewById(R.id.imageView_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mSave = findViewById(R.id.button_save);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDataBaseEntery();
                Toast.makeText(ElectronicsActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, brands);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBrandSpinner.setAdapter(adapter);

        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, timePeriods);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimePeriodSpinner.setAdapter(adapter2);

        ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, savingPercentages);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSavingsSpinner.setAdapter(adapter3);


        mPriceRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PriceRangeFragment fragment = new PriceRangeFragment();
                fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog);
                fragment.show(getSupportFragmentManager(), "s");
            }
        });
    }


    @Override
    public void sendInput(String l, String k, String h) {
        mPriceRange.setText("₹ " + l + "," + k + "," + h);
        currentPrice = l+k+h;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_brand:
                currentBrand = brands[i];
                setTypeSpinner();
                break;
            case R.id.spinner_type:
                currentType = currentTypeMaster[i];
                break;
            case R.id.spinner_timePeriod:
                currentTimePeriod = timePeriods[i];
                break;
            case R.id.spinner_saving:
                currentSavingPercentage = savingPercentages[i];
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.d("maintag", "onNothingSelected: ");
    }

    private void setTypeSpinner() {
        switch (currentBrand) {
            case "Acer":
            case "Asus": {
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, type1);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mTypeSpinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                currentTypeMaster = type1;
            }
            break;
            case "Apple": {
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, typeApple);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter.notifyDataSetChanged();
                mTypeSpinner.setAdapter(adapter);
                currentTypeMaster = typeApple;
            }
            break;
            case "Beats":
            case "Bose":
            case "Audio-Technica": {
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, type2);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mTypeSpinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                currentTypeMaster = type2;
            }
            break;
            default: {
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, type7);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mTypeSpinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                currentTypeMaster = type7;
            }
        }
    }

    private void makeDataBaseEntery() {
        EntityGoals goal = new EntityGoals(1, 69, null, currentBrand, currentType, currentPrice, currentTimePeriod, null, null, System.currentTimeMillis()
                , currentSavingPercentage);
        ViewModel  viewModel = ViewModelProviders.of(this).get(ViewModel.class);
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
