package com.example.critique;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

public class GeofenceHelper extends ContextWrapper {

    private static final String TAG = "GeofenceHelper";
    PendingIntent pendingIntent;

    public GeofenceHelper(Context base) {
        super(base);
    }

    public GeofencingRequest getGeofencingRequest(Geofence geofence) {
        return new GeofencingRequest.Builder()
                .addGeofence(geofence)//could be list of geofences should we??
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)//deleted this:| GeofencingRequest.INITIAL_TRIGGER_DWELL - afnan
                .build();
    }

    public Geofence getGeofence(String ID, LatLng latLng, float radius, int transationType) {
        return new Geofence.Builder()
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)//radius could be our choosing?
                .setRequestId(ID)//unique for each geofence
                .setTransitionTypes(transationType)
                .setLoiteringDelay(5000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }


    public PendingIntent getPendingIntent(Intent data){
        if(pendingIntent != null)
            return pendingIntent;
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        intent.putExtra("title", data.getStringExtra("title"));
        intent.putExtra("textMsg", data.getStringExtra("textMsg"));
        pendingIntent = PendingIntent.getBroadcast(this, 2607, intent, PendingIntent.FLAG_IMMUTABLE);//was comement

        return pendingIntent;
    }

    //afnan
    public PendingIntent getPendingIntent(){
        if(pendingIntent != null)
            return pendingIntent;
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        //intent.putExtra("title", data.getStringExtra("title"));
        //intent.putExtra("textMsg", data.getStringExtra("textMsg"));
        pendingIntent = PendingIntent.getBroadcast(this, 2607, intent, PendingIntent.FLAG_IMMUTABLE);//was comement

        return pendingIntent;
    }

    public String getErrorString(Exception e){
        if (e instanceof ApiException){
            ApiException apiException = (ApiException) e;
            switch(apiException.getStatusCode()){
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    return "GEOFENCE_NOT_AVAILABLE";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                    return "GEOFENCE_TOO_MANY_GEOFENCE";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "GEOFENCE_TOO_MANY_PENDING_INTENTS";
            }
        }
    return e.getLocalizedMessage();
    }
}
