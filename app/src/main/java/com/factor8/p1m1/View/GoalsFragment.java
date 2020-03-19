package com.factor8.p1m1.View;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.factor8.p1m1.Model.EntityGoals;
import com.factor8.p1m1.R;
import com.factor8.p1m1.UtilitiesClasses.Utils;
import com.factor8.p1m1.View.Goals.GoalsAdpater;
import com.factor8.p1m1.View.Goals.SetGoalActivity;
import com.factor8.p1m1.ViewModel.ViewModel;
import com.factor8.p1m1.databinding.FragmentGoalsBinding;


import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.factor8.p1m1.LoginRegistration.Login2Activity.PREF_KEY_SALARY;

public class GoalsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "GoalsFragment";
    FragmentGoalsBinding binding;
  //  FragmentGoalsBindingImpl binding;
    List<EntityGoals> goalsList;
    ViewModel viewModel;
    GoalsAdpater mAdapter;
   SharedPreferences sharedPreferences;
    public GoalsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goals,container,false);
       View view =  binding.getRoot();
       binding.constraintLayoutCreateGoalButton.setOnClickListener(this);
     sharedPreferences =  getContext().getSharedPreferences("LoginPreference", MODE_PRIVATE);
       mAdapter = new GoalsAdpater();

       viewModel = ViewModelProviders.of(this).get(ViewModel.class);
       viewModel.getAllGoals().observe(this, new Observer<List<EntityGoals>>() {
           @Override
           public void onChanged(List<EntityGoals> entityGoals) {
               goalsList = entityGoals;
               if(entityGoals!=null){
                   mAdapter.updateList(entityGoals);
                   calculateProgress(entityGoals);
               }
           }
       });
       binding.recyclerViewCreateGoal.setLayoutManager(new LinearLayoutManager(getActivity()));
       binding.recyclerViewCreateGoal.setAdapter(mAdapter);
       return view;

    }

    private void calculateProgress(List<EntityGoals> entityGoals) {
        double total_goal_amount = 0; // Goals - Total Price
        final String incomeString = sharedPreferences.getString(PREF_KEY_SALARY, "0");

            for(EntityGoals temp : entityGoals){
                total_goal_amount = total_goal_amount +Double.parseDouble( temp.getPrice());
            }
            viewModel.getSum().observe(this, new Observer<Double>() {
                @Override
                public void onChanged(Double aDouble) {
                    Log.d(TAG, "onChanged: ");
                    if (aDouble != null) {
                        binding.textViewSavings.setText("Total Savings ₹ "+ (Double.parseDouble(incomeString) - aDouble));
                    }
                }
            });
            viewModel.getDeductionSum().observe(this, new Observer<Double>() {
                @Override
                public void onChanged(Double aDouble) {
                    binding.textViewInducedSavings.setText("Total Induced Savings ₹ "+ aDouble);
                }
            });
        binding.textViewGoalSavings.setText("Target Savings ₹ "+ Utils.currencyFormatter(total_goal_amount));
    }

    @Override
    public void onClick(View view) {
           switch (view.getId()){
               case R.id.constraintLayout_createGoal_button: startActivity(new Intent(getActivity(), SetGoalActivity.class));break;
           }
    }
}
