package com.factor8.p1m1.View;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.factor8.p1m1.Model.Entity;
import com.factor8.p1m1.R;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ExpenseDialogFragment extends DialogFragment {
    private TextView mAmount,mSender, mAccountNumber, mTime, mMessageBody;
    private ImageView mImageView;

    private Entity mDataObj;

    public ExpenseDialogFragment() {
    }

     ExpenseDialogFragment(Entity mDataObj) {
        this.mDataObj = mDataObj;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_expense_details,container,false);
        mAmount = view.findViewById(R.id.textView_amount);
        mSender = view.findViewById(R.id.textView_sender);
        mAccountNumber = view.findViewById(R.id.textView_account_number);
        mTime = view.findViewById(R.id.textView_time);
        mMessageBody = view.findViewById(R.id.textView_body);
        mImageView = view.findViewById(R.id.imageView_dialog);

        fillData();
        return view;
    }


    private void fillData() {
        if(mDataObj!=null){
            mAmount.setText("₹ "+ mDataObj.getAmount());
            mSender.setText(mDataObj.getSender());
            mAccountNumber.setText("Acc: "+mDataObj.getAccountNumber());

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh.mm aa");
            String dateString = formatter.format(new Date(Long.parseLong(mDataObj.getTimestamp())));


            mTime.setText(dateString);

            mMessageBody.setText( "\""+mDataObj.getMessageBody()+ "\"");

            switch(mDataObj.getCategory()){
                case 1: mImageView.setImageResource(R.drawable.ic_category_grocery);
                   break;
                case 2: mImageView.setImageResource(R.drawable.ic_category_entertainment);
                   break;
                case 3: mImageView.setImageResource(R.drawable.ic_category_travel);
                   break;
                case 4: mImageView.setImageResource(R.drawable.ic_category_installments);
               break;
                case 5: mImageView.setImageResource(R.drawable.ic_category_util);
                  break;
                case 6: mImageView.setImageResource(R.drawable.ic_category_medical);
                   break;
            }
        }

    }
}
