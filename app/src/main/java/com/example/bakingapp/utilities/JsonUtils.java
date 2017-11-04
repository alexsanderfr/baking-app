package com.example.bakingapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;

import com.example.bakingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JsonUtils {

    public final static String url =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

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

    public static void writeJsonToSharedPrefs(String json, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("json", json);
        editor.commit();
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
}
