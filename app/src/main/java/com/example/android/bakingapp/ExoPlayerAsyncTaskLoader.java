package com.example.android.bakingapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;

/**
 * Created by Hrishikesh Kadam on 16/12/2017
 */

public class ExoPlayerAsyncTaskLoader extends AsyncTaskLoader {

    public static final int GET_EXOPLAYER = 0;
    private static final String LOG_TAG = ExoPlayerAsyncTaskLoader.class.getSimpleName();
    private Object cachedData;
    private String loaderString;

    public ExoPlayerAsyncTaskLoader(Context context) {
        super(context);

        loaderString = getLoaderString(getId());
        Log.v(LOG_TAG, "-> constructor -> " + loaderString);
    }

    public static String getLoaderString(int id) {

        switch (id) {

            case GET_EXOPLAYER:
                return "GET_EXOPLAYER";

            default:
                throw new UnsupportedOperationException("Unknown loader id = " + id);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.v(LOG_TAG, "-> onStartLoading -> " + loaderString);

        if (cachedData == null)
            forceLoad();
        else
            deliverResult(cachedData);
    }

    @Override
    public Object loadInBackground() {
        Log.v(LOG_TAG, "-> loadInBackground -> " + loaderString);

        if (getId() == GET_EXOPLAYER)
            return getExoplayer();

        return null;
    }

    private Object getExoplayer() {
        Log.v(LOG_TAG, "-> loadInBackground -> getExoplayer -> " + loaderString);

        // Create an instance of the ExoPlayer.
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();

        ExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(
                getContext(), trackSelector, loadControl);

        cachedData = exoPlayer;
        return exoPlayer;
    }
}