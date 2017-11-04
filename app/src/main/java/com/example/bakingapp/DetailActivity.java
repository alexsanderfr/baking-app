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
    boolean isDualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        Intent intent = getIntent();
        FragmentManager fragmentManager = getSupportFragmentManager();

        isDualPane = binding.dividerView != null && binding.dividerView.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            if (isDualPane) {
                Fragment fragmentLeft = fragmentManager.getFragment(savedInstanceState, "leftFragment");
                Fragment fragmentRight = fragmentManager.getFragment(savedInstanceState, "rightFragment");
                if (fragmentLeft != null) fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_left, fragmentLeft).commit();
                if (fragmentRight != null) fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_right, fragmentRight).commit();

            } else {
                Fragment fragment = fragmentManager.getFragment(savedInstanceState, "fragment");
                if (fragment != null) fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_detail, fragment).commit();
            }

        } else {
            RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
            recipeStepsFragment.setArguments(intent.getExtras());
            if (isDualPane) {
                fragmentManager.beginTransaction().add(R.id.fragment_container_left, recipeStepsFragment)
                        .commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_container_detail, recipeStepsFragment)
                        .commit();
            }
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
        bundle.putString("json", intent.getStringExtra("json"));

        stepDetailFragment.setArguments(bundle);
        if (isDualPane) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container_right, stepDetailFragment)
                    .addToBackStack(null).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.fragment_container_detail, stepDetailFragment)
                    .addToBackStack(null).commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager manager = getSupportFragmentManager();
        if (isDualPane) {
            manager.putFragment(outState, "leftFragment",
                    manager.findFragmentById(R.id.fragment_container_left));
            manager.putFragment(outState, "rightFragment",
                    manager.findFragmentById(R.id.fragment_container_right));
        } else {
            manager.putFragment(outState, "fragment",
                    manager.findFragmentById(R.id.fragment_container_detail));
        }
    }


    public void onClickPreviousButton(View view) {
    }

    public void onClickNextButton(View view) {
    }
}
