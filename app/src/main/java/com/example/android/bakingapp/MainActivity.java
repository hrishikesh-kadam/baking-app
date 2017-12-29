package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.bakingapp.data.RecipeContract.RecipeEntry;
import com.example.android.bakingapp.model.Recipe;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks,
        MenuAdapter.OnClickReloadListener, MenuAdapter.OnClickMenuItemListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private AdapterDataWrapper menuAdapterDataWrapper;
    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "-> onCreate");

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        recyclerView.setLayoutManager(layoutManager);

        menuAdapterDataWrapper = new AdapterDataWrapper(ViewType.LOADING_VIEW, null);
        menuAdapter = new MenuAdapter(this, menuAdapterDataWrapper);
        recyclerView.setAdapter(menuAdapter);

        getSupportLoaderManager().initLoader(
                MainAsyncTaskLoader.GET_ALL_RECIPES, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "-> onCreateLoader -> " + MainAsyncTaskLoader.getLoaderString(id));

        switch (id) {

            case MainAsyncTaskLoader.GET_ALL_RECIPES:

                getContentResolver().delete(RecipeEntry.CONTENT_URI, null, null);
                return new MainAsyncTaskLoader(this);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        switch (loader.getId()) {

            case MainAsyncTaskLoader.GET_ALL_RECIPES:

                menuAdapterDataWrapper = (AdapterDataWrapper) data;
                Log.v(LOG_TAG, "-> onLoadFinished -> " + MainAsyncTaskLoader.getLoaderString(loader.getId()) + " -> " + menuAdapterDataWrapper.getViewTypeString());
                menuAdapter.swapData(menuAdapterDataWrapper);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.v(LOG_TAG, "-> onLoaderReset -> " + MainAsyncTaskLoader.getLoaderString(loader.getId()));
    }

    @Override
    public void onClickReload() {
        Log.v(LOG_TAG, "-> onClickReload");

        menuAdapterDataWrapper = new AdapterDataWrapper(ViewType.LOADING_VIEW, null);
        menuAdapter.swapData(menuAdapterDataWrapper);
        getSupportLoaderManager().restartLoader(MainAsyncTaskLoader.GET_ALL_RECIPES, null, this);
    }

    @Override
    public void onClickMenuItem(Recipe recipe) {
        Log.v(LOG_TAG, "-> onClickMenuItem");

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }
}
