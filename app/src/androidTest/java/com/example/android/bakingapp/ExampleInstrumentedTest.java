package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.android.bakingapp.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    public static final String LOG_TAG = ExampleInstrumentedTest.class.getSimpleName();

    @Rule
    public ActivityTestRule<DetailsActivity> activityTestRule =
            new ActivityTestRule<>(DetailsActivity.class, false, false);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.android.bakingapp", appContext.getPackageName());
    }

    @Test
    public void testIfDetailsFragmentHidden() {

        Gson gson = new Gson();
        ArrayList<Recipe> recipeArrayList = null;
        InputStream inputStream;

        try {
            inputStream =
                    InstrumentationRegistry.getContext().getAssets().open("expectedResponse.json");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Type type = new TypeToken<ArrayList<Recipe>>() {
        }.getType();
        recipeArrayList = gson.fromJson(new InputStreamReader(inputStream), type);

        Intent intent = new Intent();
        intent.putExtra("recipe", recipeArrayList.get(0));
        activityTestRule.launchActivity(intent);

        boolean isDualPane;

        try {

            onView(withId(R.id.guideline)).check(matches(isEnabled()));
            isDualPane = true;

        } catch (NoMatchingViewException e) {

            isDualPane = false;
        }

        Log.i(LOG_TAG, "-> testIfDetailsFragmentHidden -> isDualPane = " + isDualPane);

        if (!isDualPane) {
            onView(withId(R.id.fragment_recipe_step_details)).check(matches(not(isDisplayed())));
            Log.i(LOG_TAG, "-> testIfDetailsFragmentHidden -> fragment_recipe_step_details hidden on mobile UI");
        }
    }
}
