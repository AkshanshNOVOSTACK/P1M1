package com.factor8.p1m1.View;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.transition.Fade;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.factor8.p1m1.Model.Entity;
import com.factor8.p1m1.R;
import com.factor8.p1m1.UtilitiesClasses.Utils;
import com.factor8.p1m1.ViewModel.ViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import static com.factor8.p1m1.LoginRegistration.Login2Activity.PREF_KEY_SALARY;

public class OverViewFragment extends Fragment implements View.OnClickListener {
    public static final String FRAGMENT_TAG_LIST = "List_Fragment";
    public static final String FRAGMENT_TAG_CATEGORY = "List_Fragment";
    public static final String FRAGMENT_TAG_SPLIT = "List_Fragment";

    private static final String TAG = "MainActivityTAG";
    private static final long FADE_DEFAULT_TIME = 500;
    private MaterialButton mListView, mCategoryView, mSplitView;
    private TextView mExpense, mBalance, mIncome;

    ListFragment listFragment;

    private ViewModel viewModel;
    private List<Entity> mDataList = new ArrayList<>();
    private Intent incomingIntent = null;
    private SharedPreferences pref;
    private String incomeString = "0";

    public OverViewFragment(Intent incomingIntent) {
        this.incomingIntent = incomingIntent;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_over_view, container, false);
        pref = getActivity().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);

        mListView = view.findViewById(R.id.textView_button_list);
        mListView.setOnClickListener(this);
        mCategoryView = view.findViewById(R.id.textView_button_category);
        mCategoryView.setOnClickListener(this);
        mSplitView = view.findViewById(R.id.textView_button_split);
        mSplitView.setOnClickListener(this);
        mExpense = view.findViewById(R.id.textView_expense);
        mBalance = view.findViewById(R.id.textView_balance);
        mIncome  =  view.findViewById(R.id.textView_income);
        incomeString = pref.getString(PREF_KEY_SALARY,"0");
        mIncome.setText("₹ "+ Utils.currencyFormatter(Double.parseDouble(incomeString)));


        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getAllEnteries().observe(this, new Observer<List<Entity>>() {
            @Override
            public void onChanged(List<Entity> entities) {
                mDataList = entities;
            }
        });

        viewModel.getSum().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble != null) {
                    mExpense.setText("₹ " + Utils.currencyFormatter(aDouble));
                     double balance = Double.parseDouble(incomeString) - aDouble;
                    if (balance < 0 ) {
                        mBalance.setText("₹ " + Utils.currencyFormatter(balance));
                    } else {
                        mBalance.setText("₹ " + Utils.currencyFormatter(balance));
                    }
                }

            }
        });

        initFragment(incomingIntent);
        return view;
    }

    private void initFragment(Intent incomingIntent) {
    if (incomingIntent != null) {
        int intentType = incomingIntent.getIntExtra("CalledFromService", 90);
        listFragment = new ListFragment(intentType);
        doFragmentTransaction(listFragment, FRAGMENT_TAG_LIST, false, "");
    } else {
        listFragment = new ListFragment();
        doFragmentTransaction(listFragment, FRAGMENT_TAG_LIST, false, "");
        }
    }

    private void doFragmentTransaction(Fragment fragment, String tag, boolean addToBackStack, String s) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if(getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout_container_overView) != null){
            // 1. Exit for Previous Fragment
            Fade exitFade = new Fade();
            exitFade.setDuration(FADE_DEFAULT_TIME);
            getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout_container_overView).setExitTransition(exitFade);
        }

        // 2. Enter Transition for New Fragment
        Fade enterFade = new Fade();
        enterFade.setStartDelay(FADE_DEFAULT_TIME);
        enterFade.setDuration(FADE_DEFAULT_TIME);
        fragment.setEnterTransition(enterFade);

       // transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.frameLayout_container_overView, fragment, tag);

        if (!s.equals(" ")) {
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.intent_message), s);
            fragment.setArguments(bundle);
        }

        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_button_list: {
                changeButtonView(view);
                Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout_container);
                if (currentFragment instanceof ListFragment) {
                    //Current Fragment is the same : Do nothing.
                    Toast.makeText(getActivity(), "Do NOthing Same fragment is loaded", Toast.LENGTH_SHORT).show();
                } else {
                    ListFragment listFragment = new ListFragment();
                    doFragmentTransaction(listFragment, FRAGMENT_TAG_LIST, false, "");
                }

            }
            break;
            case R.id.textView_button_category: {
                changeButtonView(view);
                Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout_container);
                if (currentFragment instanceof CategoryFragment) {
                    //Current Fragment is the same : Do nothing.
                    Toast.makeText(getActivity(), "Do NOthing Same fragment is loaded", Toast.LENGTH_SHORT).show();
                } else {
                    CategoryFragment categoryFragment = new CategoryFragment();
                    doFragmentTransaction(categoryFragment, FRAGMENT_TAG_CATEGORY, false, "");
                }
            }
            break;
            case R.id.textView_button_split: {
                changeButtonView(view);
                Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.frameLayout_container);
                if (currentFragment instanceof SplitFragment) {
                    //Current Fragment is the same : Do nothing.
                    Toast.makeText(getActivity(), "Do NOthing Same fragment is loaded", Toast.LENGTH_SHORT).show();
                } else {
                    SplitFragment splitFragment = new SplitFragment();
                    doFragmentTransaction(splitFragment, FRAGMENT_TAG_SPLIT, false, "");
                }
            }
            break;
        }
    }

    private void changeButtonView(View view) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[]{
                ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null),
                ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null),
                ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null),
                ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null)
        };
        int[] colors2 = new int[]{
                getResources().getColor(R.color.colorWhite),
                getResources().getColor(R.color.colorWhite),
                getResources().getColor(R.color.colorWhite),
                getResources().getColor(R.color.colorWhite)
        };

        switch (view.getId()) {
            case R.id.textView_button_list: {
                //Turn Current button white with pink text and icon
                mListView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                mListView.setTextColor(getResources().getColor(R.color.colorPrimary));
                mListView.setIconTint(new ColorStateList(states, colors));

                //Turn Other button pinkAlpha with white text and icon
                mCategoryView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryAlpha));
                mCategoryView.setTextColor(getResources().getColor(R.color.colorWhite));
                mCategoryView.setIconTint(new ColorStateList(states, colors2));

                mSplitView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryAlpha));
                mSplitView.setTextColor(getResources().getColor(R.color.colorWhite));
                mSplitView.setIconTint(new ColorStateList(states, colors2));

            }
            break;
            case R.id.textView_button_category: {
                //Turn Current button white with pink text and icon
                mCategoryView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                mCategoryView.setTextColor(getResources().getColor(R.color.colorPrimary));
                mCategoryView.setIconTint(new ColorStateList(states, colors));

                //Turn Other button pinkAlpha with white text and icon
                mListView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryAlpha));
                mListView.setTextColor(getResources().getColor(R.color.colorWhite));
                mListView.setIconTint(new ColorStateList(states, colors2));

                mSplitView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryAlpha));
                mSplitView.setTextColor(getResources().getColor(R.color.colorWhite));
                mSplitView.setIconTint(new ColorStateList(states, colors2));
            }
            break;
            case R.id.textView_button_split: {
                //Turn Current button white with pink text and icon
                mSplitView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                mSplitView.setTextColor(getResources().getColor(R.color.colorPrimary));
                mSplitView.setIconTint(new ColorStateList(states, colors));

                //Turn Other button pinkAlpha with white text and icon
                mCategoryView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryAlpha));
                mCategoryView.setTextColor(getResources().getColor(R.color.colorWhite));
                mCategoryView.setIconTint(new ColorStateList(states, colors2));

                mListView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryAlpha));
                mListView.setTextColor(getResources().getColor(R.color.colorWhite));
                mListView.setIconTint(new ColorStateList(states, colors2));
            }
            break;
        }
    }
}
