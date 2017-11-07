package com.example.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;

import com.example.bakingapp.R;
import com.example.bakingapp.utilities.JsonUtils;

import org.json.JSONException;

public class IngredientAppWidgetProvider extends AppWidgetProvider{

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, IngredientWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            String json = JsonUtils.getJsonFromSharedPref(context);
            if (json!= null) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                String recipeId = sharedPreferences.getString("recipeId",
                        context.getString(R.string.default_recipe_id));
                String recipeName = null;
                try {
                    String[] recipeIngredients = JsonUtils.getIngredientsFromJson(recipeId, json);
                    recipeName = JsonUtils.getRecipeNameWithIdFromJson(recipeId, json);
                    if (recipeName == null)
                        recipeName = context.getString(R.string.default_recipe_name);
                    intent.putExtra("ingredients", recipeIngredients);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);
                views.setTextViewText(R.id.recipe_name_tv_widget, "Last visited recipe: " + recipeName);
                views.setViewVisibility(R.id.recipe_name_tv_widget, View.VISIBLE);
                views.setViewVisibility(R.id.widget_list_view, View.VISIBLE);
                views.setViewVisibility(R.id.error_tv_widget, View.GONE);

                views.setRemoteAdapter(appWidgetId, R.id.widget_list_view, intent);

                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    }
}
