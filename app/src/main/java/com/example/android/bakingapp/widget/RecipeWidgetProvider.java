package com.example.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.RecipeContract;
import com.example.android.bakingapp.model.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = RecipeWidgetProvider.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.v(LOG_TAG, "-> updateAppWidget");

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget);

        Intent chooseRecipeIntent = new Intent(context, ChooseRecipeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, chooseRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.buttonChooseRecipe, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateIngredientList(Context context, AppWidgetManager appWidgetManager,
                                            int[] appWidgetIds, Recipe recipe) {
        Log.v(LOG_TAG, "-> updateIngredientList");

        for (int appWidgetId : appWidgetIds) {

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget);

            Intent chooseRecipeIntent = new Intent(context, ChooseRecipeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 0, chooseRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.buttonChooseRecipe, pendingIntent);

            views.setTextViewText(R.id.textViewIngredientHeader,
                    context.getString(R.string.holder_ingredients_list, recipe.getName()));

            Intent listWidgetServiceIntent = new Intent(context, ListWidgetService.class);
            Uri uri = RecipeContract.RecipeEntry.CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(recipe.getLocalDbId())).build();
            listWidgetServiceIntent.setData(uri);
            views.setRemoteAdapter(R.id.listView, listWidgetServiceIntent);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.v(LOG_TAG, "-> onUpdate");

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

