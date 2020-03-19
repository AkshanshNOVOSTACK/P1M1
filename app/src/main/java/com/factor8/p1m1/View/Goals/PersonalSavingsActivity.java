package com.factor8.p1m1.View.Goals;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class PersonalSavingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, PriceRangeFragment.OnInputListner {

    private String[] categories = {"Wedding", "Gift for a loved one", "Small house repair", "Student Fee","Just like that…..SPLURGE " };
    private String timePeriods[] = {"1-2 months", "2-4 months", "4-6 months", "6-9 months", "More than 9 months", "Not sure"};
    private String savingPercentages[] = {"Less than 20%", "35%", "50%", "More than 50%"};


    private Spinner mCategory, mTimePeriod, mSaving;
    private TextView mPriceRange;
    private Button mSave;

    private ImageView mBack;

    private String currentPersonalCategory,  currentTimePeriod, currentSavingPercentage, currentPrice = "0";
    List<EntityGoals> list = new ArrayList<>();

    private EditText mEditText;
    private String TAG = "PersonalSavingstag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_savings);

        mEditText = findViewById(R.id.editText_brand_others);
        mCategory = findViewById(R.id.spinner_category);
        mCategory.setOnItemSelectedListener(this);
        mTimePeriod = findViewById(R.id.spinner_timePeriod);
        mTimePeriod.setOnItemSelectedListener(this);
        mSaving = findViewById(R.id.spinner_saving);
        mSaving.setOnItemSelectedListener(this);
        setSpinnerAdapters();

        mBack = findViewById(R.id.imageView_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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

        mSave = findViewById(R.id.button_save);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDataBaseEntery();
                Toast.makeText(PersonalSavingsActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setSpinnerAdapters() {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategory.setAdapter(adapter);

        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, timePeriods);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimePeriod.setAdapter(adapter2);


        ArrayAdapter adapter4 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, savingPercentages);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSaving.setAdapter(adapter4);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_destination:
                break;
            case R.id.spinner_numberOfFamilyMembers:
                break;
            case R.id.spinner_timePeriod:
                break;
            case R.id.spinner_saving:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void sendInput(String l, String k, String h) {
        mPriceRange.setText("₹ " + l +"," + k +"," + h);
    }

    private void makeDataBaseEntery() {
        String personalCategoryString = new String();


        if(mEditText.getText().toString().isEmpty()){
            personalCategoryString = mCategory.getSelectedItem().toString();
        }else{
            personalCategoryString = mEditText.getText().toString();
        }




        EntityGoals goal = new EntityGoals(4, 69, personalCategoryString, null, null, currentPrice, currentTimePeriod, null, null, System.currentTimeMillis()
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
