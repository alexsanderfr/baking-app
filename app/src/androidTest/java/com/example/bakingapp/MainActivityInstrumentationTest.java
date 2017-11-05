package com.example.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.bakingapp.adapter.RecipeStepsAdapter;
import com.example.bakingapp.ui.MainActivity;

import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentationTest {

    public static BoundedMatcher<RecyclerView.ViewHolder,
            RecipeStepsAdapter.ViewHolder> withHolderRecipeSteps(final String text) {

        return new BoundedMatcher<RecyclerView.ViewHolder,
                RecipeStepsAdapter.ViewHolder>(RecipeStepsAdapter.ViewHolder.class) {
            @Override
            protected boolean matchesSafely(RecipeStepsAdapter.ViewHolder item) {
                TextView nameTextView = item.recipeStepNameTextView;
                return nameTextView.getText().toString().equals(text);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("No ViewHolder found with text equal to: " + text);
            }
        };
    }

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void validateRecipeRecyclerView() {
        onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        String stepName = "Starting prep";
        onView(withId(R.id.recipe_steps_rv)).perform(RecyclerViewActions
                .scrollToHolder(withHolderRecipeSteps(stepName)));
    }
}
