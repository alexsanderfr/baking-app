package com.example.bakingapp.ui;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakingapp.R;
import com.example.bakingapp.adapter.RecipeStepsAdapter;
import com.example.bakingapp.databinding.FragmentRecipeStepsBinding;
import com.example.bakingapp.utilities.JsonUtils;

import org.json.JSONException;

import timber.log.Timber;

public class RecipeStepsFragment extends Fragment{

    private FragmentRecipeStepsBinding binding;

    public RecipeStepsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_steps,
                container, false);
        String idInJson = getArguments().getString("recipeId");

        binding.recipeStepsRv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recipeStepsRv.setLayoutManager(layoutManager);
        String[] data = null;

        try {
            String jsonString = JsonUtils.getJsonFromSharedPref(getActivity());
            Timber.d(jsonString);
            if (jsonString != null) {
                data = JsonUtils.getRecipeStepsFromJson(jsonString, idInJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter(data,
                (RecipeStepsAdapter.RecipeStepsAdapterOnClickHandler) getActivity());
        binding.recipeStepsRv.setAdapter(recipeStepsAdapter);
        return binding.getRoot();
    }
}
