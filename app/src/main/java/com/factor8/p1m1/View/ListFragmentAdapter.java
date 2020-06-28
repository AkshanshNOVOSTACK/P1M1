package com.factor8.p1m1.View;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.factor8.p1m1.Model.Entity;
import com.factor8.p1m1.R;

import java.util.ArrayList;
import java.util.List;

import static com.factor8.p1m1.Model.Entity.CATE_ENTERTAINMENT;
import static com.factor8.p1m1.Model.Entity.CATE_GROCERY;
import static com.factor8.p1m1.Model.Entity.CATE_INSTALLMENTS;
import static com.factor8.p1m1.Model.Entity.CATE_MEDICAL;
import static com.factor8.p1m1.Model.Entity.CATE_PRIVATE;
import static com.factor8.p1m1.Model.Entity.CATE_TRAVEL;
import static com.factor8.p1m1.Model.Entity.CATE_UNCATEGORISED;
import static com.factor8.p1m1.Model.Entity.CATE_UTILS;

public class ListFragmentAdapter extends RecyclerView.Adapter<ListFragmentAdapter.ListViewHolder> {
    public static final String CATEGORY_GROCERY = "Grocery";
    public static final String CATEGORY_ENTERTAINMENT = "Entertainment";
    public static final String CATEGORY_TRAVEL = "Travel";
    public static final String CATEGORY_EMI = "EMI";
    public static final String CATEGORY_UTILS = "UTILS";
    public static final String CATEGORY_MEDICAL = "Medical";
    public static final String CATEGORY_UNKNOWN = "Unknown";
    public static final String CATEGORY_PRIVATE = "Private";
    private List<Entity> mDataList = new ArrayList<>();
    private List<Double> mCTHList = new ArrayList<>();
    private static final String TAG = "ListFragmentAdapter";
    private int type;

    public ListFragmentAdapter(onListClicked onListClicked, int type) {
        this.onListClicked = onListClicked;
        this.type = type;
    }


    public interface onListClicked {
        void sendInput(Entity obj);
    }

    public void setDataList(List<Entity> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    public void setmDataListCTH(List<Double> mCTHList){
        this.mCTHList = mCTHList;
        notifyDataSetChanged();
    }

    private onListClicked onListClicked;

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_expenes_list, parent, false);
        return new ListViewHolder(view, onListClicked);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        holder.mExpense.setText(String.valueOf("₹ " + mDataList.get(position).getAmount()));

        if (type == 2 & (mCTHList.size()==mDataList.size())){
            holder.mCTH.setText(String.valueOf("₹ " + mCTHList.get(position)));

        }

        switch (mDataList.get(position).getCategory()) {
            case CATE_GROCERY:
                holder.mCategoryImageView.setImageResource(R.drawable.ic_category_grocery);
                holder.mCategoryName.setText(CATEGORY_GROCERY);
                break;
            case CATE_ENTERTAINMENT:
                holder.mCategoryImageView.setImageResource(R.drawable.ic_category_entertainment);
                holder.mCategoryName.setText(CATEGORY_ENTERTAINMENT);
                break;
            case CATE_TRAVEL:
                holder.mCategoryImageView.setImageResource(R.drawable.ic_category_travel);
                holder.mCategoryName.setText(CATEGORY_TRAVEL);
                break;
            case CATE_INSTALLMENTS:
                holder.mCategoryImageView.setImageResource(R.drawable.ic_category_installments);
                holder.mCategoryName.setText(CATEGORY_EMI);
                break;
            case CATE_UTILS:
                holder.mCategoryImageView.setImageResource(R.drawable.ic_category_util);
                holder.mCategoryName.setText(CATEGORY_UTILS);
                break;
            case CATE_MEDICAL:
                holder.mCategoryImageView.setImageResource(R.drawable.ic_category_medical);
                holder.mCategoryName.setText(CATEGORY_MEDICAL);
                break;
            case CATE_UNCATEGORISED:
                holder.mCategoryImageView.setImageResource(R.drawable.ic_category_unknown);
                holder.mCategoryName.setText(CATEGORY_UNKNOWN);
                break;
            case CATE_PRIVATE:
                holder.mCategoryImageView.setImageResource(R.drawable.ic_category_private);
                holder.mCategoryName.setText(CATEGORY_PRIVATE);
                break;
        }
    }

    private int calculateNearestHundred(double number) {
        return ((int) ((number + 99) / 100) * 100);
    }

    @Override
    public int getItemCount() {

        return mDataList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mCategoryImageView;
        TextView mCategoryName, mExpense;
        onListClicked onListClicked;
        TextView mCTH;

        public ListViewHolder(@NonNull View itemView, onListClicked onListClicked) {
            super(itemView);
            this.onListClicked = onListClicked;
            mCategoryImageView = itemView.findViewById(R.id.imageView_main);
            mCategoryName = itemView.findViewById(R.id.textView_category_name);
            mExpense = itemView.findViewById(R.id.textView_expense);
            try {
                mCTH = itemView.findViewById(R.id.textView_cth);
            } catch (Exception e) {
                Log.d(TAG, "ListViewHolder: NOT available for this layout");
            }


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onListClicked.sendInput(mDataList.get(getAdapterPosition()));
        }
    }
}
