package com.example.android.bakingapp.rest;

import com.example.android.bakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Hrishikesh Kadam on 17/12/2017
 */

public interface WebServices {

    @GET("59121517_baking/baking.json")
    Call<ArrayList<Recipe>> getAllRecipes();
}
