package com.factor8.p1m1.View;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.factor8.p1m1.Model.Entity;
import com.factor8.p1m1.R;
import com.factor8.p1m1.ViewModel.ViewModel;

import java.util.ArrayList;
import java.util.List;


public class CategoryFragment extends Fragment {

    public static final String CATEGORY_GROCERY = "Grocery";
    public static final String CATEGORY_ENTERTAINMENT = "Entertainment";
    public static final String CATEGORY_TRAVEL = "Travel";
    public static final String CATEGORY_EMI = "EMI";
    public static final String CATEGORY_UTILS = "UTILS";
    public static final String CATEGORY_MEDICAL = "Medical";

    private AnyChartView anyChartView;

    ViewModel viewModel;
    public CategoryFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        anyChartView = view.findViewById(R.id.anychart_category);


         viewModel = ViewModelProviders.of(this).get(ViewModel.class);
         viewModel.getAllEnteries().observe(this, new Observer<List<Entity>>() {
             @Override
             public void onChanged(List<Entity> entityList) {
                 if(entityList!=null){
                     populateChart(entityList);
                 }

             }
         });

        return view;
    }

    private void populateChart(List<Entity> entityList) {
       Pie  pie = AnyChart.pie();
        double cate1 = 0, cate2 = 0, cate3 = 0, cate4 = 0, cate5= 0, cate6 = 0;
        String accountNUmbers_ = "";
        for (Entity temp : entityList) {
            accountNUmbers_ = accountNUmbers_ + "\n" + temp.getSender() + " --  " + temp.getAccountNumber();
            switch (temp.getCategory()) {
                case 1:
                    cate1 = cate1 += temp.getAmount();
                    break;
                case 2:
                    cate2 = cate2 += temp.getAmount();
                    break;
                case 3:
                    cate3 = cate3 += temp.getAmount();
                    break;
                case 4:
                    cate4 = cate4 += temp.getAmount();
                    break;
                case 5:
                    cate5 = cate5 += temp.getAmount();
                    break;
                case 6:
                    cate6 = cate6 += temp.getAmount();
                    break;
            }
        }
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry(CATEGORY_GROCERY, cate1));
        data.add(new ValueDataEntry(CATEGORY_ENTERTAINMENT, cate2));
        data.add(new ValueDataEntry(CATEGORY_TRAVEL, cate3));
        data.add(new ValueDataEntry(CATEGORY_EMI, cate4));
        data.add(new ValueDataEntry(CATEGORY_UTILS, cate5));
        data.add(new ValueDataEntry(CATEGORY_MEDICAL, cate6));

        pie.data(data);
        anyChartView.setChart(pie);
    }

}
