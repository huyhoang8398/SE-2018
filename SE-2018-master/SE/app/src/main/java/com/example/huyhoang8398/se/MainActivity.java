package com.example.huyhoang8398.se;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
   /* this code declare name of components that use in the layout */
    private static final int REQUEST_LOCATION = 1;
    Button button;
    EditText locationText;
    TextView temp_text, humid_text, press_text, desc_text,textNoti;
    ImageView weatherIcon,today;
    LocationManager locationManager;
    String lattitude,longitude;
    String url_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        /* this code match name of components with their real id (in string) */
        button       = findViewById(R.id.button_location);
        locationText = findViewById(R.id.text_location);
        today        = findViewById(R.id.today);
        desc_text    = findViewById(R.id.desc_text);
        temp_text    = findViewById(R.id.temp_text);
        humid_text   = findViewById(R.id.humid_text);
        press_text   = findViewById(R.id.press_text);
        weatherIcon  = findViewById(R.id.weatherIcon);
        textNoti     = findViewById(R.id.textNoti);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                /* this code shows what function(s) to do when button clicked*/
                getLocationWeatherInfor();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /* this is the function that get location and it details */
    private void getLocationWeatherInfor() throws IOException {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);
            /* 3 options above are used as sources to get location information: from cellular data, GPS or WIFI */
            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                lattitude = String.valueOf(lat);
                longitude = String.valueOf(lon);
                /* this code below convert Co-ordinates into geographical address */
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(lat,lon,1);
                if(addresses != null && addresses.size() > 0) {
                    final Address ADDRESS = addresses.get(0);
                    /* and retrieve city/province and country */
                    String result =  ADDRESS.getAdminArea() + ", " + ADDRESS.getCountryName();
                    if (locationText.getText().toString().equals("")||locationText.getText().toString().equals(result)){
                        locationText.setText(result);
                        Function.placeIdTask asyncTask = null;
                        asyncTask = new Function.placeIdTask(new Function.AsyncResponse(){
                            @Override
                            public void processFinish(String weather_city, String weather_description, String weather_temp, String weather_humid, String weather_pressure, String weatherUpdateOn, String WeatherIconText, String sun_rise) {
                                textNoti.setText("");
                                desc_text.setText(weather_description);
                                temp_text.setText("Temperature: " + weather_temp);
                                humid_text.setText("Humidity: " + weather_humid);
                                press_text.setText("Pressure: " + weather_pressure);
                                url_icon = "http://openweathermap.org/img/w/" + WeatherIconText;
                                Picasso.get().load(url_icon).into(today);

                            }
                        });
                        asyncTask.execute(lattitude, longitude);
                    }
                    else{
                        List<Address> geoResults = geocoder.getFromLocationName(locationText.getText().toString(),1);
                        if(geoResults.size()>0) {
                            Address addr = geoResults.get(0);

                            Function.placeIdTask asyncTask = new Function.placeIdTask(new Function.AsyncResponse() {
                                @Override
                                public void processFinish(String weather_city, String weather_description, String weather_temp, String weather_humid, String weather_pressure, String weatherUpdateOn, String WeatherIconText, String sun_rise) {
                                    textNoti.setText("");
                                    desc_text.setText(weather_description);
                                    temp_text.setText("Temperature: " + weather_temp);
                                    humid_text.setText("Humidity " + weather_humid);
                                    press_text.setText("Pressure: " + weather_pressure);
                                    url_icon = "http://openweathermap.org/img/w/" + WeatherIconText;
                                    Picasso.get().load(url_icon).into(today);
                                }
                            });
                            asyncTask.execute(String.valueOf(addr.getLatitude()),String.valueOf(addr.getLongitude()));
                        }else textNoti.setText("Invalid city/province");
                    }
                }


            } else  if (location1 != null) {
                double lat = location1.getLatitude();
                double lon = location1.getLongitude();
                lattitude = String.valueOf(lat);
                longitude = String.valueOf(lon);
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(lat,lon,1);
                if(addresses != null && addresses.size() > 0) {
                    final Address ADDRESS = addresses.get(0);
                    String result =  ADDRESS.getAdminArea() + ", " + ADDRESS.getCountryName();

                    if (locationText.getText().toString().equals("")||locationText.getText().toString().equals(result)) {
                        locationText.setText( result);

                        Function.placeIdTask asyncTask = new Function.placeIdTask(new Function.AsyncResponse() {
                            @Override
                            public void processFinish(String weather_city, String weather_description, String weather_temp, String weather_humid, String weather_pressure, String weatherUpdateOn, String WeatherIconText, String sun_rise) {
                                textNoti.setText("");
                                desc_text.setText(weather_description);
                                temp_text.setText("Temperature: " + weather_temp);
                                humid_text.setText("Humidity: " + weather_humid);
                                press_text.setText("Pressure: " + weather_pressure);
                                url_icon = "http://openweathermap.org/img/w/" + WeatherIconText;
                                Picasso.get().load(url_icon).into(today);


                            }
                        });


                        asyncTask.execute(lattitude, longitude);
                    }else{

                        List<Address> geoResults = geocoder.getFromLocationName(locationText.getText().toString(),1);
                        if(geoResults.size()>0) {
                            Address addr = geoResults.get(0);

                            Function.placeIdTask asyncTask = new Function.placeIdTask(new Function.AsyncResponse() {

                                @Override
                                public void processFinish(String weather_city, String weather_description, String weather_temp, String weather_humid, String weather_pressure, String weatherUpdateOn, String WeatherIconText, String sun_rise) {
                                    textNoti.setText("");
                                    desc_text.setText(weather_description);
                                    temp_text.setText("Temperature: " + weather_temp);
                                    humid_text.setText("Humidity " + weather_humid);
                                    press_text.setText("Pressure: " + weather_pressure);
                                    url_icon = "http://openweathermap.org/img/w/" + WeatherIconText;
                                    Picasso.get().load(url_icon).into(today);
                                }
                            });
                            asyncTask.execute(String.valueOf(addr.getLatitude()),String.valueOf(addr.getLongitude()));
                        }else textNoti.setText("Invalid city/province");
                    }
                }



            } else  if (location2 != null) {
                double lat = location2.getLatitude();
                double lon = location2.getLongitude();
                lattitude = String.valueOf(lat);
                longitude = String.valueOf(lon);

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(lat,lon,1);
                if(addresses != null && addresses.size() > 0) {
                    final Address ADDRESS = addresses.get(0);
                    String result =  ADDRESS.getAdminArea() + ", " + ADDRESS.getCountryName();
                    if (locationText.getText().toString().equals("")||locationText.getText().toString().equals(result)) {
                        locationText.setText( result);

                        Function.placeIdTask asyncTask = new Function.placeIdTask(new Function.AsyncResponse() {
                            @Override
                            public void processFinish(String weather_city, String weather_description, String weather_temp, String weather_humid, String weather_pressure, String weatherUpdateOn, String WeatherIconText, String sun_rise) {
                                textNoti.setText("");
                                desc_text.setText(weather_description);
                                temp_text.setText("Temperature: " + weather_temp);
                                humid_text.setText("Humidity: " + weather_humid);
                                press_text.setText("Pressure: " + weather_pressure);
                                url_icon = "http://openweathermap.org/img/w/" + WeatherIconText;
                                Picasso.get().load(url_icon).into(today);


                            }
                        });
                        asyncTask.execute(lattitude, longitude);
                    }else{

                        List<Address> geoResults = geocoder.getFromLocationName(locationText.getText().toString(),1);
                        if(geoResults.size()>0) {
                            Address addr = geoResults.get(0);

                            Function.placeIdTask asyncTask = new Function.placeIdTask(new Function.AsyncResponse() {

                                @Override
                                public void processFinish(String weather_city, String weather_description, String weather_temp, String weather_humid, String weather_pressure, String weatherUpdateOn, String WeatherIconText, String sun_rise) {
                                    textNoti.setText("");
                                    desc_text.setText(weather_description);
                                    temp_text.setText("Temperature: " + weather_temp);
                                    humid_text.setText("Humidity " + weather_humid);
                                    press_text.setText("Pressure: " + weather_pressure);
                                    url_icon = "http://openweathermap.org/img/w/" + WeatherIconText;
                                    Picasso.get().load(url_icon).into(today);
                                }
                            });
                            asyncTask.execute(String.valueOf(addr.getLatitude()),String.valueOf(addr.getLongitude()));
                        }else textNoti.setText("Invalid city/province");
                    }
                }




            }else{

                Toast.makeText(this,"Unable to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }






}