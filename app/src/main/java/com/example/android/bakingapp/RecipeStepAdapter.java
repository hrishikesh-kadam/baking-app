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
import com.example.android.bakingapp.model.WhichStep;

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
    private ArrayList<WhichStep> whichStepsList = new ArrayList<>();
    private int dataViewType;
    private OnClickStepListener onClickStepListener;

    public RecipeStepAdapter(Context context, AdapterDataWrapper adapterDataWrapper) {

        this.context = context.getApplicationContext();
        onClickStepListener = (OnClickStepListener) context;

        Recipe recipe = (Recipe) adapterDataWrapper.data;

        ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>) recipe.getIngredients();
        if (ingredients != null && !ingredients.isEmpty())
            whichStepsList.add(new WhichStep(context.getString(R.string.recipe_ingredients),
                    "ingredients", 0));

        ArrayList<Step> steps = (ArrayList<Step>) recipe.getSteps();
        if (steps != null && !steps.isEmpty())
            for (int i = 0; i < steps.size(); i++)
                whichStepsList.add(new WhichStep(steps.get(i).getShortDescription(), "steps", i));

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
        normalViewHolder.textViewStep.setText(whichStepsList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {

        return whichStepsList.size();
    }

    public interface OnClickStepListener {
        public void onClickStep(WhichStep thisStep);
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
            onClickStepListener.onClickStep(whichStepsList.get(getAdapterPosition()));
        }
    }
}
