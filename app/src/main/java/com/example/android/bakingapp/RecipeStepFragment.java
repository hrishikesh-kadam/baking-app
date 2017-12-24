package com.example.android.bakingapp;

import android.os.Bundle;
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

public class RecipeStepFragment extends Fragment {

    private static final String LOG_TAG = RecipeStepFragment.class.getSimpleName();
    @BindView(R.id.recipeStepRecyclerView)
    RecyclerView recipeStepRecyclerView;
    private Recipe recipe;
    private RecipeStepAdapter recipeStepAdapter;

    public RecipeStepFragment() {
        Log.v(LOG_TAG, "-> Constructor");
    }

    public void setRecipe(Recipe recipe) {
        Log.v(LOG_TAG, "-> setRecipe");

        this.recipe = recipe;
        recipeStepAdapter = new RecipeStepAdapter(
                getActivity(), new AdapterDataWrapper(ViewType.NORMAL_VIEW, recipe));
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
