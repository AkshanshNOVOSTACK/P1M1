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
    private double closeToHunderedSum = 0;
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
                       for (Entity temo : entityList){
                           Log.d(TAG, "CTH -------> "+ temo.getCth());
                       }
            }
        });
        viewModel.getSum().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble != null) {
                    binding.textViewAmountTotal.setText("₹" + aDouble);
                }
            }
        });
     viewModel.getSumCth().observe(this, new Observer<Double>() {
         @Override
         public void onChanged(Double aDouble) {
             binding.textViewCthTotal.setText("₹" + aDouble);
         }
     });
        Calendar calender = Calendar.getInstance();
        Date today = calender.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        final String formattedDate = df.format(today);


        calender.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calender.getTime();
        final String formattedDateT = df.format(tomorrow);
        viewModel.getAllSavings().observe(this, new Observer<List<EntitySavings>>() {
            @Override
            public void onChanged(List<EntitySavings> entitySavings) {
                if (entitySavings != null) {
                    for (EntitySavings saving : entitySavings) {
                        Log.d(TAG, "onChanged: "+saving.getId());
                        Log.d(TAG, "onChanged: "+ saving.getTimeStamp());
                        Log.d(TAG, "onChanged: "+ saving.getDeduction());
                        Log.d(TAG, "onChanged: "+saving.getTotalExpense());
                        if (saving.getTimeStamp() >= convertDateToTimeStamp(formattedDate, "dd-MM-yyyy")
                                && saving.getTimeStamp() < convertDateToTimeStamp(formattedDateT, "dd-MM-yyyy")) {
                            Log.d(TAG, "onChanged: Sum " + saving.getDeduction());
                            saving.setDeduction(Math.min(250, closeToHunderedSum));
                            binding.textViewTotalSaving.setText(""+ saving.getDeduction());
                        }
                    }
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

        initRecyclerView();
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        Calendar calender = Calendar.getInstance();
        Date today = calender.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        final String formattedDate = df.format(today);


        calender.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calender.getTime();
        final String formattedDateT = df.format(tomorrow);

        viewModel.getAllSavings().observe(this, new Observer<List<EntitySavings>>() {
            @Override
            public void onChanged(List<EntitySavings> entitySavings) {
                if (entitySavings != null) {
                    for (EntitySavings saving : entitySavings) {
                        Log.d(TAG, "onChanged: "+saving.getId());
                        Log.d(TAG, "onChanged: "+ saving.getTimeStamp());
                        Log.d(TAG, "onChanged: "+ saving.getDeduction());
                        Log.d(TAG, "onChanged: "+saving.getTotalExpense());
                        if (saving.getTimeStamp() >= convertDateToTimeStamp(formattedDate, "dd-MM-yyyy")
                                && saving.getTimeStamp() < convertDateToTimeStamp(formattedDateT, "dd-MM-yyyy")) {
                            Log.d(TAG, "onChanged: Sum " + saving.getDeduction());
                            saving.setDeduction(Math.min(250, closeToHunderedSum));


                        }
                    }
                }
            }
        });

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




    private long convertDateToTimeStamp(String dateString, String format) {
        DateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        Date date = null;
        try {
            date = (Date) formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long output = date.getTime() / 1000L;
        String str = Long.toString(output);
        long timestamp = Long.parseLong(str) * 1000;
        return timestamp;
    }



}
