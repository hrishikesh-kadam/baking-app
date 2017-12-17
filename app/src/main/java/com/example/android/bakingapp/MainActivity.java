package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.bakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "-> onCreate");

        setContentView(R.layout.activity_main);

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
        Log.v(LOG_TAG, "-> onLoadFinished -> " + MainAsyncTaskLoader.getLoaderString(loader.getId()));

        switch (loader.getId()) {

            case MainAsyncTaskLoader.GET_ALL_RECIPES:

                @SuppressWarnings("unchecked")
                Response<ArrayList<Recipe>> recipeResponse = (Response<ArrayList<Recipe>>) data;
                Log.d(LOG_TAG, "-> " + recipeResponse.body());

                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.v(LOG_TAG, "-> onLoaderReset -> " + MainAsyncTaskLoader.getLoaderString(loader.getId()));
    }
}
