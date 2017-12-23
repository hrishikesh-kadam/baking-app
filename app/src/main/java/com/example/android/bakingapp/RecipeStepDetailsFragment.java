package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Hrishikesh Kadam on 23/12/2017
 */

public class RecipeStepDetailsFragment extends Fragment {

    private static final String LOG_TAG = RecipeStepDetailsFragment.class.getSimpleName();

    public RecipeStepDetailsFragment() {
        Log.v(LOG_TAG, "-> Constructor");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(LOG_TAG, "-> onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_recipe_step_details, container, false);
        return rootView;
    }
}
