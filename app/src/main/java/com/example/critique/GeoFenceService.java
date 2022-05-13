package com.example.critique;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

/**
 * Created by daamjad on 3/7/2017.
 */

public class GeoFenceService extends IntentService {

    public static final String TAG = "Location";
    Intent broadcastIntent = new Intent("GeoFence");
    DBHelper database; //added by reem to set notification message

    public GeoFenceService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        database = DBHelper.getInstance(this); //added by reem to set notification message
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event.hasError()) {
            Log.d(TAG, "Error: ");
        } else {
            int transition = event.getGeofenceTransition();
            List<Geofence> geofences = event.getTriggeringGeofences();
            Geofence geofence = geofences.get(0);
            String requestId = geofence.getRequestId();
            if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.d(TAG, "Entering geofence: " + requestId);
                //added by reem
                Cursor cursor = database.getRow("Invitation", "NotificationID = ?", new String[]{requestId});
                String title ="", textMsg ="";
                if (cursor.moveToFirst()) {
                    title = cursor.getString(2);
                    textMsg = cursor.getString(3);
                }
                broadcastIntent.putExtra("Title", title);
                broadcastIntent.putExtra("TextMsg", textMsg);
                //---------
                broadcastIntent.putExtra("Enter", true);
                sendBroadcast(broadcastIntent);
            } else if (transition == Geofence.GEOFENCE_TRANSITION_EXIT) { //no need for exit, enter will be enough
                Log.d(TAG, "Exit geofence: " + requestId);
                broadcastIntent.putExtra("Exit", true);
                sendBroadcast(broadcastIntent);
            }
        }

    }
}
