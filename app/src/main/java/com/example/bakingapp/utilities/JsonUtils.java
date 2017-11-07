package com.example.bakingapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    public final static String url =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static String getJsonFromSharedPref(Context context) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPref.getString("json", null);
    }

    public static String[] getRecipeNamesFromJson(String jsonString) throws JSONException {
        final String OWM_NAME = "name";

        ArrayList<String> recipeNamesArrayList = new ArrayList<>();
        JSONArray recipesJsonArray = new JSONArray(jsonString);

        for (int i = 0; i < recipesJsonArray.length(); i++) {
            JSONObject recipeJsonObject = recipesJsonArray.getJSONObject(i);
            String name = recipeJsonObject.getString(OWM_NAME);
            recipeNamesArrayList.add(name);
        }

        return recipeNamesArrayList.toArray(new String[0]);
    }

    public static String[] getRecipeStepsFromJson(String jsonString, String recipeId)
            throws JSONException {
        final String OWM_ID = "id";
        final String OWM_STEPS = "steps";
        final String OWM_SHORT_DESCRIPTION = "shortDescription";

        ArrayList<String> recipeStepsArrayList = new ArrayList<>();
        JSONArray recipesJsonArray = new JSONArray(jsonString);

        for (int i = 0; i < recipesJsonArray.length(); i++) {
            JSONObject recipeJsonObject = recipesJsonArray.getJSONObject(i);
            String id = recipeJsonObject.getString(OWM_ID);
            if (id.equals(recipeId)) {
                JSONArray stepsJsonArray = recipeJsonObject.getJSONArray(OWM_STEPS);
                for (int j = 0; j < stepsJsonArray.length(); j++) {
                    JSONObject stepJsonObject = stepsJsonArray.getJSONObject(j);
                    String step = stepJsonObject.getString(OWM_SHORT_DESCRIPTION);
                    recipeStepsArrayList.add(step);
                }
                break;
            }
        }

        return recipeStepsArrayList.toArray(new String[0]);
    }

    public static String getStepDescriptionFromJson(String jsonString, String recipeId,
                                                    String stepIdInJson) throws JSONException {
        final String OWM_ID = "id";
        final String OWM_STEPS = "steps";
        final String OWM_DESCRIPTION = "description";

        JSONArray recipesJsonArray = new JSONArray(jsonString);

        for (int i = 0; i < recipesJsonArray.length(); i++) {
            JSONObject recipeJsonObject = recipesJsonArray.getJSONObject(i);
            String objectRecipeId = recipeJsonObject.getString(OWM_ID);
            if (objectRecipeId.equals(recipeId)) {
                JSONArray stepsJsonArray = recipeJsonObject.getJSONArray(OWM_STEPS);
                for (int j = 0; j < stepsJsonArray.length(); j++) {
                    JSONObject stepJsonObject = stepsJsonArray.getJSONObject(j);
                    String objectStepId = stepJsonObject.getString(OWM_ID);
                    if (objectStepId.equals(stepIdInJson)) {
                        return stepJsonObject.getString(OWM_DESCRIPTION);
                    }
                }
            }
        }
        return null;
    }

    public static String getVideoUrlFromJson(String jsonString, String recipeId, String stepIdInJson)
            throws JSONException {
        final String OWM_ID = "id";
        final String OWM_STEPS = "steps";
        final String OWM_VIDEO_URL = "videoURL";

        JSONArray recipesJsonArray = new JSONArray(jsonString);

        for (int i = 0; i < recipesJsonArray.length(); i++) {
            JSONObject recipeJsonObject = recipesJsonArray.getJSONObject(i);
            String objectRecipeId = recipeJsonObject.getString(OWM_ID);
            if (objectRecipeId.equals(recipeId)) {
                JSONArray stepsJsonArray = recipeJsonObject.getJSONArray(OWM_STEPS);
                for (int j = 0; j < stepsJsonArray.length(); j++) {
                    JSONObject stepJsonObject = stepsJsonArray.getJSONObject(j);
                    String objectStepId = stepJsonObject.getString(OWM_ID);
                    if (objectStepId.equals(stepIdInJson)) {
                        return stepJsonObject.getString(OWM_VIDEO_URL);
                    }
                }
            }
        }
        return null;
    }

    public static String getThumbnailUrlFromJson(String jsonString, String recipeId, String stepIdInJson)
            throws JSONException {
        final String OWM_ID = "id";
        final String OWM_STEPS = "steps";
        final String OWM_THUMBNAIL_URL = "thumbnailURL";

        JSONArray recipesJsonArray = new JSONArray(jsonString);

        for (int i = 0; i < recipesJsonArray.length(); i++) {
            JSONObject recipeJsonObject = recipesJsonArray.getJSONObject(i);
            String objectRecipeId = recipeJsonObject.getString(OWM_ID);
            if (objectRecipeId.equals(recipeId)) {
                JSONArray stepsJsonArray = recipeJsonObject.getJSONArray(OWM_STEPS);
                for (int j = 0; j < stepsJsonArray.length(); j++) {
                    JSONObject stepJsonObject = stepsJsonArray.getJSONObject(j);
                    String objectStepId = stepJsonObject.getString(OWM_ID);
                    if (objectStepId.equals(stepIdInJson)) {
                        return stepJsonObject.getString(OWM_THUMBNAIL_URL);
                    }
                }
            }
        }
        return null;
    }

    public static String[] getIngredientsFromJson(String recipeId, String jsonString) throws JSONException {
        final String OWM_ID = "id";
        final String OWM_INGREDIENTS = "ingredients";
        final String OWM_INGREDIENT = "ingredient";
        final String OWM_QUANTITY = "quantity";
        final String OWM_MEASURE = "measure";

        JSONArray recipesJsonArray = new JSONArray(jsonString);
        ArrayList<String> ingredientsArrayList = new ArrayList<>();

        for (int i = 0; i < recipesJsonArray.length(); i++) {
            JSONObject recipeJsonObject = recipesJsonArray.getJSONObject(i);
            String id = recipeJsonObject.getString(OWM_ID);
            if (recipeId.equals(id)) {
                JSONArray ingredientsJsonArray = recipeJsonObject.getJSONArray(OWM_INGREDIENTS);
                for (int j = 0; j < ingredientsJsonArray.length(); j++) {
                    JSONObject ingredientJsonObject = ingredientsJsonArray.getJSONObject(j);
                    String quantity = ingredientJsonObject.getString(OWM_QUANTITY);
                    String measure = ingredientJsonObject.getString(OWM_MEASURE);
                    String ingredient = ingredientJsonObject.getString(OWM_INGREDIENT);
                    String formattedIngredient = String.format("%s %s, %s", quantity, measure, ingredient);
                    ingredientsArrayList.add(formattedIngredient);
                }
            }
        }

        return ingredientsArrayList.toArray(new String[0]);
    }

    public static String getRecipeNameWithIdFromJson(String recipeId, String jsonString)
            throws JSONException {
        final String OWM_NAME = "name";
        final String OWM_ID = "id";

        JSONArray recipesJsonArray = new JSONArray(jsonString);

        for (int i = 0; i < recipesJsonArray.length(); i++) {
            JSONObject recipeJsonObject = recipesJsonArray.getJSONObject(i);
            String id = recipeJsonObject.getString(OWM_ID);
            if (id.equals(recipeId)) {
                return recipeJsonObject.getString(OWM_NAME);
            }
        }
        return null;
    }
}
