package com.example.android.bakingapp.widget;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.bakingapp.AdapterDataWrapper;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.ViewType;
import com.example.android.bakingapp.data.RecipeContract.RecipeEntry;
import com.example.android.bakingapp.model.Recipe;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseRecipeActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        MenuAdapter.OnClickRecipeListener {

    public static final String LOG_TAG = ChooseRecipeActivity.class.getSimpleName();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private AdapterDataWrapper menuAdapterDataWrapper;
    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "-> onCreate");

        setContentView(R.layout.activity_choose_recipe);
        ButterKnife.bind(this);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        recyclerView.setLayoutManager(layoutManager);

        menuAdapterDataWrapper = new AdapterDataWrapper(ViewType.LOADING_VIEW, null);
        menuAdapter = new MenuAdapter(this, menuAdapterDataWrapper);
        recyclerView.setAdapter(menuAdapter);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "-> onCreateLoader");

        return new CursorLoader(this, RecipeEntry.CONTENT_URI, null,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(LOG_TAG, "-> onLoadFinished");

        if (cursor == null || cursor.getCount() == 0) {

            menuAdapterDataWrapper = new AdapterDataWrapper(ViewType.EMPTY_VIEW, null);

        } else {

            Gson gson = new Gson();
            ArrayList<Recipe> recipeArrayList = new ArrayList<>();

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                Recipe recipe = gson.fromJson(
                        cursor.getString(cursor.getColumnIndex(RecipeEntry.COLUMN_JSON)),
                        Recipe.class);
                recipe.setLocalDbId(cursor.getLong(cursor.getColumnIndex(RecipeEntry._ID)));
                recipeArrayList.add(recipe);
            }

            menuAdapterDataWrapper = new AdapterDataWrapper(ViewType.NORMAL_VIEW, recipeArrayList);
        }

        menuAdapter.swapData(menuAdapterDataWrapper);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.v(LOG_TAG, "-> onLoaderReset");
    }

    @Override
    public void onClickRecipe(Long localDbId) {
        Log.v(LOG_TAG, "-> onClickRecipe -> localDbId = " + localDbId);

        IngredientWidgetService.startActionRecipeChosen(this, localDbId);
        finish();
    }
}
