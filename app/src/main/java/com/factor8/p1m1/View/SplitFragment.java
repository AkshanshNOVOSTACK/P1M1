package com.factor8.p1m1.View;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.factor8.p1m1.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SplitFragment extends Fragment {


    public SplitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_split, container, false);
    }

}
