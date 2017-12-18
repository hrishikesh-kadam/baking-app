package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private AdapterDataWrapper menuAdapterDataWrapper;
    private MenuAdapter menuAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

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

        menuAdapter = new MenuAdapter(this, new AdapterDataWrapper(ViewType.LOADING_VIEW, null));
        recyclerView.setAdapter(menuAdapter);

        getSupportLoaderManager().initLoader(
                MainAsyncTaskLoader.GET_ALL_RECIPES, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "-> onCreateLoader -> " + MainAsyncTaskLoader.getLoaderString(id));

        switch (id) {

            case MainAsyncTaskLoader.GET_ALL_RECIPES:
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
}
