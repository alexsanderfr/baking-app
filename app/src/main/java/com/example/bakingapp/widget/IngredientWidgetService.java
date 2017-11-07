package com.example.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class IngredientWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        String[] recipeIngredients = intent.getStringArrayExtra("ingredients");
        return new IngredientRemoteViewsFactory(this.getApplicationContext(), intent, recipeIngredients);
    }
}