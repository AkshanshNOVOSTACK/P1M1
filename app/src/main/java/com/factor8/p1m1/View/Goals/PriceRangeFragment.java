package com.factor8.p1m1.View.Goals;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.factor8.p1m1.R;
import com.factor8.p1m1.UtilitiesClasses.Utils;

public class PriceRangeFragment extends DialogFragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private TextView mLakhs, mThousands, mHundreds;
    private Button mCancel, mSubmit;
    private SeekBar mLSeekBar, mTSeekBar, mHSeekBar;
    private String mLakhString = "1", mThousandString = "10", mHundredString = "999";

    private OnInputListner mOnInputListner;

    public PriceRangeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_price_range, container, false);

//        mLakhs = view.findViewById(R.id.textView_price_l);
//        mThousands = view.findViewById(R.id.textView_price_k);
        mHundreds = view.findViewById(R.id.textView_price_h);

        mCancel = view.findViewById(R.id.button_cancel);
        mCancel.setOnClickListener(this);
        mSubmit = view.findViewById(R.id.button_submit);
        mSubmit.setOnClickListener(this);

        mLSeekBar = view.findViewById(R.id.seekbar_l); mLSeekBar.setMax(9);mLSeekBar.incrementProgressBy(1);
        mTSeekBar = view.findViewById(R.id.seekbar_k);mTSeekBar.setMax(99);mTSeekBar.incrementProgressBy(3);
        mHSeekBar = view.findViewById(R.id.seekbar_h);
        mHSeekBar.setMax(200000);
        mHSeekBar.incrementProgressBy(10000);

        mLSeekBar.setOnSeekBarChangeListener(this);
        mTSeekBar.setOnSeekBarChangeListener(this);
        mHSeekBar.setOnSeekBarChangeListener(this);
        return view;
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.seekbar_l:
                mLakhs.setText(""+i);
                mLakhString = String.valueOf(i);
                break;
            case R.id.seekbar_k:
                String thous = String.valueOf(i);

                if(thous.length()  == 1){
                    thous = "0"+thous;
                }
                mThousands.setText(""+thous);
                mThousandString = thous;
                break;
            case R.id.seekbar_h:
                String hund = String.valueOf(i);

                if(hund.length()  == 2){
                     hund = "0"+hund;
                }else if(hund.length()  == 1){
                    hund = "00"+hund;
                }

                mHundreds.setText(""+ Utils.currencyFormatter(Double.valueOf(hund)));
                mHundredString = hund;
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.seekbar_l:
                break;
            case R.id.seekbar_k:
                break;
            case R.id.seekbar_h:
                break;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.seekbar_l:
                break;
            case R.id.seekbar_k:
                break;
            case R.id.seekbar_h:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_cancel:
                getDialog().dismiss();
                break;
            case R.id.button_submit:
                   mOnInputListner.sendInput(mLakhString, mThousandString, mHundredString);
                   getDialog().dismiss();
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{

            mOnInputListner = (OnInputListner) getActivity();

        }catch(Exception e){
            Toast.makeText(context, "*Exception*"+e, Toast.LENGTH_SHORT).show();
        }

    }

    public interface OnInputListner{

        public void sendInput(String l, String k, String h);

    }


}
