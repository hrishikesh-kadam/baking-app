package com.example.android.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.android.bakingapp.data.RecipeContract.RecipeEntry;
import com.example.android.bakingapp.model.Recipe;
import com.google.gson.Gson;

public class IngredientWidgetService extends IntentService {

    private static final String LOG_TAG = IngredientWidgetService.class.getSimpleName();
    private static final String ACTION_RECIPE_CHOSEN =
            "com.example.android.bakingapp.action.RECIPE_CHOSEN";

    public IngredientWidgetService() {
        super("IngredientWidgetService");
    }

    public static void startActionRecipeChosen(Context context, long localDbId) {
        Log.v(LOG_TAG, "-> startActionRecipeChosen");

        Intent intent = new Intent(context, IngredientWidgetService.class);
        intent.setAction(ACTION_RECIPE_CHOSEN);
        intent.putExtra("localDbId", localDbId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {

            if (ACTION_RECIPE_CHOSEN.equals(intent.getAction())) {
                Log.v(LOG_TAG, "-> onHandleIntent -> " + ACTION_RECIPE_CHOSEN);
                handleActionRecipeChosen(intent);
            }
        }
    }

    private void handleActionRecipeChosen(Intent intent) {
        Log.v(LOG_TAG, "-> handleActionRecipeChosen");

        long localDbId = intent.getLongExtra("localDbId", 0);

        if (localDbId <= 0) {
            Log.e(LOG_TAG, "-> handleActionRecipeChosen -> localDbId = " + localDbId);
            return;
        }

        Uri uri = RecipeEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(localDbId)).build();
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor == null || cursor.getCount() == 0) {
            Log.e(LOG_TAG, "-> handleActionRecipeChosen -> query command failed");
            return;
        }

        cursor.moveToFirst();

        Gson gson = new Gson();
        String recipeJson = cursor.getString(cursor.getColumnIndex(RecipeEntry.COLUMN_JSON));
        Recipe recipe = gson.fromJson(recipeJson, Recipe.class);
        recipe.setLocalDbId(localDbId);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(this, RecipeWidgetProvider.class));

        RecipeWidgetProvider.updateIngredientList(this, appWidgetManager, appWidgetIds, recipe);

        cursor.close();
    }
}
