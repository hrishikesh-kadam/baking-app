package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.WhichStep;

import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity
        implements RecipeStepAdapter.OnClickStepListener {

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();
    private static final int HIDE = 0;
    private static final int SHOW = 1;
    private Recipe recipe;
    private boolean isDualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "-> onCreate");

        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        isDualPane = findViewById(R.id.guideline) != null;
        Log.i(LOG_TAG, "-> onCreate -> isDualPane = " + isDualPane);

        recipe = getIntent().getParcelableExtra("recipe");

        RecipeStepFragment recipeStepFragment = (RecipeStepFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_recipe_step);
        recipeStepFragment.setAdapterDataWrapper(
                new AdapterDataWrapper(ViewType.NORMAL_VIEW, recipe));

        RecipeStepDetailsFragment recipeStepDetailsFragment = (RecipeStepDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_recipe_step_details);

        if (!isDualPane) {
            setVisibility(recipeStepDetailsFragment, HIDE);
        }
    }

    private void setVisibility(Fragment fragment, int visibility) {

        switch (visibility) {

            case SHOW:
                Log.v(LOG_TAG, "-> setVisibility -> fragment: " + fragment.getClass().getSimpleName() + ", visibility: SHOW");
                getSupportFragmentManager().beginTransaction().show(fragment).commit();
                break;

            case HIDE:
                Log.v(LOG_TAG, "-> setVisibility -> fragment: " + fragment.getClass().getSimpleName() + ", visibility: HIDE");
                getSupportFragmentManager().beginTransaction().hide(fragment).commit();
                break;

            default:
                throw new IllegalArgumentException("Illegal visibility: " + visibility);
        }
    }

    @Override
    public void onClickStep(WhichStep thisStep) {
        Log.v(LOG_TAG, "-> onClickStep -> " + thisStep);
    }
}
