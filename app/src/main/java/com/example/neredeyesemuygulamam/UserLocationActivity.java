package com.example.neredeyesemuygulamam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

public class UserLocationActivity extends AppCompatActivity  implements OnClickListener {

    //Bulunduğumuz Konumu aldıran sınıf.
    private static final int REQUEST_LOCATION = 1;
    Button button;
    TextView textView;
    LocationManager locationManager;
    String lattitude, longitude;
    double lat,lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlocation);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.konumbuton);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                 lat = location.getLatitude();
                 lon = location.getLongitude();
                lattitude = String.valueOf(lat);
                longitude = String.valueOf(lon);

                textView.setText("Konumunuz: "+ "\n" + "Enlem = " + lattitude
                        + "\n" + "Boylam = " + longitude);

            } else  if (location1 != null) {
                 lat = location1.getLatitude();
                 lon = location1.getLongitude();
                lattitude = String.valueOf(lat);
                longitude = String.valueOf(lon);

                textView.setText("Konumunuz: "+ "\n\n" + "Enlem = " + lattitude
                        + "\n" + "Boylam = " + longitude);


            } else  if (location2 != null) {
                lat = location2.getLatitude();
                lon = location2.getLongitude();
                lattitude = String.valueOf(lat);
                longitude = String.valueOf(lon);

                textView.setText("Konumunuz: "+ "\n" + "Enlem = " + lattitude
                        + "\n" + "Boylam = " + longitude);
            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
        //Eğer Konum çekilebidiyse RestaurantListActivity'e gidiyor.
        Intent i = new Intent(UserLocationActivity.this,RestaurantListActivity.class);
        i.putExtra("lat",lat+"");
        i.putExtra("longi",lon+"");
        UserLocationActivity.this.startActivity(i);
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
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

