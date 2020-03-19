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


public class HolidaysActivity extends AppCompatActivity implements PriceRangeFragment.OnInputListner, AdapterView.OnItemSelectedListener {

    private String[] countries = {"Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda", "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "The Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cabo Verde", "Cambodia", "Cameroon", "Canada", "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo, Democratic Republic of the", "Congo, Republic of the", "Costa Rica", "Côte d’Ivoire", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor ", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia", "Fiji", "Finland", "France", "Gabon", "The Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary",
            "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, North", "Korea, South", "Kosovo", "Kuwait", "Kyrgyzstan", "Laos", "Latvia",
            "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mexico", "Micronesia, Federated States of", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar (Burma)", "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Macedonia", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Romania", "Russia", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "Spain", "Sri Lanka", "Sudan", "Sudan, South", "Suriname", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"};
    private String timePeriods[] = {"1-2 months", "2-4 months", "4-6 months", "6-9 months", "More than 9 months", "Not sure"};
    private String savingPercentages[] = {"Less than 20%", "35%", "50%", "More than 50%"};
    private String familyMembers[] = {"I like to fly solo", "I’m on my honeymoon", "Including kids", "Full family"};

    private Spinner mDestination, mNumberOfFamilyMembers, mTimePeriod, mSaving;
    private TextView mPriceRange;
    private EditText mDestinationEditText, mNumberOfMembersEditText;

    private Button mSave;
    private ImageView mBack;
    private String currentDestination, currentNumberOfMembers, currentTimePeriod, currentSavingPercentage, currentPrice = "0";
    List<EntityGoals>list = new ArrayList<>();

    private String TAG = "Travelltag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holidays);
        mDestinationEditText = findViewById(R.id.editText_brand_others);
        mNumberOfMembersEditText = findViewById(R.id.editText_numberOfFamilyMembers);
        mDestination = findViewById(R.id.spinner_destination); mDestination.setOnItemSelectedListener(this);
        mNumberOfFamilyMembers = findViewById(R.id.spinner_numberOfFamilyMembers); mNumberOfFamilyMembers.setOnItemSelectedListener(this);
        mTimePeriod = findViewById(R.id.spinner_timePeriod); mTimePeriod.setOnItemSelectedListener(this);
        mSaving = findViewById(R.id.spinner_saving); mSaving.setOnItemSelectedListener(this);
        mPriceRange = findViewById(R.id.textView_set_price_range);
           setSpinnerAdapters();

        mBack = findViewById(R.id.imageView_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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
                Toast.makeText(HolidaysActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setSpinnerAdapters() {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDestination.setAdapter(adapter);

        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, familyMembers);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNumberOfFamilyMembers.setAdapter(adapter2);

        ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, timePeriods);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimePeriod.setAdapter(adapter3);

        ArrayAdapter adapter4 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, savingPercentages);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSaving.setAdapter(adapter4);

    }

    @Override
    public void sendInput(String l, String k, String h) {
//        String hund = h;
//        if(h.length()==2)
//        {
//            hund = "0"+h;
//        }
        mPriceRange.setText("₹ " + l +"," + k +"," + h);
        currentPrice = l+k+h;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
         switch(adapterView.getId())
         {
             case R.id.spinner_destination: break;
             case R.id.spinner_numberOfFamilyMembers: break;
             case R.id.spinner_timePeriod: currentTimePeriod = timePeriods[i]; break;
             case R.id.spinner_saving:currentSavingPercentage = savingPercentages[i];  break;
         }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void makeDataBaseEntery() {
        String destinationString = new String();
        int numberOfMembersString =1 ;

        if(mDestinationEditText.getText().toString().isEmpty()){
            destinationString = mDestination.getSelectedItem().toString();
        }else{
             destinationString = mDestinationEditText.getText().toString();
        }

        if(mNumberOfMembersEditText.getText().toString().isEmpty()){
            switch(mNumberOfFamilyMembers.getSelectedItem().toString()){
                case "I like to fly solo": numberOfMembersString = 1;break;
                case "I’m on my honeymoon": numberOfMembersString =2 ;break;
                case "Including kids": numberOfMembersString = 4;break;
                case "Full family":  numberOfMembersString = 6; break;

            }


        }else{
            destinationString = mNumberOfMembersEditText.getText().toString();
        }


        EntityGoals goal = new EntityGoals(2, numberOfMembersString, null, null, null, currentPrice, currentTimePeriod, destinationString, null, System.currentTimeMillis()
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
