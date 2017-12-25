package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepFragment extends Fragment
        implements RecipeStepAdapter.UpdateIndexInFragmentCallback {

    private static final String LOG_TAG = RecipeStepFragment.class.getSimpleName();
    @BindView(R.id.recipeStepRecyclerView)
    RecyclerView recipeStepRecyclerView;
    private Recipe recipe;
    private RecipeStepAdapter recipeStepAdapter;
    private boolean isDualPane;
    private int selectedStepIndex;

    public RecipeStepFragment() {
        Log.v(LOG_TAG, "-> Constructor");
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(LOG_TAG, "-> onSaveInstanceState");

        outState.putBoolean("isDualPane", isDualPane);
        outState.putInt("selectedStepIndex", selectedStepIndex);
        outState.putParcelable("recipe", recipe);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(LOG_TAG, "-> onActivityCreated");

        if (savedInstanceState == null)
            return;

        isDualPane = savedInstanceState.getBoolean("isDualPane");
        selectedStepIndex = savedInstanceState.getInt("selectedStepIndex");
        setRecipe((Recipe) savedInstanceState.getParcelable("recipe"));
    }

    public void setDualPane(boolean isDualPane) {
        Log.v(LOG_TAG, "-> setDualPane");

        this.isDualPane = isDualPane;
    }

    public void updateIndex(int index) {
        Log.v(LOG_TAG, "-> updateIndex");

        selectedStepIndex = index;
        recipeStepAdapter.updateIndex(index);
    }

    public void setRecipe(Recipe recipe) {
        Log.v(LOG_TAG, "-> setRecipe");

        this.recipe = recipe;
        recipeStepAdapter = new RecipeStepAdapter(
                this, new AdapterDataWrapper(ViewType.NORMAL_VIEW, recipe), isDualPane);
        recipeStepAdapter.updateIndex(selectedStepIndex);
        recipeStepRecyclerView.setAdapter(recipeStepAdapter);
    }

    @Override
    public void updateIndexInFragment(int index) {
        Log.v(LOG_TAG, "-> updateIndexInFragment");

        selectedStepIndex = index;
    }
}