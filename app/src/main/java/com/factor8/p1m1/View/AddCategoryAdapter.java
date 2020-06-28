package com.factor8.p1m1.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.factor8.p1m1.Model.Entity;
import com.factor8.p1m1.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.factor8.p1m1.Model.Entity.CATE_ENTERTAINMENT;
import static com.factor8.p1m1.Model.Entity.CATE_GROCERY;
import static com.factor8.p1m1.Model.Entity.CATE_INSTALLMENTS;
import static com.factor8.p1m1.Model.Entity.CATE_MEDICAL;
import static com.factor8.p1m1.Model.Entity.CATE_PRIVATE;
import static com.factor8.p1m1.Model.Entity.CATE_TRAVEL;
import static com.factor8.p1m1.Model.Entity.CATE_UTILS;


public class AddCategoryAdapter extends RecyclerView.Adapter<AddCategoryAdapter.RecViewHolder> {
    private List<Entity> list;
    private Context mContext;
    OnSubItemClick onSubItemClick;
    private int counter;

    public AddCategoryAdapter(List<Entity> list, Context mContext, OnSubItemClick onSubItemClick) {
        this.list = list;
        this.mContext = mContext;
        this.onSubItemClick = onSubItemClick;
        counter = 0;
    }

    public void updateCurrentList(List<Entity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_category, parent, false);
        return new RecViewHolder(view, onSubItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull RecViewHolder holder, final int position) {
        final Entity movie = list.get(position);
        holder.bind(movie);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean expanded = movie.isExpanded();
                int post = 0;
                for (Entity temp : list) {
                    boolean expanded_temp = temp.isExpanded();
                    if (expanded_temp) {
                        temp.setExpanded(false);
                        notifyItemChanged(post);
                    }
                    post++;
                }
                movie.setExpanded(!expanded);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class RecViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mSenderTextView, mTimeStampTextView, mAmountTextView;

        private ConstraintLayout view1, view2, view3, view4, view5, view6;
        private LinearLayout subItem;
        private TextView makePrivate;
        OnSubItemClick onSubItemClick;

        public RecViewHolder(View itemView, OnSubItemClick onSubItemClick) {
            super(itemView);
            this.onSubItemClick = onSubItemClick;
            mSenderTextView = itemView.findViewById(R.id.textView_sender);
            mTimeStampTextView = itemView.findViewById(R.id.textView_timeStamp);
            mAmountTextView = itemView.findViewById(R.id.textView_amount);

            view1 = itemView.findViewById(R.id.constraintLayout_cate_1);
            view2 = itemView.findViewById(R.id.constraintLayout_cate_2);
            view3 = itemView.findViewById(R.id.constraintLayout_cate_3);
            view4 = itemView.findViewById(R.id.constraintLayout_cate_4);
            view5 = itemView.findViewById(R.id.constraintLayout_cate_5);
            view6 = itemView.findViewById(R.id.constraintLayout_cate_6);
           makePrivate = itemView.findViewById(R.id.textView_make_private);
            subItem = itemView.findViewById(R.id.constraintLayout_subLayout);
        }


        private void bind(Entity movie) {
            boolean expanded = movie.isExpanded();
          if(getAdapterPosition()==0 && (counter == 0 )){
              subItem.setVisibility(View.VISIBLE);
              counter++;
          }else{
              subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);
          }

          mSenderTextView.setText(movie.getSender());


            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh.mm aa");
            String dateString = formatter.format(new Date(Long.parseLong(movie.getTimestamp())));

            mTimeStampTextView.setText(dateString);
            mAmountTextView.setText("₹ " + movie.getAmount());
            view1.setOnClickListener(this);
            view2.setOnClickListener(this);
            view3.setOnClickListener(this);
            view4.setOnClickListener(this);
            view5.setOnClickListener(this);
            view6.setOnClickListener(this);
            makePrivate.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.constraintLayout_cate_1:
                    onSubItemClick.OnSubItemClick(getAdapterPosition(),CATE_MEDICAL);
                    break;
                case R.id.constraintLayout_cate_2:
                    onSubItemClick.OnSubItemClick(getAdapterPosition(), CATE_ENTERTAINMENT);
                    break;
                case R.id.constraintLayout_cate_3:
                    onSubItemClick.OnSubItemClick(getAdapterPosition(), CATE_INSTALLMENTS);
                    break;
                case R.id.constraintLayout_cate_4:
                    onSubItemClick.OnSubItemClick(getAdapterPosition(), CATE_GROCERY);
                    break;
                case R.id.constraintLayout_cate_5:
                    onSubItemClick.OnSubItemClick(getAdapterPosition(),CATE_TRAVEL);
                    break;
                case R.id.constraintLayout_cate_6:
                    onSubItemClick.OnSubItemClick(getAdapterPosition(), CATE_UTILS);
                    break;
                case R.id.textView_make_private:
                    onSubItemClick.OnSubItemClick(getAdapterPosition(), CATE_PRIVATE);
                    break;
            }
        }
    }

    public interface OnSubItemClick {
        public void OnSubItemClick(int position, int id);
    }
}
