package com.example.bakingapp.ui;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bakingapp.R;
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

public class StepDetailFragment extends Fragment {

    private FragmentStepDetailBinding binding;
    private SimpleExoPlayer exoPlayer;
    private String mediaUrl;
    private long playbackPosition;
    private int currentWindow;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container,
                false);

        boolean isDualPane = getActivity().getResources().getBoolean(R.bool.isTablet);
        boolean isLand = getActivity().getResources().getBoolean(R.bool.isLand);

        String recipeId;
        String stepId;
        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getString("recipeId");
            stepId = savedInstanceState.getString("stepId");
            playbackPosition = savedInstanceState.getLong("position");
            currentWindow = savedInstanceState.getInt("window");
        } else {
            recipeId = getArguments().getString("recipeId");
            stepId = getArguments().getString("stepId");
            playbackPosition = 0;
            currentWindow = 0;

        }

        String stepDescription = null;
        String thumbnailUrl = null;
        String videoUrl = null;
        try {
            String jsonString = JsonUtils.getJsonFromSharedPref(getActivity());
            if (jsonString != null) {
                stepDescription = JsonUtils.getStepDescriptionFromJson(jsonString, recipeId, stepId);
                videoUrl = JsonUtils.getVideoUrlFromJson(jsonString, recipeId, stepId);
                thumbnailUrl = JsonUtils.getThumbnailUrlFromJson(jsonString, recipeId, stepId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(thumbnailUrl)) {
            if (!(isLand && !isDualPane)) {
                binding.thumbnailCardView.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(thumbnailUrl).into(binding.thumbnailView);
            }
            mediaUrl = thumbnailUrl;
        } else if (!TextUtils.isEmpty(videoUrl)) {
            mediaUrl = videoUrl;
        }

        if (!(isLand && !isDualPane)) {
            binding.stepInstructionTv.setText(stepDescription);
        }
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
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
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("recipeId", getArguments().getString("recipeId"));
        outState.putString("stepId", getArguments().getString("stepId"));
        if (exoPlayer != null) {
            outState.putLong("position", exoPlayer.getCurrentPosition());
            outState.putInt("window", exoPlayer.getCurrentWindowIndex());
        }
    }
}
