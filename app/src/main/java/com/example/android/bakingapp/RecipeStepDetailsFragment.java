package com.example.android.bakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.WhichStep;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by Hrishikesh Kadam on 23/12/2017
 */

public class RecipeStepDetailsFragment extends Fragment
        implements LoaderManager.LoaderCallbacks, Player.EventListener {

    private static final String LOG_TAG = RecipeStepDetailsFragment.class.getSimpleName();
    private static MediaSessionCompat mediaSession;
    @BindView(R.id.exoPlayerView)
    SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.textViewDescription)
    TextView textViewDescription;
    @Nullable
    @BindView(R.id.viewFooterNext)
    View viewFooterNext;
    @Nullable
    @BindView(R.id.viewFooterPrevious)
    View viewFooterPrevious;
    @BindView(R.id.custom_prev)
    View viewCustomPrev;
    @BindView(R.id.custom_next)
    View viewCustomNext;
    @BindView(R.id.exo_play)
    View viewExoPlay;
    private PlaybackStateCompat.Builder stateBuilder;
    private boolean isDualPane;
    private Recipe recipe;
    private ArrayList<WhichStep> whichStepList;
    private WhichStep thisStep;
    private int indexWhichStep;
    private SimpleExoPlayer exoPlayer;
    private DefaultDataSourceFactory dataSourceFactory;
    private ExtractorsFactory extractorsFactory;
    private String mediaUrl;
    private boolean hasInstanceStateSaved;
    private UpdateIndexCallbackInterface updateIndexCallback;

    public RecipeStepDetailsFragment() {
        Log.v(LOG_TAG, "-> Constructor");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(LOG_TAG, "-> onCreateView");

        updateIndexCallback = (UpdateIndexCallbackInterface) getActivity();
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_details, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(LOG_TAG, "-> onSaveInstanceState");

        outState.putBoolean("isDualPane", isDualPane);
        outState.putParcelable("recipe", recipe);
        outState.putParcelableArrayList("whichStepList", whichStepList);
        outState.putParcelable("thisStep", thisStep);
        outState.putInt("indexWhichStep", indexWhichStep);
        outState.putBoolean("useController", exoPlayerView.getUseController());
        outState.putBoolean("isViewExoPlayEnabled", viewExoPlay.isEnabled());
        outState.putFloat("viewExoPlayAlpha", viewExoPlay.getAlpha());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(LOG_TAG, "-> onActivityCreated");

        initializeMediaSession();

        if (savedInstanceState != null) {

            isDualPane = savedInstanceState.getBoolean("isDualPane");
            recipe = savedInstanceState.getParcelable("recipe");
            whichStepList = savedInstanceState.getParcelableArrayList("whichStepList");
            thisStep = savedInstanceState.getParcelable("thisStep");
            indexWhichStep = savedInstanceState.getInt("indexWhichStep");
            exoPlayerView.setUseController(savedInstanceState.getBoolean("useController"));
            viewExoPlay.setEnabled(savedInstanceState.getBoolean("isViewExoPlayEnabled"));
            viewExoPlay.setAlpha(savedInstanceState.getFloat("viewExoPlayAlpha"));

            onClickStep(indexWhichStep);
            hasInstanceStateSaved = true;

        } else
            hasInstanceStateSaved = false;

        getLoaderManager().initLoader(ExoPlayerAsyncTaskLoader.GET_EXOPLAYER, null, this);
    }

    private void initializeMediaSession() {
        Log.v(LOG_TAG, "-> initializeMediaSession");

        // Create a MediaSessionCompat.
        mediaSession = new MediaSessionCompat(getContext().getApplicationContext(), LOG_TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller.
        mediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mediaSession.setActive(true);
    }

    public void onClickStep(int index) {
        Log.v(LOG_TAG, "-> onClickStep");

        indexWhichStep = index;
        thisStep = whichStepList.get(indexWhichStep);
        setVisibilityPreviousOrNext();
        textViewDescription.setText(getDescription());

        mediaUrl = getMediaUrl();
        Log.d(LOG_TAG, "-> onClickStep -> " + mediaUrl);

        if (exoPlayer != null)
            playMediaUrl(mediaUrl);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (exoPlayer != null)
            exoPlayer.removeListener(this);

        mediaSession.setActive(false);
        mediaSession.setCallback(null);
        mediaSession.release();

        exoPlayerView.setPlayer(null);
    }

    public void setRecipe(Recipe recipe) {
        Log.v(LOG_TAG, "-> setRecipe");
        this.recipe = recipe;
    }

    public void setWhichStepList(ArrayList<WhichStep> whichStepList) {
        Log.v(LOG_TAG, "-> setWhichStepList");
        this.whichStepList = whichStepList;
    }

    private String getMediaUrl() {

        switch (thisStep.getType()) {

            case "steps":
                String videoUrl = recipe.getSteps().get(thisStep.getIndex()).getVideoURL();
                return TextUtils.isEmpty(videoUrl) ? "" : videoUrl;

            default:
                return "";
        }
    }

    private String getDescription() {

        switch (thisStep.getType()) {

            case "ingredients":
                StringBuilder description = new StringBuilder();
                description.append("\n");

                Iterator<Ingredient> iterator = recipe.getIngredients().iterator();

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

                    description.append(quantityString).append(" ")
                            .append(measureString)
                            .append(ingredient.getIngredient());

                    if (iterator.hasNext())
                        description.append("\n\n");
                    else
                        description.append("\n");
                }

                return description.toString();

            case "steps":
                return "\n" + recipe.getSteps().get(thisStep.getIndex()).getDescription() + "\n";

            default:
                return "NA";
        }
    }

    public String getMeasureString(String measure) {

        switch (measure) {
            case "K":
                return getString(R.string.kilogram);
            case "G":
                return getString(R.string.gram);
            case "CUP":
                return getString(R.string.cup);
            case "TBLSP":
                return getString(R.string.tblsp);
            case "TSP":
                return getString(R.string.tsp);
            case "OZ":
                return getString(R.string.oz);
            default:
                return "";
        }
    }

    public void setDualPane(boolean isDualPane) {
        this.isDualPane = isDualPane;
    }

    @Optional
    @OnClick({R.id.viewFooterPrevious, R.id.viewFooterNext, R.id.custom_prev, R.id.custom_next})
    public void onClickFooterPreviousOrNext(View view) {

        int i = indexWhichStep;

        switch (view.getId()) {
            case R.id.viewFooterPrevious:
            case R.id.custom_prev:
                Log.v(LOG_TAG, "-> onClickFooterPreviousOrNext -> viewFooterPrevious");
                --i;
                break;
            case R.id.viewFooterNext:
            case R.id.custom_next:
                Log.v(LOG_TAG, "-> onClickFooterPreviousOrNext -> viewFooterNext");
                ++i;
                break;
            default:
                Log.e(LOG_TAG, "-> onClickFooterPreviousOrNext -> Unknown view found");
                return;
        }

        if (indexWhichStep <= -1) {

        } else if (i <= -1) {

        } else if (i >= whichStepList.size()) {

        } else {
            indexWhichStep = i;
        }

        updateIndexCallback.updateIndex(indexWhichStep);
        onClickStep(indexWhichStep);
    }

    public void setVisibilityPreviousOrNext() {
        Log.v(LOG_TAG, "-> setVisibilityPreviousOrNext");

        if (whichStepList.size() <= 1) {

            if (viewFooterPrevious != null)
                viewFooterPrevious.setVisibility(View.INVISIBLE);
            if (viewFooterNext != null)
                viewFooterNext.setVisibility(View.INVISIBLE);

            viewCustomPrev.setVisibility(View.INVISIBLE);
            viewCustomNext.setVisibility(View.INVISIBLE);

        } else {

            if (indexWhichStep == 0) {

                if (viewFooterPrevious != null)
                    viewFooterPrevious.setVisibility(View.INVISIBLE);
                if (viewFooterNext != null)
                    viewFooterNext.setVisibility(View.VISIBLE);

                viewCustomPrev.setVisibility(View.INVISIBLE);
                viewCustomNext.setVisibility(View.VISIBLE);

            } else if (indexWhichStep == whichStepList.size() - 1) {

                if (viewFooterPrevious != null)
                    viewFooterPrevious.setVisibility(View.VISIBLE);
                if (viewFooterNext != null)
                    viewFooterNext.setVisibility(View.INVISIBLE);

                viewCustomPrev.setVisibility(View.VISIBLE);
                viewCustomNext.setVisibility(View.INVISIBLE);

            } else {

                if (viewFooterPrevious != null)
                    viewFooterPrevious.setVisibility(View.VISIBLE);
                if (viewFooterNext != null)
                    viewFooterNext.setVisibility(View.VISIBLE);

                viewCustomPrev.setVisibility(View.VISIBLE);
                viewCustomNext.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initializePlayer() {
        Log.v(LOG_TAG, "-> initializePlayer");

        exoPlayerView.setPlayer(exoPlayer);
        exoPlayerView.setControllerShowTimeoutMs(-1);
        exoPlayerView.showController();

        // Set the ExoPlayer.EventListener to this activity.
        exoPlayer.addListener(this);

        String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));

        // Default parameters, except allowCrossProtocolRedirects is true
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(
                userAgent,
                null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true);

        dataSourceFactory = new DefaultDataSourceFactory(
                getContext(),
                null,
                httpDataSourceFactory);

        extractorsFactory = new DefaultExtractorsFactory();
    }

    public void playMediaUrl(String url) {
        Log.v(LOG_TAG, "-> playMediaUrl");

        MediaSource mediaSource = new ExtractorMediaSource(
                Uri.parse(url), dataSourceFactory, extractorsFactory, null, null);

        exoPlayer.prepare(mediaSource);

        if (url != null && !url.isEmpty()) {

            viewExoPlay.setAlpha(1.0f);
            viewExoPlay.setEnabled(true);
            exoPlayer.setPlayWhenReady(true);

        } else {
            viewExoPlay.setAlpha(0.5f);
            viewExoPlay.setEnabled(false);
            exoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "-> onCreateLoader -> " + ExoPlayerAsyncTaskLoader.getLoaderString(id));

        switch (id) {

            case ExoPlayerAsyncTaskLoader.GET_EXOPLAYER:
                return new ExoPlayerAsyncTaskLoader(getContext());

            default:
                throw new UnsupportedOperationException("-> Unknown loader id = " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        switch (loader.getId()) {

            case ExoPlayerAsyncTaskLoader.GET_EXOPLAYER:

                Log.v(LOG_TAG, "-> onLoadFinished -> " + ExoPlayerAsyncTaskLoader.getLoaderString(loader.getId()));
                exoPlayer = (SimpleExoPlayer) data;
                initializePlayer();
                if (isDualPane && !hasInstanceStateSaved)
                    playMediaUrl(mediaUrl);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.v(LOG_TAG, "-> onLoaderReset -> " + ExoPlayerAsyncTaskLoader.getLoaderString(loader.getId()));

        switch (loader.getId()) {

            case ExoPlayerAsyncTaskLoader.GET_EXOPLAYER:

                releasePlayer();
                break;
        }
    }

    private void releasePlayer() {
        Log.v(LOG_TAG, "-> releasePlayer");

        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }

    public void hide() {
        Log.v(LOG_TAG, "-> hide");

        if (exoPlayer != null)
            exoPlayer.stop();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            Log.v(LOG_TAG, "-> onPlayerStateChanged -> Play");

            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    exoPlayer.getCurrentPosition(), 1f);

        } else if ((playbackState == Player.STATE_READY)) {
            Log.v(LOG_TAG, "-> onPlayerStateChanged -> Pause");

            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    exoPlayer.getCurrentPosition(), 1f);
        }

        mediaSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    public interface UpdateIndexCallbackInterface {
        public void updateIndex(int index);
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mediaSession, intent);
        }
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            Log.v(LOG_TAG, "-> MySessionCallback -> onPlay");

            if (viewExoPlay.isEnabled() && exoPlayer!=null)
                exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            Log.v(LOG_TAG, "-> MySessionCallback -> onPause");

            if (viewExoPlay.isEnabled() && exoPlayer!=null)
                exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            Log.v(LOG_TAG, "-> MySessionCallback -> onSkipToPrevious");
        }
    }
}
