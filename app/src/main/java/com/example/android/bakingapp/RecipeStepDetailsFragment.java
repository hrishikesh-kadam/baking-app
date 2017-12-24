package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.WhichStep;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by Hrishikesh Kadam on 23/12/2017
 */

public class RecipeStepDetailsFragment extends Fragment {

    private static final String LOG_TAG = RecipeStepDetailsFragment.class.getSimpleName();
    @BindView(R.id.exoPlayerView)
    SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.textViewDescription)
    TextView textViewDescription;
    @Nullable
    @BindView(R.id.viewFooterNext)
    View viewFooterNext;
    @Nullable
    @BindView(R.id.viewFooterPrevious)
    View viewFooterPrevious;
    private boolean isDualPane;
    private Recipe recipe;
    private ArrayList<WhichStep> whichStepList;
    private WhichStep thisStep;
    private int indexWhichStep;

    public RecipeStepDetailsFragment() {
        Log.v(LOG_TAG, "-> Constructor");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(LOG_TAG, "-> onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_recipe_step_details, container, false);
        ButterKnife.bind(this, rootView);
        textViewDescription.setMovementMethod(new ScrollingMovementMethod());
        return rootView;
    }

    public void setRecipe(Recipe recipe) {
        Log.v(LOG_TAG, "-> setRecipe");
        this.recipe = recipe;
    }

    public void setWhichStepList(ArrayList<WhichStep> whichStepList) {
        Log.v(LOG_TAG, "-> setWhichStepList");
        this.whichStepList = whichStepList;
    }

    public void onClickStep(int index) {
        Log.v(LOG_TAG, "-> onClickStep");

        indexWhichStep = index;
        thisStep = whichStepList.get(indexWhichStep);
        setVisibilityPreviousOrNext();
        textViewDescription.setText(getDescription());
    }

    private String getDescription() {

        switch (thisStep.getType()) {

            case "ingredients":
                StringBuilder description = new StringBuilder();
                description.append("\n");

                Iterator<Ingredient> iterator = recipe.getIngredients().iterator();

                while (iterator.hasNext()) {

                    Ingredient ingredient = iterator.next();

                    String measureString = getMeasureString(ingredient.getMeasure());
                    if (!measureString.isEmpty())
                        measureString += " ";

                    float quantity = ingredient.getQuantity();
                    int quantityInteger = (int) quantity;
                    String quantityString;
                    if (quantity == quantityInteger)
                        quantityString = String.valueOf(quantityInteger);
                    else
                        quantityString = String.valueOf(quantity);

                    description.append(quantityString).append(" ")
                            .append(measureString)
                            .append(ingredient.getIngredient());

                    if (iterator.hasNext())
                        description.append("\n\n");
                    else
                        description.append("\n");
                }

                return description.toString();

            case "steps":
                return "\n" + recipe.getSteps().get(thisStep.getIndex()).getDescription() + "\n";

            default:
                return "NA";
        }
    }

    public String getMeasureString(String measure) {

        switch (measure) {
            case "K":
                return getString(R.string.kilogram);
            case "G":
                return getString(R.string.gram);
            case "CUP":
                return getString(R.string.cup);
            case "TBLSP":
                return getString(R.string.tblsp);
            case "TSP":
                return getString(R.string.tsp);
            case "OZ":
                return getString(R.string.oz);
            default:
                return "";
        }
    }

    public void setDualPane(boolean isDualPane) {
        this.isDualPane = isDualPane;
    }

    @Optional
    @OnClick({R.id.viewFooterPrevious, R.id.viewFooterNext})
    public void onClickFooterPreviousOrNext(View view) {

        int i = indexWhichStep;

        switch (view.getId()) {
            case R.id.viewFooterPrevious:
                Log.v(LOG_TAG, "-> onClickFooterPreviousOrNext -> viewFooterPrevious");
                --i;
                break;
            case R.id.viewFooterNext:
                Log.v(LOG_TAG, "-> onClickFooterPreviousOrNext -> viewFooterNext");
                ++i;
                break;
            default:
                Log.e(LOG_TAG, "-> onClickFooterPreviousOrNext -> Unknown view found");
                return;
        }

        if (indexWhichStep <= -1) {

        } else if (i <= -1) {

        } else if (i >= whichStepList.size()) {

        } else {
            indexWhichStep = i;
        }

        onClickStep(indexWhichStep);
    }

    public void setVisibilityPreviousOrNext() {
        Log.v(LOG_TAG, "-> setVisibilityPreviousOrNext");

        if (viewFooterPrevious == null || viewFooterNext == null)
            return;

        if (whichStepList.size() <= 1) {

            viewFooterPrevious.setVisibility(View.INVISIBLE);
            viewFooterNext.setVisibility(View.INVISIBLE);

        } else {

            if (indexWhichStep == 0) {

                viewFooterPrevious.setVisibility(View.INVISIBLE);
                viewFooterNext.setVisibility(View.VISIBLE);

            } else if (indexWhichStep == whichStepList.size() - 1) {

                viewFooterPrevious.setVisibility(View.VISIBLE);
                viewFooterNext.setVisibility(View.INVISIBLE);

            } else {

                viewFooterPrevious.setVisibility(View.VISIBLE);
                viewFooterNext.setVisibility(View.VISIBLE);
            }
        }
    }
}
