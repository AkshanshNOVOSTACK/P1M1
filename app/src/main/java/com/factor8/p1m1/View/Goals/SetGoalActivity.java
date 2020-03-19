package com.factor8.p1m1.View.Goals;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.factor8.p1m1.R;
import com.factor8.p1m1.databinding.ActivitySetGoalBinding;


public class SetGoalActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySetGoalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_goal);
        binding.cardViewCate1.setOnClickListener(this);
        binding.cardViewCate2.setOnClickListener(this);
        binding.cardViewCate3.setOnClickListener(this);
        binding.cardViewCate4.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.cardView_cate_1:
                Intent i = new Intent(this, ElectronicsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                break;
            case R.id.cardView_cate_2:
                Intent p = new Intent(this, HolidaysActivity.class);
                p.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                p.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(p);
                finish();
                break;
            case R.id.cardView_cate_3:
                Intent u = new Intent(this, AutomobileActivity.class);
                u.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                u.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(u);
                finish();
                break;
            case R.id.cardView_cate_4:
                Intent t = new Intent(this, PersonalSavingsActivity.class);
                t.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                t.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(t);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
