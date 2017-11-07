package com.example.bakingapp.utilities;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JsonUtils {

    public static String getJsonFromAssets(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("baking.json");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream,
                    "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    public static String[] getRecipeStepsFromJson(String jsonString, String idInJson)
            throws JSONException {
        final String OWM_ID = "id";
        final String OWM_STEPS = "steps";
        final String OWM_SHORT_DESCRIPTION = "shortDescription";

        ArrayList<String> recipeStepsArrayList = new ArrayList<>();
        JSONArray recipesJsonArray = new JSONArray(jsonString);

        for (int i = 0; i < recipesJsonArray.length(); i++) {
            JSONObject recipeJsonObject = recipesJsonArray.getJSONObject(i);
            String id = recipeJsonObject.getString(OWM_ID);
            if (id.equals(idInJson)) {
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

    public static String getStepDescriptionFromJson(String jsonString, String recipeIdInJson,
                                                    String stepIdInJson) throws JSONException {
        final String OWM_ID = "id";
        final String OWM_STEPS = "steps";
        final String OWM_DESCRIPTION = "description";

        JSONArray recipesJsonArray = new JSONArray(jsonString);

        for (int i = 0; i < recipesJsonArray.length(); i++) {
            JSONObject recipeJsonObject = recipesJsonArray.getJSONObject(i);
            String objectRecipeId = recipeJsonObject.getString(OWM_ID);
            if (objectRecipeId.equals(recipeIdInJson)) {
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

    public static String getVideoUrlFromJson(String jsonString, String recipeIdInJson, String stepIdInJson)
            throws JSONException {
        final String OWM_ID = "id";
        final String OWM_STEPS = "steps";
        final String OWM_VIDEO_URL = "videoURL";

        JSONArray recipesJsonArray = new JSONArray(jsonString);

        for (int i = 0; i < recipesJsonArray.length(); i++) {
            JSONObject recipeJsonObject = recipesJsonArray.getJSONObject(i);
            String objectRecipeId = recipeJsonObject.getString(OWM_ID);
            if (objectRecipeId.equals(recipeIdInJson)) {
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

    public static String getThumbnailUrlFromJson(String jsonString, String recipeIdInJson, String stepIdInJson)
            throws JSONException {
        final String OWM_ID = "id";
        final String OWM_STEPS = "steps";
        final String OWM_THUMBNAIL_URL = "thumbnailURL";

        JSONArray recipesJsonArray = new JSONArray(jsonString);

        for (int i = 0; i < recipesJsonArray.length(); i++) {
            JSONObject recipeJsonObject = recipesJsonArray.getJSONObject(i);
            String objectRecipeId = recipeJsonObject.getString(OWM_ID);
            if (objectRecipeId.equals(recipeIdInJson)) {
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

    public static String[] getIngredientsFromJson(String idInJson, String jsonString) throws JSONException {
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
            if (idInJson.equals(id)) {
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
            if (id.equals(recipeId)){
                return recipeJsonObject.getString(OWM_NAME);
            }
        }
        return null;
    }
}
