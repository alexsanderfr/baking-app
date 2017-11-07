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
import com.example.bakingapp.adapter.IngredientsAdapter;
import com.example.bakingapp.databinding.FragmentIngredientsBinding;
import com.example.bakingapp.utilities.JsonUtils;

import org.json.JSONException;


public class IngredientsFragment extends Fragment {

    FragmentIngredientsBinding binding;

    public IngredientsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredients, container,
                false);
        String recipeId = getArguments().getString("recipeId");
        binding.ingredientsRv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.ingredientsRv.setLayoutManager(layoutManager);
        String[] data = null;
        try {
            String json = JsonUtils.getJsonFromSharedPref(getActivity());
            if(json!=null) {
                data = JsonUtils.getIngredientsFromJson(recipeId, json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(data);
        binding.ingredientsRv.setAdapter(ingredientsAdapter);
        return binding.getRoot();
    }
}