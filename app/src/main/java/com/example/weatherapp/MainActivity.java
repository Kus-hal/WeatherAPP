package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private ProgressBar progressBar;
    private RelativeLayout rLHome;
    private TextView textCityName, textTemp, textConditions, textWindSpeed, textLastTime, textShowForecast;
    private RecyclerView rvWeather, rvFavs;
    private ImageView imgBG, imgSearch, imgWeather, imgRefresh;
    private TextInputEditText editCityName;
    private ArrayList<WeatherModel> arr;
    private ArrayList<FavCityModel> favArr;
    private WeatherModelAdapter weatherModelAdapter;
    private FavCityAdapter favCityAdapter;
    private int PERMISSION_CODE = 1;
    private String cityName;
    //    double lat, lon;
    double lat = 29.1229234, lon = 79.6868848;
    private LocationManager locationManager;
    private Location location;
    String[] favCity = {
            "New York",
            "Singapore",
            "Mumbai",
            "Delhi",
            "Sydney",
            "Melbourne"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        progressBar = findViewById(R.id.pBarLoading);
        rLHome = findViewById(R.id.RLHome);
        textCityName = findViewById(R.id.textCityName);
        textTemp = findViewById(R.id.textTemp);
        textConditions = findViewById(R.id.textConditions);
        rvWeather = findViewById(R.id.rvWeather);
        rvFavs = findViewById(R.id.rvFavs);
        imgBG = findViewById(R.id.imgBG);
        imgWeather = findViewById(R.id.imgWeather);
        imgSearch = findViewById(R.id.imgSearch);
        imgRefresh = findViewById(R.id.imgRefresh);
        editCityName = findViewById(R.id.editCityName);
        textWindSpeed = findViewById(R.id.textWindSpeed);
        textLastTime = findViewById(R.id.textLastTime);
        textShowForecast = findViewById(R.id.textShowForecast);




        arr = new ArrayList<>();
        favArr = new ArrayList<>();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            Toast.makeText(this,"Fetching Last known location",Toast.LENGTH_SHORT).show();
            lat = location.getLatitude();
            lon = location.getLongitude();

        }
        getCurrentWeather(lat, lon);
        getForecastWeather(lat, lon);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        weatherModelAdapter = new WeatherModelAdapter(this, arr);
        rvWeather.setAdapter(weatherModelAdapter);

        favCityAdapter = new FavCityAdapter(this,favArr);
        rvFavs.setAdapter(favCityAdapter);

        for(int i=0;i<favCity.length;i++){
            getFavCoord(favCity[i]);
        }

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editCityName.getText().toString();
                if (city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter city Name", Toast.LENGTH_SHORT).show();
                }
                textCityName.setText(city.toUpperCase());
                updateWeather(city);
            }
        });

        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textCityName.getText().equals("City Name")){
                    Toast.makeText(MainActivity.this,"Refreshing...",Toast.LENGTH_SHORT).show();
                    getCurrentWeather(lat,lon);
                    getForecastWeather(lat,lon);
                    getFavWeather(lat,lon);
                }
                else {
                    Toast.makeText(MainActivity.this,"Network Connection Required",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateWeather(String city) {
        String apiKey = "fc853b4d52b5b246574b3f6ff1f63387";
        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                +city
                +"&appid="
                +apiKey;
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    textLastTime.setText(getCurrentTime());
                    lon = response.getJSONObject("coord").getDouble("lon");
                    lat = response.getJSONObject("coord").getDouble("lat");
                    getCurrentWeather(lat, lon);
                    getForecastWeather(lat, lon);
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, "Please enter correct city name", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = error.getMessage();
                Toast.makeText(MainActivity.this, "Please enter correct city name", Toast.LENGTH_LONG).show();
                Log.d("Fetch Error",error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private String getCurrentTime() {
        LocalDateTime dateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formattedDateTime = dateTime.format(formatter);

        return formattedDateTime;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Permission Granted...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Permission Denied...", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    public void getCurrentWeather(double lat, double lon) {
        String apiKey = "fc853b4d52b5b246574b3f6ff1f63387";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        String urlCurrent = "https://api.openweathermap.org/data/2.5/weather?lat="
                + lat
                + "&lon="
                + lon
                + "&appid="
                + apiKey
                + "&units=metric";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlCurrent, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                textLastTime.setText(getCurrentTime());
                saveLastResponse(response,0);
                progressBar.setVisibility(View.GONE);
                rLHome.setVisibility(View.VISIBLE);
                updateCurrentWeather(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Current Weather fetch failed...", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void updateCurrentWeather(JSONObject response) {
        try{
            String city = response.getString("name");
            textCityName.setText(city);

            String temperature = response.getJSONObject("main").getString("temp");
            textTemp.setText(temperature + "°C");

            String img = response.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("icon");
            Picasso.get().load("https://openweathermap.org/img/w/" + img + ".png").into(imgWeather);

            String condition = response.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("main");
            textConditions.setText(condition);

            double wSpeed = response.getJSONObject("wind")
                    .getDouble("speed");
            textWindSpeed.setText(""+wSpeed+"Km/h");
        }catch (Exception e){
            Log.d("Update Res",e.getMessage());
            Toast.makeText(this,"Current Weather Update Failed",Toast.LENGTH_SHORT).show();
        }

    }

    private void getForecastWeather(double lat, double lon) {
        String apiKey = "fc853b4d52b5b246574b3f6ff1f63387";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        String urlForecast = "https://api.openweathermap.org/data/2.5/forecast?lat="
                + lat
                + "&lon="
                + lon
                + "&appid="
                + apiKey
                + "&units=metric";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlForecast, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                textLastTime.setText(getCurrentTime());
                saveLastResponse(response,1);
                if (!arr.isEmpty()) {
                    arr.clear();
                }
                updateForecastWeather(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Forecast Weather fetch failed", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void updateForecastWeather(JSONObject response) {
        try{
            int loop = response.getInt("cnt");
            JSONArray forecast = response.getJSONArray("list");

            for (int i = 0; i < loop; i += 1) {
                String time = forecast.getJSONObject(i)
                        .getString("dt_txt");
                double temp = forecast.getJSONObject(i)
                        .getJSONObject("main")
                        .getDouble("temp");
                String condition = forecast.getJSONObject(i)
                        .getJSONArray("weather")
                        .getJSONObject(0)
                        .getString("icon");
                double wSpeed = forecast.getJSONObject(i)
                        .getJSONObject("wind")
                        .getDouble("speed");
                String pod = forecast.getJSONObject(i)
                        .getJSONObject("sys")
                        .getString("pod");
                arr.add(new WeatherModel(temp, condition, wSpeed,time,pod));
                if(i==0){
                    if(pod.equals("n")){
                        imgBG.setImageResource(R.drawable.night);
                    }else {
                        imgBG.setImageResource(R.drawable.day);
                    }
                }
            }
            weatherModelAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.d("Update Res",e.getMessage());
            Toast.makeText(this,"Forecast Weather Update Failed",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location loca) {
        // Update currentLocation with the new location
        location = loca;

        // You can access the latitude and longitude from the currentLocation variable
        lat = location.getLatitude();
        lon = location.getLongitude();

        // Do something with the latitude and longitude
        Log.d("Latitude", String.valueOf(lat));
        Log.d("Longitude", String.valueOf(lon));

        // Stop receiving location updates if needed
        locationManager.removeUpdates(this);
    }

    private void saveLastResponse(JSONObject res, int time) {
        String[] weatherTime = {"CurrentWeatherData","ForecastWeatherData"};
        SharedPreferences sharedPreferences = getSharedPreferences("WeatherData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try{
            String save = res.toString();
            editor.putString(weatherTime[time], save);
            editor.putString("Time", getCurrentTime());
            editor.apply();
        }catch (Exception e){
            Log.d("Save Res",e.getMessage());
            Toast.makeText(this,"Weather Data Save Failed",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveLastResponse(0);
        retrieveLastResponse(1);
    }

    private void retrieveLastResponse(int time) {
        String[] weatherTime = {"CurrentWeatherData","ForecastWeatherData"};
        SharedPreferences sharedPreferences = getSharedPreferences("WeatherData", Context.MODE_PRIVATE);
        String weatherData = sharedPreferences.getString(weatherTime[time], "");
        String dateTime = sharedPreferences.getString("Time", "");
        textLastTime.setText(dateTime);
        try {
            JSONObject response = new JSONObject(weatherData);
            switch (time){
                case 0:
                    updateCurrentWeather(response);
                    break;
                case 1:
                    updateForecastWeather(response);
                    break;
                default:
                    Toast.makeText(MainActivity.this,"Wrong time",Toast.LENGTH_SHORT).show();
                    break;
            }
        }catch (Exception ex){
            Log.d("Load Res",ex.getMessage());
            Toast.makeText(this,"No previous data Found",Toast.LENGTH_SHORT).show();
        }
    }

    public void getFavCoord(String name){
        String apiKey = "fc853b4d52b5b246574b3f6ff1f63387";
        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                +name
                +"&appid="
                +apiKey;
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    textLastTime.setText(getCurrentTime());
                    double lon = response.getJSONObject("coord").getDouble("lon");
                    double lat = response.getJSONObject("coord").getDouble("lat");
                    getFavWeather(lat, lon);
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, "Favorite City Fetch Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = error.getMessage();
                Toast.makeText(MainActivity.this, "Favorite City Fetch Failed", Toast.LENGTH_SHORT).show();
                Log.d("Fetch Error",error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getFavWeather(double lat, double lon) {
        String apiKey = "fc853b4d52b5b246574b3f6ff1f63387";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        String urlCurrent = "https://api.openweathermap.org/data/2.5/weather?lat="
                + lat
                + "&lon="
                + lon
                + "&appid="
                + apiKey
                + "&units=metric";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlCurrent, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                updateFavWeather(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Favorite City info fetch failed...", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void updateFavWeather(JSONObject response) {
        try{
            String city = response.getString("name");
            String temperature = response.getJSONObject("main").getString("temp");
            String img = response.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("icon");
            String condition = response.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("main");
            double wSpeed = response.getJSONObject("wind")
                    .getDouble("speed");
            favArr.add(new FavCityModel(0,city,temperature,condition,String.valueOf(wSpeed),img));
        }catch (Exception e){
            Log.d("Update Res",e.getMessage());
            Toast.makeText(this,"Favorite City Data Update Failed",Toast.LENGTH_SHORT).show();
        }
        favCityAdapter.notifyDataSetChanged();
    }
}