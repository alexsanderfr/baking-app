package com.example.bakingapp;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.example.bakingapp.databinding.ActivityDetailBinding;

public class DetailActivity extends FragmentActivity implements RecipeStepsAdapter.RecipeStepsAdapterOnClickHandler {

    ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        Intent intent = getIntent();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            Fragment fragment = fragmentManager.getFragment(savedInstanceState, "fragment");
            fragmentManager.beginTransaction().replace(R.id.recipe_steps_fragment_container, fragment)
                    .commit();
        } else {
            RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
            recipeStepsFragment.setArguments(intent.getExtras());
            fragmentManager.beginTransaction().add(R.id.recipe_steps_fragment_container, recipeStepsFragment)
                    .commit();
        }
    }

    @Override
    public void onClick(String stepId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        StepDetailFragment stepDetailFragment = new StepDetailFragment();

        Intent intent = getIntent();
        String recipeId = intent.getStringExtra(Intent.EXTRA_TEXT);

        Bundle bundle = new Bundle();
        bundle.putString("stepId", stepId);
        bundle.putString("recipeId", recipeId);

        stepDetailFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.recipe_steps_fragment_container, stepDetailFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "fragment",
                getSupportFragmentManager().findFragmentById(R.id.recipe_steps_fragment_container));
    }


    public void onClickPreviousButton(View view) {
    }

    public void onClickNextButton(View view) {
    }
}
