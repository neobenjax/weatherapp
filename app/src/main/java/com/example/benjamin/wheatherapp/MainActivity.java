package com.example.benjamin.wheatherapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.iconWeatherImageView) ImageView iconWeather;
    @BindView(R.id.descriptionTextView) TextView descriptionWeather;
    @BindView(R.id.actualDegreeTextView) TextView actualDegree;
    @BindView(R.id.HighestTempTextView) TextView highestTemp;
    @BindView(R.id.LowestTempTextView) TextView lowestTemp;
    @BindDrawable(R.drawable.cloudy) Drawable cloudyIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.darksky.net/forecast/79af0812528559b80b8c5050463fa78f/37.8267,-122.4233";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            CurrentWeather currentWeather = getCurrentWeatherFromJson(response);
                            iconWeather.setImageDrawable(currentWeather.getIconDrawableResouce());
                            descriptionWeather.setText(currentWeather.getWeatherDescription());
                            actualDegree.setText(currentWeather.getCurrentTemp());
                            highestTemp.setText(currentWeather.getHighestTemp());
                            lowestTemp.setText(currentWeather.getLowestTemp());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @OnClick(R.id.dailyTextView)
    public void dailyWeatherClick(){
        Intent dailyActivityIntent = new Intent(MainActivity.this, DailyWeatherActivity.class);
        startActivity(dailyActivityIntent);
    }

    @OnClick(R.id.hourlyTextView)
    public void hourlyWeatherClick(){
        Intent dailyActivityIntent = new Intent(MainActivity.this, HourlyWeatherForecast.class);
        startActivity(dailyActivityIntent);
    }

    @OnClick(R.id.minutelyTextView)
    public void minutelyWeatherClick(){
        Intent dailyActivityIntent = new Intent(MainActivity.this, MinutelyWeatherActivity.class);
        startActivity(dailyActivityIntent);
    }

    private CurrentWeather getCurrentWeatherFromJson(String json) throws JSONException{
        JSONObject jsonObject = new JSONObject(json);

        JSONObject jsonWithCurrentWeather = jsonObject.getJSONObject("currently");

        JSONObject jsonWithDailyWeather = jsonObject.getJSONObject("daily");

        JSONArray jsonWithDailyWeatherData = jsonWithDailyWeather.getJSONArray("data");

        JSONObject jsonWithTodayData = jsonWithDailyWeatherData.getJSONObject(0);

        String summary = jsonWithCurrentWeather.getString("summary");
        String icon = jsonWithCurrentWeather.getString("icon");
        String temperature = jsonWithCurrentWeather.getDouble("temperature") + "";


        String maxTemperature = jsonWithTodayData.getDouble("temperatureMax") + "";

        String minTemperature = jsonWithTodayData.getDouble("temperatureMin") + "";

        CurrentWeather currentWeather = new CurrentWeather(MainActivity.this);

        currentWeather.setWeatherDescription(summary);
        currentWeather.setIconImage(icon);
        currentWeather.setCurrentTemp(temperature);
        currentWeather.setHighestTemp(maxTemperature);
        currentWeather.setLowestTemp(minTemperature);

        return currentWeather;
    }
}
