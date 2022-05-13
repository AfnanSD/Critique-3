package com.example.critique;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;
import java.util.Random;

public class TrackAndInviteService extends Service {
    private static final String TAG = "TrackAndInviteService";

    private static TrackAndInviteService INSTANCE;

    DBHelper database;
    private SharedPreferences prefs;

    public static TrackAndInviteService getInstance(){
        if (INSTANCE == null)
            INSTANCE = new TrackAndInviteService();
        return INSTANCE;
    }

    public Intent newIntent(Context context){
        return new Intent(context, TrackAndInviteService.class);
    }

    private NotificationManagerCompat notificationManagerCompat;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.e(TAG, "onCreate");
        notificationManagerCompat = notificationManagerCompat.from(this);
        database = DBHelper.getInstance(TrackAndInviteService.this);
        prefs = getApplicationContext().getSharedPreferences(Constant.SharedPrefs.Geofences, Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);
        Log.e(TAG, "onStartCommand");
        checkGeofenes(intent);

        return START_STICKY;

    }
    private static int id = 0 ;
    private void checkGeofenes(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        // Test that the reported transition was of interest.
        if ((geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) || (geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL)) {
            Location location = geofencingEvent.getTriggeringLocation();
            List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
            if (geofenceList != null)
                for (Geofence geofence : geofenceList) {
                    String ID = geofence.getRequestId();
                    String title, textMsg;
                    Cursor cursor = database.getRow("Invitation", "NotificationID = ?", new String[]{ID});
                    if (cursor.moveToFirst()) {
                        title = cursor.getString(2);
                        textMsg = cursor.getString(3);
                        startForeground(id++, createNotification(title, textMsg));
                    }
                }

        }
    }

    private String CHANNEL_NAME = "Critique channel";
    private String CHANNEL_ID = "com.example.critique" + CHANNEL_NAME;
    private int CHANNELID = 1;

    public Notification createNotification(String title, String body) {

        Intent intent = new Intent(this, ViewStores.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 267, intent, PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().setSummaryText("summary").setBigContentTitle(title).bigText(body))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

//        NotificationManagerCompat.from(this).notify(new Random().nextInt(), notification);

    }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void createChannels() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setDescription("this is the description of the channel.");
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);
    }


    public TrackAndInviteService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}