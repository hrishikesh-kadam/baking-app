package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.WhichStep;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity
        implements RecipeStepAdapter.OnClickStepListener,
        RecipeStepAdapter.SetWhichStepListInterface {

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();
    private static final String INGREDIENTS = "ingredients";
    private static final int HIDE = 0;
    private static final int SHOW = 1;
    private Recipe recipe;
    private boolean isDualPane;
    private RecipeStepFragment recipeStepFragment;
    private RecipeStepDetailsFragment recipeStepDetailsFragment;
    private ArrayList<WhichStep> whichStepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "-> onCreate");

        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isDualPane = findViewById(R.id.guideline) != null;
        Log.i(LOG_TAG, "-> onCreate -> isDualPane = " + isDualPane);

        recipe = getIntent().getParcelableExtra("recipe");

        initRecipeStepFragment();
        initRecipeStepDetailsFragment();

        if (!isDualPane) {
            setVisibility(recipeStepDetailsFragment, HIDE, 0, 0);
        } else {

        }
    }

    private void initRecipeStepFragment() {
        Log.v(LOG_TAG, "-> initRecipeStepFragment");

        recipeStepFragment = (RecipeStepFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_recipe_step);
        recipeStepFragment.setRecipe(recipe);
    }

    private void initRecipeStepDetailsFragment() {
        Log.v(LOG_TAG, "-> initRecipeStepDetailsFragment");

        recipeStepDetailsFragment = (RecipeStepDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_recipe_step_details);
        recipeStepDetailsFragment.setDualPane(isDualPane);
        recipeStepDetailsFragment.setRecipe(recipe);
        recipeStepDetailsFragment.setWhichStepList(whichStepList);

        if (whichStepList == null || whichStepList.size() == 0)
            recipeStepDetailsFragment.onClickStep(-1);
        else
            recipeStepDetailsFragment.onClickStep(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                Log.v(LOG_TAG, "-> home pressed");
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.v(LOG_TAG, "-> onBackPressed");

        if (isDualPane) {
            finish();
        } else {

            if (recipeStepDetailsFragment.isVisible()) {
                setVisibility(recipeStepDetailsFragment, HIDE, 0, R.anim.slide_out_top_to_bottom);
            } else {
                finish();
            }
        }
    }

    private void setVisibility(Fragment fragment, int visibility,
                               int enter, int exit) {

        switch (visibility) {

            case SHOW:
                Log.v(LOG_TAG, "-> setVisibility -> fragment: " + fragment.getClass().getSimpleName() + ", visibility: SHOW");
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(enter, exit)
                        .show(fragment).commit();

                break;

            case HIDE:
                Log.v(LOG_TAG, "-> setVisibility -> fragment: " + fragment.getClass().getSimpleName() + ", visibility: HIDE");
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(enter, exit)
                        .hide(fragment).commit();
                break;

            default:
                throw new IllegalArgumentException("Illegal visibility: " + visibility);
        }
    }

    @Override
    public void onClickStep(int index) {
        Log.v(LOG_TAG, "-> onClickStep");

        if (isDualPane) {

        } else {
            setVisibility(recipeStepDetailsFragment, SHOW, R.anim.slide_in_bottom_to_top, 0);
        }

        recipeStepDetailsFragment.onClickStep(index);
    }

    @Override
    public void setWhichStepList(ArrayList<WhichStep> whichStepList) {
        Log.v(LOG_TAG, "-> setWhichStepList");
        this.whichStepList = whichStepList;
    }
}
