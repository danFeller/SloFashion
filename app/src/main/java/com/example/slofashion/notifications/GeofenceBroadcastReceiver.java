package com.example.slofashion.notifications;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.slofashion.DialogueActivity;
import com.example.slofashion.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    public static class GeofenceLocation {
        public String name;
        public double lat;
        public double lng;
        public float rad;

        public GeofenceLocation(String name, double lat, double lng, float rad) {
            this.name = name;
            this.lat = lat;
            this.lng = lng;
            this.rad = rad;
        }
    }

    private static final String TAG = GeofenceBroadcastReceiver.class.getSimpleName();
    private static final GeofenceLocation[] gfLocs = new GeofenceLocation[] {
            new GeofenceLocation("DPIBuilding", 41.8791649, -87.6375703, 300),
            new GeofenceLocation("IlliniUnion", 40.1091134, -88.2271691, 400),
    };

    // https://developer.android.com/develop/sensors-and-location/location/geofencing
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent == null) {
            return;
        }

        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        Log.i(TAG, "Received a geofence transition");

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            sendNotification(context, DialogueActivity.DialogueType.ENTER_STORE);
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            sendNotification(context, DialogueActivity.DialogueType.LEAVE_STORE);
        } else {
            // Log the error.
            Log.e(TAG, "Other geofence transition event");
        }
    }

    public static List<Geofence> getGeofencesList() {
        List<Geofence> geofenceList = new ArrayList<>();

        for (GeofenceLocation gfLoc : gfLocs) {
            geofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(gfLoc.name)
                    .setCircularRegion(
                            gfLoc.lat,
                            gfLoc.lng,
                            gfLoc.rad
                    )
                    .setExpirationDuration(1000*60*60*24)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }

        return geofenceList;
    }

    // https://developer.android.com/develop/sensors-and-location/location/geofencing
    public static void sendNotification(@NotNull Context context, @NotNull DialogueActivity.DialogueType dialogueType) {
        Intent notifyIntent = new Intent(context, DialogueActivity.class);
        // Set the Activity to start in a new, empty task.
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntent.putExtra(Intent.EXTRA_TEXT, dialogueType.toString());
        // Create the PendingIntent.
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                context, (int) (Math.random() * 50), notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1234");
        builder.setContentIntent(notifyPendingIntent);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        switch(dialogueType) {
            case ENTER_STORE:
                builder
                    .setContentTitle("You are entering a store")
                    .setContentText("Check up on your budget");
                break;
            case LEAVE_STORE:
                builder
                        .setContentTitle("You left the store")
                        .setContentText("Update your budget");
                break;
        }

        Log.i(TAG, "About to send notification");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify((int) (Math.random() * 50), builder.build());
    }
}