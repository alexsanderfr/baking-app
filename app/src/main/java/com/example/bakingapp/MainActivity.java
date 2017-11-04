package com.example.bakingapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.bakingapp.databinding.ActivityMainBinding;
import com.example.bakingapp.utilities.JsonUtils;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipesAdapterOnClickHandler {

    ActivityMainBinding binding;
    RecyclerView.LayoutManager layoutManager;
    RecipesAdapter recipesAdapter;
    String mJson = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.recipesRv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        binding.recipesRv.setLayoutManager(layoutManager);

        recipesAdapter = new RecipesAdapter(null, MainActivity.this);
        binding.recipesRv.setAdapter(recipesAdapter);

        if (savedInstanceState != null) {
            String json = savedInstanceState.getString("json");
            if (json != null) {
                updateRecyclerView(json);
            } else {
                getJsonFromUrl(JsonUtils.url);
            }
        } else {
            getJsonFromUrl(JsonUtils.url);
        }

    }

    public void updateRecyclerView(String json) {
        try {
            String[] data = JsonUtils.getRecipeNamesFromJson(json);
            recipesAdapter.swapRecipes(data);
            recipesAdapter.notifyDataSetChanged();
            binding.recipesPb.setVisibility(View.GONE);
            binding.recipesRv.setVisibility(View.VISIBLE);
            mJson = json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getJsonFromUrl(String url) {
        Ion.with(this).load(url).asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if (result != null) {
                    updateRecyclerView(result);
                } else {
                    Timber.d("No connection");
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mJson != null) {
            outState.putString("json", mJson);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(String idInJson) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, idInJson);
        intent.putExtra("json", mJson);
        startActivity(intent);
    }
}
