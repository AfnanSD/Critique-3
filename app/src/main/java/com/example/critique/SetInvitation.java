package com.example.critique;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class SetInvitation extends AppCompatActivity {

    Spinner stores;
    EditText msg;
    Button setLocation, done;
    TextView location;
    DBHelper database;
    String storeID, selectedStore, msgText;
    int ID;


    Intent data;

    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_invitation);

        database = DBHelper.getInstance(this);
        data = new Intent(SetInvitation.this, GeofenceHelper.class);

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);


        Intent intent = getIntent();
        ID = intent.getIntExtra("ID", -1); //retailer id
        Cursor storesCursor = database.getStores(ID);
        ArrayList<String> spinnerArray = new ArrayList<>();
        storesCursor.moveToFirst();
        do {
            spinnerArray.add(storesCursor.getString(2));
        }while(storesCursor.moveToNext());
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);

        stores = findViewById(R.id.stores);
        stores.setAdapter(spinnerArrayAdapter);

        msg = findViewById(R.id.invitationMsg);
        setLocation = findViewById(R.id.setLocation);
//        location = findViewById(R.id.LatLng);
        done = findViewById(R.id.DoneInvitation);
//        done.setVisibility(View.INVISIBLE);

        setLocation.setOnClickListener(v -> {
            Intent intent1 = new Intent(SetInvitation.this, MapsActivity.class);
            activityResultLauncher.launch(intent1);
        });
    }

    //start activity for result alt
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult activityResult) {
            int result = activityResult.getResultCode();
            Intent data = activityResult.getData();

            if(result == RESULT_OK){
                Toast.makeText(SetInvitation.this, "Successful", Toast.LENGTH_SHORT).show();
                assert data != null;
                double lat = data.getDoubleExtra("Lat", 0);
                double lng = data.getDoubleExtra("Lng", 0);
                done.setVisibility(View.VISIBLE);
                done.setOnClickListener(v -> {
                    selectedStore = stores.getSelectedItem().toString(); data.putExtra("title", selectedStore);
                    String RetailerID = ID + "";
                    String NotificationID =  UUID.randomUUID().toString();;
                    storeID = database.getRow("storesTable", "STORENAME = ? AND RETAILERID = ? ", new String[]{selectedStore, RetailerID}).getString(0);
                    msgText = msg.getText().toString(); data.putExtra("textMsg", msgText);
                    database.insertInvitationData(NotificationID, storeID, selectedStore, msgText, lat, lng);
                    LatLng geo = new LatLng(lat,lng);
                    addGeofence(geo, NotificationID);//afnan was up

                    Intent intent = new Intent(SetInvitation.this, RetailerProfile.class);
                    setResult(RESULT_OK, intent);
//                    SetInvitation.this.startActivity(new Intent(SetInvitation.this, RetailerProfile.class));
                });
            }else
                Toast.makeText(SetInvitation.this, "An error has occurred", Toast.LENGTH_SHORT).show();
        }
    });

    private static final String TAG = "SetInvitation";


    //here and not on maps activity but latlng wroks
    @SuppressLint("MissingPermission")
    private void addGeofence(LatLng latLng, String ID){
        //id is random so each geofence should be uniqe

        Geofence geofence = geofenceHelper.getGeofence(ID, latLng, 700, Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_ENTER);//(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL)
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);

        //PendingIntent pendingIntent = geofenceHelper.getPendingIntent(data);//data? why?
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();//afnan
        geofencingClient.addGeofences(geofencingRequest, pendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(SetInvitation.this,"geofence added",Toast.LENGTH_SHORT).show();//afnan
                //so geofance added show after clicking done so adding is good notification not so much
                Log.d(TAG, "OnSuccess: Geofence Added");//afnan

            }
        }
        ).addOnFailureListener(e -> {
            String errorMsg = geofenceHelper.getErrorString(e);
            Log.d(TAG, "OnFailure: " + errorMsg);
        });


    }
}