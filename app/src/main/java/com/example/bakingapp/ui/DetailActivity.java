package com.example.bakingapp.ui;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.bakingapp.R;
import com.example.bakingapp.adapter.RecipeStepsAdapter;
import com.example.bakingapp.databinding.ActivityDetailBinding;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailActivity extends AppCompatActivity
        implements RecipeStepsAdapter.RecipeStepsAdapterOnClickHandler {

    private ActivityDetailBinding binding;
    private boolean isDualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent intent = getIntent();
        String recipeId = intent.getStringExtra("recipeId");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("recipeId", recipeId);
        editor.apply();

        FragmentManager fragmentManager = getSupportFragmentManager();
        isDualPane = getResources().getBoolean(R.bool.isTablet);

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
                fragmentManager.beginTransaction().add(R.id.fragment_container_left,
                        recipeStepsFragment).commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_container_detail,
                        recipeStepsFragment).commit();
            }
        }
    }

    @Override
    public void onClick(String stepId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        StepDetailFragment stepDetailFragment = new StepDetailFragment();

        Intent intent = getIntent();
        String recipeId = intent.getStringExtra("recipeId");

        Bundle bundle = new Bundle();
        bundle.putString("stepId", stepId);
        bundle.putString("recipeId", recipeId);

        stepDetailFragment.setArguments(bundle);
        if (isDualPane) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container_right,
                    stepDetailFragment).addToBackStack(null).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.fragment_container_detail,
                    stepDetailFragment).addToBackStack(null).commit();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void onClickIngredientsButton(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        IngredientsFragment ingredientsFragment = new IngredientsFragment();

        Intent intent = getIntent();
        String recipeId = intent.getStringExtra("recipeId");

        Bundle bundle = new Bundle();
        bundle.putString("recipeId", recipeId);

        ingredientsFragment.setArguments(bundle);
        if (isDualPane) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container_right,
                    ingredientsFragment).addToBackStack(null).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.fragment_container_detail,
                    ingredientsFragment).addToBackStack(null).commit();
        }
    }
}
