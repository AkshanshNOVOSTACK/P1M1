package com.factor8.p1m1.View;


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
import android.widget.Toast;

import com.factor8.p1m1.Model.Entity;
import com.factor8.p1m1.Model.EntitySavings;
import com.factor8.p1m1.R;
import com.factor8.p1m1.ViewModel.ViewModel;
import com.factor8.p1m1.databinding.FragmentToolsBinding;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ToolsFragment extends Fragment implements ListFragmentAdapter.onListClicked {
    private FragmentToolsBinding binding;
    private ViewModel viewModel;
    private ListFragmentAdapter mAdapter;
    private static final String TAG = "ToolsFragment";
    private List<Entity> mLocalDataList = new ArrayList<>();

    public ToolsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tools, container, false);
        View view = binding.getRoot();

        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getAllEnteries().observe(this, new Observer<List<Entity>>() {
            @Override
            public void onChanged(List<Entity> entityList) {
                mLocalDataList = entityList;
                mAdapter.setDataList(entityList);
            }
        });
        viewModel.getSum().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble != null) {
                    binding.textViewAmountTotal.setText("₹" + aDouble);
                }else{
                    binding.textViewAmountTotal.setText("₹ 0");
                }
            }
        });
     viewModel.getSumCth().observe(this, new Observer<Double>() {
         @Override
         public void onChanged(Double aDouble) {
             if(aDouble != null){
                 binding.textViewCthTotal.setText("₹" + aDouble);
             }else{
                 binding.textViewCthTotal.setText("₹ 0");
             }
         }
     });

     viewModel.getDeductionSum().observe(this, new Observer<Double>() {
         @Override
         public void onChanged(Double aDouble) {
             if(aDouble!=null){
                 binding.textViewTotalSaving.setText("₹"+ aDouble);
             }else{
                 binding.textViewTotalSaving.setText("₹ 0" );
             }
         }
     });

        viewModel.getGetAllCth().observe(this, new Observer<List<Double>>() {
             @Override
             public void onChanged(List<Double> doubles) {
                 if(doubles.size()!=0){
                     mAdapter.setmDataListCTH(doubles);
                 }
             }
         });
         viewModel.getAllSavings().observe(this, new Observer<List<EntitySavings>>() {
             @Override
             public void onChanged(List<EntitySavings> entitySavings) {
                             if(entitySavings.size()==0){
                                 Log.d(TAG, "Savings List is empty");
                             }else{
                                 Log.d(TAG, "---------------------------Tools All Savings Entries -----------------------");
                                  for(EntitySavings temp : entitySavings){
                                      Log.d(TAG, "ID : "+ temp.getId());
                                      Log.d(TAG, "timeStamp : "+ temp.getTimeStamp());
                                      Log.d(TAG, "Deduction: "+ temp.getDeduction());
                                      Log.d(TAG, "TotalExpense: "+ temp.getTotalExpense());
                                  }
                             }
             }
         });

        initRecyclerView();
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    public void initRecyclerView() {
        mAdapter = new ListFragmentAdapter(this, 2);
        binding.recyclerViewTools.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewTools.setAdapter(mAdapter);
    }

    @Override
    public void sendInput(Entity obj) {
        Toast.makeText(getActivity(), "Coming Soon", Toast.LENGTH_SHORT).show();
    }

}
