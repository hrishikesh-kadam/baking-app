package com.example.android.bakingapp.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.AdapterDataWrapper;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.ViewType;
import com.example.android.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hrishikesh Kadam on 17/12/2017
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private static final String LOG_TAG = MenuAdapter.class.getSimpleName();
    private Context context;
    private int dataViewType;
    private ArrayList<Recipe> recipesList;
    private int[] foodBlurredImages = {
            R.drawable.food_blurred_image_1, R.drawable.food_blurred_image_2,
            R.drawable.food_blurred_image_3, R.drawable.food_blurred_image_4,
            R.drawable.food_blurred_image_5, R.drawable.food_blurred_image_6};
    private OnClickRecipeListener onClickRecipeListener;

    public MenuAdapter(Context context, AdapterDataWrapper adapterDataWrapper) {

        this.context = context.getApplicationContext();
        dataViewType = adapterDataWrapper.dataViewType;
        //noinspection unchecked
        recipesList = (ArrayList<Recipe>) adapterDataWrapper.data;

        onClickRecipeListener = (OnClickRecipeListener) context;
    }

    public void swapData(AdapterDataWrapper adapterDataWrapper) {
        Log.v(LOG_TAG, "-> swapData");

        dataViewType = adapterDataWrapper.dataViewType;
        //noinspection unchecked
        recipesList = (ArrayList<Recipe>) adapterDataWrapper.data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewHolder viewHolder;
        View itemView;

        switch (dataViewType) {

            case ViewType.NORMAL_VIEW:
                itemView = LayoutInflater.from(context).inflate(
                        R.layout.menu_item, parent, false);
                viewHolder = new NormalViewHolder(itemView);
                break;

            case ViewType.LOADING_VIEW:
                itemView = LayoutInflater.from(context).inflate(
                        R.layout.loading_view, parent, false);
                viewHolder = new ViewHolder(itemView);
                break;

            case ViewType.EMPTY_VIEW:
                itemView = LayoutInflater.from(context).inflate(
                        R.layout.empty_view, parent, false);
                viewHolder = new EmptyViewHolder(itemView);
                break;

            default:
                throw new UnsupportedOperationException("Unknown viewType = " + dataViewType);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        switch (dataViewType) {

            case ViewType.NORMAL_VIEW:
                NormalViewHolder normalViewHolder = (NormalViewHolder) holder;

                normalViewHolder.itemView.setTag(recipesList.get(position).getLocalDbId());
                normalViewHolder.textViewMenu.setText(recipesList.get(position).getName());

                String imageUrl = recipesList.get(position).getImage();
                imageUrl = TextUtils.isEmpty(imageUrl) ? null : imageUrl;

                Picasso.with(context)
                        .load(imageUrl)
                        .placeholder(foodBlurredImages[position % foodBlurredImages.length])
                        .into(normalViewHolder.imageViewMenu);

                break;
        }
    }

    @Override
    public int getItemCount() {

        if (recipesList == null || recipesList.isEmpty())
            return 1;
        else
            return recipesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataViewType;
    }

    public interface OnClickRecipeListener {
        public void onClickRecipe(Long localDbId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class NormalViewHolder extends ViewHolder {

        @BindView(R.id.textViewMenu)
        TextView textViewMenu;

        @BindView(R.id.imageViewMenu)
        ImageView imageViewMenu;

        public NormalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.cardView)
        public void onClickCardView() {
            Log.v(LOG_TAG, "-> NormalViewHolder -> onClickCardView -> localDbId = " + itemView.getTag());
            onClickRecipeListener.onClickRecipe((Long) itemView.getTag());
        }
    }

    public class EmptyViewHolder extends ViewHolder {

        @BindView(R.id.textViewEmptyMessage)
        TextView textViewEmptyMessage;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            textViewEmptyMessage.setText(R.string.empty_recipe_table);
        }
    }
}
