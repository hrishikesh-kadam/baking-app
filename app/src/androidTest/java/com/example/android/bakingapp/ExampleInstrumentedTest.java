package com.example.android.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.JsonReader;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.rest.RetrofitSingleton;
import com.example.android.bakingapp.rest.WebServices;
import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.android.bakingapp", appContext.getPackageName());
    }

    @Test
    public void checkDownloadedJson() throws Exception {

        WebServices webServices = RetrofitSingleton.getRetrofit().create(WebServices.class);
        Call<ArrayList<Recipe>> recipeCall = webServices.getAllRecipes();
        Response<ArrayList<Recipe>> recipeResponse = null;

        try {
            recipeResponse = recipeCall.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader("E:\\Study Material\\Udacity\\Android Developer Nanodegree\\Projects\\BakingApp\\app\\src\\test\\java\\com\\example\\android\\bakingapp\\expectedResponse.json"));
        System.out.println(reader.toString());

        //System.out.println(recipeResponse.body());
    }
}
