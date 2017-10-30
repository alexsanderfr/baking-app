package com.example.bakingapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.bakingapp.databinding.ActivityMainBinding;
import com.example.bakingapp.utilities.JsonUtils;

import org.json.JSONException;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipesAdapterOnClickHandler{

    ActivityMainBinding binding;
    RecyclerView.LayoutManager layoutManager;
    RecipesAdapter recipesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.recipesRv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        binding.recipesRv.setLayoutManager(layoutManager);

        String[] data = null;
        try {
            String jsonString = JsonUtils.getJsonFromAssets(this);
            data = JsonUtils.getRecipeNamesFromJson(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recipesAdapter = new RecipesAdapter(data, this);
        binding.recipesRv.setAdapter(recipesAdapter);
    }

    @Override
    public void onClick(String idInJson) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, idInJson);
        startActivity(intent);
    }
}
