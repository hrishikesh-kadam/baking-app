package com.example.android.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Hrishikesh Kadam on 28/12/2017
 */

public class RecipeContract {

    public static final String AUTHORITY = "com.example.android.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String RECIPE_PATH = "recipe";

    public static final class RecipeEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(RECIPE_PATH).build();
        public static final String TABLE_NAME = "recipe";
        public static final String COLUMN_JSON = "recipeJson";
    }
}
