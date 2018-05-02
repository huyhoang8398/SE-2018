package com.example.huyhoang8398.se;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_LOCATION = 1;
    Button button;
    TextView locationText, today, temp_text, humid_text, press_text, desc_text;
    ImageView weatherIcon;
    LocationManager locationManager;
    String lattitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        button       = findViewById(R.id.button_location);
        locationText = findViewById(R.id.text_location);
        today        = findViewById(R.id.today);
        desc_text    = findViewById(R.id.desc_text);
        temp_text    = findViewById(R.id.temp_text);
        humid_text   = findViewById(R.id.humid_text);
        press_text   = findViewById(R.id.press_text);
        weatherIcon  = findViewById(R.id.weatherIcon);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();


        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                getLocation();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //textView.setPadding(30,30,30,30);

        }
    }

    private void getLocation() throws IOException {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(latti,longi,1);
                if(addresses != null && addresses.size() > 0){
                    final Address ADDRESS = addresses.get(0);
                    String result = ADDRESS.getLocality() + ", " + ADDRESS.getAdminArea() +", "+ADDRESS.getCountryName();
                    locationText.setText("Your current location is"+ ":\n " + result);


                    Function.placeIdTask asyncTask = new Function.placeIdTask(new Function.AsyncResponse() {
                        @Override
                        public void processFinish(String weather_city, String weather_description, String weather_temp, String weather_humid, String weather_pressure, String weatherUpdateOn, String WeatherIconText, String sun_rise) {
                            desc_text.setText(weather_description);
                            temp_text.setText("Temperature: "+weather_temp);
                            humid_text.setText("Humidity: "+weather_humid);
                            press_text.setText("Pressure: "+weather_pressure);
                            today.setText(WeatherIconText);

                        }
                    });
                    asyncTask.execute(lattitude,longitude);
                }


            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(latti,longi,1);
                if(addresses != null && addresses.size() > 0){
                    final Address ADDRESS = addresses.get(0);
                    String result = ADDRESS.getLocality() + ", " + ADDRESS.getAdminArea() +", "+ADDRESS.getCountryName();
                    locationText.setText("Your current location is"+ ":\n " + result);

                    Function.placeIdTask asyncTask = new Function.placeIdTask(new Function.AsyncResponse() {
                        @Override
                        public void processFinish(String weather_city, String weather_description, String weather_temp, String weather_humid, String weather_pressure, String weatherUpdateOn, String WeatherIconText, String sun_rise) {
                            desc_text.setText(weather_description);
                            temp_text.setText("Temperature: "+weather_temp);
                            humid_text.setText("Humidity: "+weather_humid);
                            press_text.setText("Pressure: "+weather_pressure);
                            today.setText(WeatherIconText);

                        }
                    });

                    asyncTask.execute(lattitude,longitude);
                }



            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(latti,longi,1);
                if(addresses != null && addresses.size() > 0){
                    final Address ADDRESS = addresses.get(0);
                    String result = ADDRESS.getLocality() + ", " + ADDRESS.getAdminArea() +", "+ADDRESS.getCountryName();
                    locationText.setText("Your current location is"+ ":\n " + result);

                    Function.placeIdTask asyncTask = new Function.placeIdTask(new Function.AsyncResponse() {
                        @Override
                        public void processFinish(String weather_city, String weather_description, String weather_temp, String weather_humid, String weather_pressure, String weatherUpdateOn, String WeatherIconText, String sun_rise) {
                            desc_text.setText(weather_description);
                            temp_text.setText("Temperature: "+weather_temp);
                            humid_text.setText("Humidity: "+weather_humid);
                            press_text.setText("Pressure: "+weather_pressure);
                            today.setText(WeatherIconText);




                        }
                    });
                    asyncTask.execute(lattitude,longitude);
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