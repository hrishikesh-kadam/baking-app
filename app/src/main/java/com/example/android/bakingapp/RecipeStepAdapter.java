package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hrishikesh Kadam on 22/12/2017
 */

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.ViewHolder> {

    private static final String LOG_TAG = RecipeStepAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<String> stepsArrayList = new ArrayList<>();
    private int dataViewType;

    public RecipeStepAdapter(Context context, AdapterDataWrapper adapterDataWrapper) {

        this.context = context.getApplicationContext();

        Recipe recipe = (Recipe) adapterDataWrapper.data;

        ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>) recipe.getIngredients();
        if (ingredients != null && !ingredients.isEmpty())
            stepsArrayList.add(context.getString(R.string.recipe_ingredients));

        ArrayList<Step> steps = (ArrayList<Step>) recipe.getSteps();
        if (steps != null && !steps.isEmpty())
            for (Step step : steps)
                stepsArrayList.add(step.getShortDescription());

        dataViewType = adapterDataWrapper.dataViewType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recipe_step_item, parent, false);

        return new NormalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
        normalViewHolder.textViewStep.setText(stepsArrayList.get(position));
    }

    @Override
    public int getItemCount() {

        return stepsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class NormalViewHolder extends ViewHolder {

        @BindView(R.id.textViewStep)
        TextView textViewStep;

        public NormalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.textViewStep)
        public void onClickStep(TextView textView) {
            Log.v(LOG_TAG, "-> onClickStep -> " + textView.getText());

        }
    }
}
