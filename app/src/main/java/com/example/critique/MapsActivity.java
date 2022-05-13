package com.example.critique;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import android.app.UiModeManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.critique.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {


    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private final int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private final int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    private float radius = 500;

    AlertDialog.Builder builder;
    Button button;

    //afnan
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        button = findViewById(R.id.doneMap);
        button.setVisibility(View.INVISIBLE);
        button.setOnClickListener(v -> builder.show());

        //was not here - afnan
        //try getting geofence from here not button??
        geofencingClient = LocationServices.getGeofencingClient(this);//this is because of context
        geofenceHelper = new GeofenceHelper(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng Riyadh = new LatLng(24.7136, 46.6753);
        mMap.addMarker(new MarkerOptions().position(Riyadh).title("Marker in Riyadh").draggable(true));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(Riyadh, 12.0F));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Riyadh, 12.0f));


        enableUserLocation();

        mMap.setOnMapLongClickListener(this);
    }

    private void enableUserLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            mMap.setMyLocationEnabled(true);
        else
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
        else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                mMap.setMyLocationEnabled(true);

        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "You can add geofence", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Background location access is necessary for geofence to trigger", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        if (Build.VERSION.SDK_INT >= 29) {
            //We need background permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                handleMapLongClick(latLng);
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
            }
        } else {
            handleMapLongClick(latLng);
        }

    }

    private void handleMapLongClick(LatLng latLng) {
        mMap.clear();
        addMarker(latLng);
        addCircle(latLng, radius);

        button.setVisibility(View.VISIBLE);
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    dialog.dismiss();
                    Intent intent = new Intent(MapsActivity.this, SetInvitation.class);
                    intent.putExtra("Lat", latLng.latitude);
                    intent.putExtra("Lng", latLng.longitude);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        };
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Location");
        builder.setMessage("Are you sure from the location?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener);
    }



    private void addMarker(LatLng latlng){
        MarkerOptions markerOptions = new MarkerOptions().position(latlng);
        mMap.addMarker(markerOptions);
    }

    private void addCircle(LatLng latlng, float radius){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latlng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 0,0));
        circleOptions.fillColor(Color.argb(64, 255, 0,0));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);

    }

}