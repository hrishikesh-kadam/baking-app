package com.example.android.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.bakingapp.data.RecipeContract.RecipeEntry;

/**
 * Created by Hrishikesh Kadam on 28/12/2017
 */

public class RecipeContentProvider extends ContentProvider {

    public static final int RECIPE = 100;
    public static final int RECIPE_WITH_ID = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private RecipeDbHelper recipeDbHelper;

    private static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.RECIPE_PATH, RECIPE);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.RECIPE_PATH + "/#", RECIPE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {

        recipeDbHelper = new RecipeDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = recipeDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);

        switch (match) {

            case RECIPE:
                cursor = db.query(RecipeEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case RECIPE_WITH_ID:
                cursor = db.query(RecipeEntry.TABLE_NAME, projection, RecipeEntry._ID + "=?",
                        new String[]{uri.getLastPathSegment()}, null, null, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = recipeDbHelper.getWritableDatabase();
        int noOfRowsInserted = 0;
        int match = uriMatcher.match(uri);

        switch (match) {

            case RECIPE:

                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        db.insert(RecipeEntry.TABLE_NAME, null, value);
                        ++noOfRowsInserted;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (noOfRowsInserted > 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return noOfRowsInserted;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new UnsupportedOperationException("insert not yet implemented");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = recipeDbHelper.getWritableDatabase();
        int noOfRowsDeleted;
        int match = uriMatcher.match(uri);

        switch (match) {

            case RECIPE:

                noOfRowsDeleted = db.delete(RecipeEntry.TABLE_NAME, null, null);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (noOfRowsDeleted > 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return noOfRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("update not yet implemented");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("getType not yet implemented");
    }
}
