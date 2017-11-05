package com.example.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.bakingapp.R;
import com.example.bakingapp.utilities.JsonUtils;

import org.json.JSONException;

public class IngredientWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        String json = JsonUtils.getJsonFromAssets(this.getApplicationContext());
        String[] data = null;
        try {
            data = JsonUtils.getIngredientsFromJson(getString(R.string.desired_recipe_id), json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new IngredientRemoteViewsFactory(this.getApplicationContext(), intent, data);
    }
}