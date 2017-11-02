package com.example.bakingapp;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakingapp.databinding.FragmentStepDetailBinding;
import com.example.bakingapp.utilities.JsonUtils;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONException;

import timber.log.Timber;

public class StepDetailFragment extends Fragment {

    FragmentStepDetailBinding binding;
    private SimpleExoPlayer exoPlayer;
    private String mediaUrl;
    private long playbackPosition = 0;
    private int currentWindow = 0;
    String recipeId;
    String stepId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container,
                false);

        recipeId = getArguments().getString("recipeId");
        stepId = getArguments().getString("stepId");
        String stepDescription = null;
        String videoUrl = null;
        String thumbnailUrl = null;

        try {
            String jsonString = JsonUtils.getJsonFromAssets(getActivity());
            stepDescription = JsonUtils.getStepDescriptionFromJson(jsonString, recipeId, stepId);
            videoUrl = JsonUtils.getVideoUrlFromJson(jsonString, recipeId, stepId);
            thumbnailUrl = JsonUtils.getThumbnailUrlFromJson(jsonString, recipeId, stepId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (thumbnailUrl != null && !thumbnailUrl.equals("")) {
            mediaUrl = thumbnailUrl;
        } else if (videoUrl != null && !videoUrl.equals("")) {
            mediaUrl = videoUrl;
        }

        Timber.d("URL: " + mediaUrl);
        initializePlayer();

        binding.stepInstructionTv.setText(stepDescription);

        return binding.getRoot();
    }



    private void initializePlayer() {
        if ((Util.SDK_INT <= 23 || exoPlayer == null) && mediaUrl != null && !mediaUrl.equals("")) {
            binding.playerView.setVisibility(View.VISIBLE);
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory
                    (bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            RenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity());
            exoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);
            binding.playerView.setPlayer(exoPlayer);

            Uri mediaUri = Uri.parse(mediaUrl);
            MediaSource mediaSource = buildMediaSource(mediaUri);
            exoPlayer.prepare(mediaSource, true, false);

            exoPlayer.setPlayWhenReady(true);
            exoPlayer.seekTo(currentWindow, playbackPosition);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
        return new ExtractorMediaSource(uri, new DefaultHttpDataSourceFactory(userAgent),
                new DefaultExtractorsFactory(), null, null);
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            playbackPosition = exoPlayer.getCurrentPosition();
            currentWindow = exoPlayer.getCurrentWindowIndex();
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();

    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
