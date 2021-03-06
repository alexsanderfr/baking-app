package com.example.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.bakingapp.BuildConfig;
import com.example.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.bakingapp.R;
import com.example.bakingapp.adapter.RecipesAdapter;
import com.example.bakingapp.databinding.ActivityMainBinding;
import com.example.bakingapp.utilities.JsonUtils;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipesAdapterOnClickHandler {

    private ActivityMainBinding binding;
    RecipesAdapter recipesAdapter;
    String[] data = null;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

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
        if (savedInstanceState != null) {
            data = savedInstanceState.getStringArray("data");
        }

        recipesAdapter = new RecipesAdapter(data, MainActivity.this);
        binding.recipesRv.setAdapter(recipesAdapter);

        getIdlingResource();
        if (data == null) {
            getJsonFromUrl(JsonUtils.url);
        } else {
            updateUi();
        }
    }

    public void getJsonFromUrl(String url) {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        final boolean isOnline = netInfo != null && netInfo.isConnectedOrConnecting();
        if (isOnline) {
            if (mIdlingResource != null) {
                mIdlingResource.setIdleState(false);
            }
            Ion.with(this).load(url).asString().setCallback(new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String result) {
                    if (result != null) {
                        SharedPreferences sharedPref = PreferenceManager
                                .getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("json", result);
                        editor.apply();
                        updateUi();
                        if (mIdlingResource != null) {
                            mIdlingResource.setIdleState(true);
                        }
                    }
                }
            });
        } else {
            binding.recipesPb.setVisibility(View.GONE);
            View view = findViewById(android.R.id.content);
            Snackbar.make(view, getString(R.string.connectivity_error),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    private void updateUi() {
        binding.recipesPb.setVisibility(View.GONE);
        binding.recipesRv.setVisibility(View.VISIBLE);

        String json = JsonUtils.getJsonFromSharedPref(this);

        try {
            data = JsonUtils.getRecipeNamesFromJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recipesAdapter.swapData(data);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (data != null) outState.putStringArray("data", data);
    }

    @Override
    public void onClick(String idInJson) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("recipeId", idInJson);
        String json = JsonUtils.getJsonFromSharedPref(this);
        try {
            String image = JsonUtils.getImageWithIdFromJson(idInJson, json);
            intent.putExtra("image", image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }
}