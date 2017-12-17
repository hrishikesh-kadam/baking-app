package com.example.android.bakingapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.rest.RetrofitSingleton;
import com.example.android.bakingapp.rest.WebServices;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Hrishikesh Kadam on 17/12/2017
 */

public class MainAsyncTaskLoader extends AsyncTaskLoader {

    public static final int GET_ALL_RECIPES = 0;
    private static final String LOG_TAG = MainAsyncTaskLoader.class.getSimpleName();
    private Object cachedData;

    public MainAsyncTaskLoader(Context context) {
        super(context);
    }

    public static String getLoaderString(int id) {

        switch (id) {

            case GET_ALL_RECIPES:
                return "GET_ALL_RECIPES";

            default:
                throw new UnsupportedOperationException("Unknown id = " + id);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.v(LOG_TAG, "-> onStartLoading -> " + getLoaderString(getId()));

        if (cachedData == null)
            forceLoad();
        else
            deliverResult(cachedData);
    }

    @Override
    public Object loadInBackground() {
        Log.v(LOG_TAG, "-> loadInBackground -> " + getLoaderString(getId()));

        switch (getId()) {

            case MainAsyncTaskLoader.GET_ALL_RECIPES:

                WebServices webServices = RetrofitSingleton.getRetrofit().create(WebServices.class);
                Call<ArrayList<Recipe>> recipeCall = webServices.getAllRecipes();
                Response<ArrayList<Recipe>> recipeResponse = null;

                try {
                    recipeResponse = recipeCall.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                cachedData = recipeResponse;
                return recipeResponse;
        }

        return null;
    }
}
