package com.example.critique;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class SignIn extends AppCompatActivity {

    DBHelper db;
    EditText username, password;
    Button login;
    TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        enableUserLocation();

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        registerLink = (TextView) findViewById(R.id.registerLink);

        db = DBHelper.getInstance(this);


        startService(new Intent(this, TrackAndInviteService.class));
        startService(new Intent(this, GeofenceTrasitionService.class));
//        startService(new Intent(this, GeofenceRegistrationService.class));


        registerLink.setOnClickListener(view -> SignIn.this.startActivity(new Intent(SignIn.this, SignUp.class)));

        login.setOnClickListener(view -> {
            String userN = username.getText().toString();
            String pass = password.getText().toString();

            if (userN.equals("") || pass.equals(""))
                Toast.makeText(SignIn.this, "Please fill all field", Toast.LENGTH_SHORT).show();
            else {
                boolean valid = db.validateInput(userN, pass);
                if (valid) {
                    if(db.getRow("Users", "username = ?", new String[]{userN}).getString(3).equals("Retailer")) {
                        Intent data = new Intent(SignIn.this, RetailerProfile.class);
                        data.putExtra("name", userN);
                        data.putExtra("ID", db.getRow("Users", "username = ?", new String[]{userN}).getInt(0));
                        SignIn.this.startActivity(data);
                    }
                    else
                        SignIn.this.startActivity(new Intent(SignIn.this, ViewStores.class));

                }
                else
                    Toast.makeText(SignIn.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        MenuItem item = menu.findItem(R.id.myswitch);
        item.setActionView(R.layout.switch_layout);

        Switch darkmode = item.getActionView().findViewById(R.id.darkmode);
        darkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (darkmode.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    //getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else
                    //getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
        return true;
    }

    private final int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private final int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    private void enableUserLocation(){
        if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED))
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
//                mMap.setMyLocationEnabled(true);

        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "You can add geofence", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Background location access is necessary for geofence to trigger", Toast.LENGTH_SHORT).show();
    }

//    private String CHANNEL_NAME = "Critique channel";
//    public static int CHANNEL_ID = 1;//"com.example.notifications" + CHANNEL_NAME;
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void createChannels() {
//        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
//        notificationChannel.enableLights(true);
//        notificationChannel.enableVibration(true);
//        notificationChannel.setDescription("this is the description of the channel.");
//        notificationChannel.setLightColor(Color.RED);
//        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.createNotificationChannel(notificationChannel);
//    }


}