package com.example.bakingapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bakingapp.databinding.FragmentStepDetailBinding;
import com.example.bakingapp.utilities.JsonUtils;

import org.json.JSONException;

public class StepDetailFragment extends Fragment {

    FragmentStepDetailBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container,
                false);

        String recipeId = getArguments().getString("recipeId");
        String stepId = getArguments().getString("stepId");
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
            Toast.makeText(getActivity(), "Thumbnail: " + videoUrl, Toast.LENGTH_SHORT).show();
        } else if (videoUrl != null && !videoUrl.equals("")) {
            Toast.makeText(getActivity(), "Video: " + videoUrl, Toast.LENGTH_SHORT).show();
        }

        binding.stepInstructionTv.setText(stepDescription);
        return binding.getRoot();
    }
}
