package com.factor8.p1m1.View;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.factor8.p1m1.Model.Entity;
import com.factor8.p1m1.R;
import com.factor8.p1m1.ViewModel.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddCategoryDialogFragment extends DialogFragment implements AddCategoryAdapter.OnSubItemClick {
    ViewModel viewModel;
    List<Entity> movieList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_select_category, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final AddCategoryAdapter adapter = new AddCategoryAdapter(movieList, getActivity(), this);

        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getUnCategorisedEntries().observe(this, new Observer<List<Entity>>() {
            @Override
            public void onChanged(List<Entity> entityList) {
                movieList = entityList;
                adapter.updateCurrentList(entityList);
                if(entityList.size() == 0)
                    dismiss();
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_dialogFragment);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);


        return view;
    }

    @Override
    public void OnSubItemClick(int position, int id) {
        int id2 = id+1;
        Toast.makeText(getActivity(), "categoty: "+position+" at entery: "+id2 , Toast.LENGTH_SHORT).show();
        Entity tempUpdated = movieList.get(id);
        tempUpdated.setCategory(position);
        tempUpdated.setCth(tempUpdated.getAmount());//CTH needed to be manually updated here. Because UPDATE QUERY was not updating cth field.
        viewModel.updateEntity(tempUpdated);
    }
}
