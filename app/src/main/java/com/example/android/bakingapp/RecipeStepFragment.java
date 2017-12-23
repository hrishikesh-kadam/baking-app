package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepFragment extends Fragment {

    private static final String LOG_TAG = RecipeStepFragment.class.getSimpleName();
    @BindView(R.id.recipeStepRecyclerView)
    RecyclerView recipeStepRecyclerView;
    private AdapterDataWrapper adapterDataWrapper;
    private RecipeStepAdapter recipeStepAdapter;

    public RecipeStepFragment() {
        Log.v(LOG_TAG, "-> Constructor");
    }

    public void setAdapterDataWrapper(AdapterDataWrapper adapterDataWrapper) {
        Log.v(LOG_TAG, "-> setAdapterDataWrapper");

        this.adapterDataWrapper = adapterDataWrapper;
        recipeStepAdapter = new RecipeStepAdapter(getActivity(), adapterDataWrapper);
        recipeStepRecyclerView.setAdapter(recipeStepAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "-> onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, rootView);
        recipeStepRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return rootView;
    }

}
