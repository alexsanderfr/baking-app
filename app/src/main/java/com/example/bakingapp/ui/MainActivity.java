package com.example.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.bakingapp.BuildConfig;
import com.example.bakingapp.R;
import com.example.bakingapp.adapter.RecipesAdapter;
import com.example.bakingapp.databinding.ActivityMainBinding;
import com.example.bakingapp.utilities.JsonUtils;

import org.json.JSONException;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipesAdapterOnClickHandler {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.recipesRv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recipesRv.setLayoutManager(layoutManager);

        String json = JsonUtils.getJsonFromAssets(this);

        String[] data = null;
        try {
            data = JsonUtils.getRecipeNamesFromJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RecipesAdapter recipesAdapter = new RecipesAdapter(data, MainActivity.this);
        binding.recipesRv.setAdapter(recipesAdapter);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(String idInJson) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, idInJson);
        startActivity(intent);
    }
}