package com.factor8.p1m1.View;


import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.factor8.p1m1.Model.Entity;
import com.factor8.p1m1.Network.VolleyMultipartRequest;
import com.factor8.p1m1.Network.VolleySingleton;
import com.factor8.p1m1.R;
import com.factor8.p1m1.ViewModel.ViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.factor8.p1m1.Model.Entity.CATE_UNCATEGORISED;


public class ListFragment extends Fragment implements ListFragmentAdapter.onListClicked {

    public static final int DIALOG_TYPE_DETAILS= 11;
    public static final int DIALOG_TYPE_SELECT_CATE = 12;

    private RecyclerView mRecyclerView;
    private ListFragmentAdapter mAdapter;

    private TextView mTotal;
    public static final String TAG ="ListFragmentTag";

    private ViewModel viewModel;
    private int dialogType;

    private List<Entity> mDataList;

    public ListFragment() {

    }
    public ListFragment(int dialogType){
        this.dialogType = dialogType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView_listFragment);
        mTotal = view.findViewById(R.id.textView_sum);
        initRecyclerView();

        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getAllEnteries().observe(this, new Observer<List<Entity>>() {
            @Override
            public void onChanged(List<Entity> entityList) {
                mAdapter.setDataList(entityList);
                mDataList = entityList;
            }
        });
        viewModel.getSum().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble != null)
                    mTotal.setText("Total  ₹ " + aDouble);
            }
        });

        populateDialogs(dialogType, null);
        callFakeApi(); // Fake API: Dummy WeScraper API -
        return view;
    }



    public void initRecyclerView() {
        mAdapter = new ListFragmentAdapter(this,1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void sendInput(Entity obj) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        switch (obj.getCategory()) {
            case CATE_UNCATEGORISED: {
                populateDialogs(DIALOG_TYPE_SELECT_CATE, obj);
            }
            break;
            default: {
               populateDialogs(DIALOG_TYPE_DETAILS,obj);
            }
        }
    }

    private  void  populateDialogs(int type, Entity obj){
        switch(type){
            case DIALOG_TYPE_DETAILS:{
                ExpenseDialogFragment dialog = new ExpenseDialogFragment(obj);
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog);
                dialog.show(getActivity().getSupportFragmentManager(), "s");
            }break;
            case DIALOG_TYPE_SELECT_CATE:{
                AddCategoryDialogFragment dialog = new AddCategoryDialogFragment();
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog);
                dialog.show(getActivity().getSupportFragmentManager(), "s");
            }break;
        }
    }

    private void callFakeApi(){
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.GET, "https://cb539bb4dd41.ngrok.io/api/htmlparser", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String responseString = new String(response.data);
                try {
                    //JSONArray arr = new JSONArray(responseString);
                    JSONObject obj = new JSONObject(responseString);
                    //JSONObject obj =  arr.getJSONObject(0);
                    if(obj.getString("OrderPrice")!=null){
                            Entity tempObj = new Entity("Zomato", Double.parseDouble(obj.getString("TotalAmount")),8
                                    , "akshanshssj5@gmail.com", String.valueOf(System.currentTimeMillis()),
                                    obj.getString("Order"));
                        Log.d(TAG, "onResponse: "+ tempObj.toString());
                        mDataList.add(tempObj);
                        mAdapter.setDataList(mDataList);
                    }
                } catch (Exception e) {
                    Log.d(TAG, "onResponse EXCEPTION: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
            }
        });

        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
}
