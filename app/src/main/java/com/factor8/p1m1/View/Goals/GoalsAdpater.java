package com.factor8.p1m1.View.Goals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.factor8.p1m1.Model.EntityGoals;
import com.factor8.p1m1.R;
import com.factor8.p1m1.UtilitiesClasses.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.factor8.p1m1.Model.EntityGoals.CATEGORY_AUTO;
import static com.factor8.p1m1.Model.EntityGoals.CATEGORY_ELECTRONICS;
import static com.factor8.p1m1.Model.EntityGoals.CATEGORY_FESTIVAL;
import static com.factor8.p1m1.Model.EntityGoals.CATEGORY_TRAVEL;


public class GoalsAdpater extends RecyclerView.Adapter<GoalsAdpater.GoalsViewHolder> {
    private List<EntityGoals> mDataList = new ArrayList<>();

    @NonNull
    @Override
    public GoalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_goals,parent,false);
        return new GoalsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalsViewHolder holder, int position) {
  //Add Later
        EntityGoals goal = mDataList.get(position);
        switch(goal.getCategory()){
            case CATEGORY_ELECTRONICS: {
                holder.mIcon.setImageResource(R.drawable.ic_electronics);
                holder.mTitle.setText(goal.getType());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh.mm aa");
                String dateString = formatter.format(new Date(goal.getGoalCreatedDate()));
                holder.mDate.setText("created on:"+ dateString);
                holder.mStartingPrice.setText("₹ 0");
                holder.mEndingPrice.setText("₹ "+ Utils.currencyFormatter(Double.parseDouble(goal.getPrice())));
                holder.animationView.setAnimation(R.raw.progress_yellow);
            }break;
            case CATEGORY_TRAVEL: {
                holder.mIcon.setImageResource(R.drawable.ic_holidays);
                holder.mTitle.setText(goal.getDestination());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh.mm aa");
                String dateString = formatter.format(new Date(goal.getGoalCreatedDate()));
                holder.mDate.setText("created on:"+ dateString);
                holder.mStartingPrice.setText("₹ 0");
                holder.mEndingPrice.setText("₹ "+Utils.currencyFormatter(Double.parseDouble(goal.getPrice())));
                holder.animationView.setAnimation(R.raw.progress_blue);
            }break;
            case CATEGORY_AUTO: {
                holder.mIcon.setImageResource(R.drawable.ic_automobile);
                holder.mTitle.setText(goal.getType());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh.mm aa");
                String dateString = formatter.format(new Date(goal.getGoalCreatedDate()));
                holder.mDate.setText("created on:"+ dateString);
                holder.mStartingPrice.setText("₹ 0");
                holder.mEndingPrice.setText("₹ "+Utils.currencyFormatter(Double.parseDouble(goal.getPrice())));
                holder.animationView.setAnimation(R.raw.progress_red);
            }break;
            case CATEGORY_FESTIVAL: {
                holder.mIcon.setImageResource(R.drawable.ic_festivls);
                holder.mTitle.setText(goal.getPersonalGoalCategory());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh.mm aa");
                String dateString = formatter.format(new Date(goal.getGoalCreatedDate()));
                holder.mDate.setText("created on:"+ dateString);
                holder.mStartingPrice.setText("₹ 0");
                holder.mEndingPrice.setText("₹ "+Utils.currencyFormatter(Double.parseDouble(goal.getPrice())));
                holder.animationView.setAnimation(R.raw.progress_pink);
            }break;
        }

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void updateList(List<EntityGoals> mDataList){
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

      class GoalsViewHolder extends RecyclerView.ViewHolder {
        private   ImageView mIcon;
        private TextView mTitle, mDate, mStartingPrice, mEndingPrice;
        private LottieAnimationView animationView;

          GoalsViewHolder(@NonNull View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.imageView_icon);
            mTitle = itemView.findViewById(R.id.textView_title);
            mDate = itemView.findViewById(R.id.textView_date);
            mStartingPrice = itemView.findViewById(R.id.textView_startGoal_price);
            mEndingPrice = itemView.findViewById(R.id.textView_endGoal_price);
           animationView = itemView.findViewById(R.id.animationView_lottie);
        }
    }
}
