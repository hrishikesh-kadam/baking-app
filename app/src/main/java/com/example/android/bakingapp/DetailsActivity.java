package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.bakingapp.model.Recipe;

public class DetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "-> onCreate");

        setContentView(R.layout.activity_details);

        recipe = getIntent().getParcelableExtra("recipe");

        RecipeStepFragment recipeStepFragment = (RecipeStepFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_recipe_step);
        recipeStepFragment.setAdapterDataWrapper(
                new AdapterDataWrapper(ViewType.NORMAL_VIEW, recipe));
    }
}
