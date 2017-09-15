package com.foo.umbrella.ui;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foo.umbrella.R;
import com.foo.umbrella.UmbrellaApp;
import com.foo.umbrella.data.ApiServicesProvider;
import com.foo.umbrella.data.ForecastConditionAdapter;
import com.foo.umbrella.data.model.ForecastCondition;
import com.foo.umbrella.data.model.WeatherData;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.squareup.moshi.JsonAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.color.holo_blue_dark;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

  public static final String TAG = MainActivity.class.getSimpleName() + "_TAG";
  private ApiServicesProvider provider;
  private GridAdapter forecastAdapter;

  List<ForecastCondition> fullForecastList;
  List<ForecastCondition> todayForecastList;
  RecyclerView todayForecast;
  RecyclerView tomorrowForecast;
  TextView tvTemp;
  TextView tvWeather;
  TextView tvLocation;
  EditText etZip;
  Button btnZip;
  RelativeLayout rlBanner;
  TextView tvForeOne;
  TextView tvForeOneTemp;
  ImageView ivForeOne;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    provider = new ApiServicesProvider(UmbrellaApp.getApplication());
    tvTemp = (TextView) findViewById(R.id.tv_temp);
    tvWeather = (TextView) findViewById(R.id.tv_weather);
    tvLocation = (TextView) findViewById(R.id.tv_location);
    rlBanner = (RelativeLayout) findViewById(R.id.tempBanner);
    etZip = (EditText) findViewById(R.id.et_enter_zip);
    btnZip = (Button) findViewById(R.id.btn_get_weather);
    btnZip.setOnClickListener(this);

    todayForecast = (RecyclerView) findViewById(R.id.rv_today);
    setUpRecyclerView();
    /*todayForecastList = new ArrayList<>(0);
    FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this, FlexDirection.ROW);
    layoutManager.setFlexWrap(FlexWrap.WRAP);
    layoutManager.setFlexDirection(FlexDirection.ROW);
    layoutManager.setAlignItems(AlignItems.STRETCH);
    todayForecast.setLayoutManager(layoutManager);
    forecastAdapter = new GridAdapter(todayForecastList);
    todayForecast.setAdapter(forecastAdapter);*/

    getWeather("30011");

  }

  private void setUpRecyclerView() {
    todayForecastList = new ArrayList<>(0);
    forecastAdapter = new GridAdapter(todayForecastList);
    todayForecast.setAdapter(forecastAdapter);
    todayForecast.setLayoutManager(new LinearLayoutManager(this));
  }

  private void initViews(WeatherData wd) {
    String temperatureF = wd.getCurrentObservation().getTempFahrenheit();
    String temperatureC = wd.getCurrentObservation().getTempCelsius();
    String weather = wd.getCurrentObservation().getWeatherDescription();
    String location = wd.getCurrentObservation().getDisplayLocation().getFullName();
    tvTemp.setText(temperatureF + "\u00B0");
    tvWeather.setText(weather);
    tvLocation.setText(location);

    if(Double.parseDouble(temperatureF) < 60 || Double.parseDouble(temperatureC) < 15.5)
      rlBanner.setBackgroundColor(getResources().getColor(R.color.weather_cool));
    else
      rlBanner.setBackgroundColor(getResources().getColor(R.color.weather_warm));

    fullForecastList = wd.getForecast();
    for(int i = 0; i < 1; i++) {
      todayForecastList.add(fullForecastList.get(i));
    }
    forecastAdapter.updateDataSet(todayForecastList);
    /*ForecastCondition forecast = forecastList.get(0);
      String time = forecast.getDisplayTime();
      String condition = forecast.getCondition();
      String tempeF = forecast.getTempFahrenheit();
      String tempC = forecast.getTempCelsius();
      tvForeOne.setText(time);
      tvForeOneTemp.setText(tempC + "\u00B0");
      switch (condition) {
        case "Chance of Rain":
          ivForeOne.setImageResource(R.drawable.weather_rainy);
          break;
        case "Rain":
          ivForeOne.setImageResource(R.drawable.weather_cloudy);
          break;
        case "Overcast":
          ivForeOne.setImageResource(R.drawable.weather_cloudy);
          break;
        case "Cloudy":
          ivForeOne.setImageResource(R.drawable.weather_cloudy);
          break;
      }*/
    //}


  }

  private void getWeather(String zipCode) {
    final WeatherData[] responseWD = new WeatherData[1];
    provider.getWeatherService().forecastForZipCallable(zipCode).enqueue(new Callback<WeatherData>() {
      @Override
      public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
        if(response.isSuccessful()) {
          responseWD[0] = response.body();
          initViews(responseWD[0]);
        } else{
          Toast.makeText(MainActivity.this, "API Error: " + response.code(), Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(Call<WeatherData> call, Throwable t) {
        Log.d(TAG, "onFailure: " + "Failed bro");
        Toast.makeText(MainActivity.this, "Failed bro", Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_get_weather:
        getWeather(etZip.getText().toString());
        Toast.makeText(MainActivity.this, "Getting Weather for " + etZip.getText().toString(), Toast.LENGTH_SHORT).show();
      break;
    }
  }
}
