package com.example.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.RecipeContract.RecipeEntry;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Hrishikesh Kadam on 29/12/2017
 */

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new ListRemoteViewsFactory(
                this.getApplicationContext(), intent.getData());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final String LOG_TAG = ListRemoteViewsFactory.class.getSimpleName();
    Context applicationContext;
    Uri uri;
    ArrayList<Ingredient> ingredientArrayList;
    ArrayList<String> stringArrayList;

    public ListRemoteViewsFactory(Context applicationContext, Uri uri) {
        this.applicationContext = applicationContext;
        this.uri = uri;
        constructStringArrayList();
    }

    public void constructStringArrayList() {

        stringArrayList = new ArrayList<>();

        Cursor cursor = applicationContext.getContentResolver()
                .query(uri, null, null, null, null);

        if (cursor == null || cursor.getCount() == 0)
            return;

        cursor.moveToFirst();
        Gson gson = new Gson();
        String recipeJson = cursor.getString(cursor.getColumnIndex(RecipeEntry.COLUMN_JSON));
        Recipe recipe = gson.fromJson(recipeJson, Recipe.class);
        ingredientArrayList = (ArrayList<Ingredient>) recipe.getIngredients();
        cursor.close();

        Iterator<Ingredient> iterator = ingredientArrayList.iterator();

        while (iterator.hasNext()) {

            Ingredient ingredient = iterator.next();

            String measureString = getMeasureString(ingredient.getMeasure());
            if (!measureString.isEmpty())
                measureString += " ";

            float quantity = ingredient.getQuantity();
            int quantityInteger = (int) quantity;
            String quantityString;
            if (quantity == quantityInteger)
                quantityString = String.valueOf(quantityInteger);
            else
                quantityString = String.valueOf(quantity);

            String tempString = quantityString + " " + measureString + ingredient.getIngredient();
            stringArrayList.add(tempString);
        }
    }

    public String getMeasureString(String measure) {

        switch (measure) {
            case "K":
                return applicationContext.getString(R.string.kilogram);
            case "G":
                return applicationContext.getString(R.string.gram);
            case "CUP":
                return applicationContext.getString(R.string.cup);
            case "TBLSP":
                return applicationContext.getString(R.string.tblsp);
            case "TSP":
                return applicationContext.getString(R.string.tsp);
            case "OZ":
                return applicationContext.getString(R.string.oz);
            default:
                return "";
        }
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (stringArrayList == null)
            return 0;
        else
            return stringArrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews remoteViews = new RemoteViews(applicationContext.getPackageName(), R.layout.list_item_widget);
        remoteViews.setTextViewText(R.id.textViewIngredient, stringArrayList.get(position));
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
