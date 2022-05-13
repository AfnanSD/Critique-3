package com.example.critique;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    //maybe dont use two events for geofence

    private static final String TAG = "GeofenceBroadcastReceiv";


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Toast.makeText(context, "Geofence Triggered", Toast.LENGTH_LONG).show();

        NotificationHelper notificationHelper = new NotificationHelper(context);
        //notificationHelper.sendDefaultPriorityNotification("GEOFENCE_TRANSITION_ENTER", "default", MapsActivity.class);
        //notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_ENTER", "", MapsActivity.class);
        notificationHelper.sendHighPriorityNotification(intent.getStringExtra("Title"), intent.getStringExtra("TextMsg"), SetInvitation.class);//class?

//        notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_ENTER", "000000", SetInvitation.class);//class?

       /*
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.sendHighPriorityNotification(intent.getStringExtra("title"), intent.getStringExtra("textMsg"), SignIn.class);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "OnReceive: Error receiving geofence event");
            return;
        }

        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();

        ///here? switch
        if (geofenceList != null)
            for (Geofence geofence: geofenceList) {
                Log.d(TAG, "onReceive: " + geofence.getRequestId());
            }

        //afnan

        int transitionType = geofencingEvent.getGeofenceTransition();

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show();
                //notificationHelper.setTilte()
                //notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_ENTER", "", MapsActivity.class);
                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_ENTER", "", RetailerProfile.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show();
                //notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_DWELL", "", MapsActivity.class);
                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_dwell", "", RetailerProfile.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show();
                //notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_EXIT", "", MapsActivity.class);
                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_Exirt", "", RetailerProfile.class);
                break;
            default:
                notificationHelper.sendHighPriorityNotification("0000000", "", RetailerProfile.class);
                break;
        }
*/
    }
}