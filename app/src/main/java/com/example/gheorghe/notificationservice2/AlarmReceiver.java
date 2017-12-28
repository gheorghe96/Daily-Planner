package com.example.gheorghe.notificationservice2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Gheorghe on 26.12.2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private static final int MY_NOTIFICATION_ID = 4;
    NotificationManager nfm;
    Notification ntf;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, MainActivity.class); //SO THIS ACTIVITY IN SETTED ALARM TIME.
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

        context.getApplicationContext();


        ntf = new NotificationCompat.Builder(context)
                .setContentTitle("Daily Planner")
                .setContentText("You have a new event, click here to see more.")
                .setTicker("Notification Head")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND) //PLAY DEFAULT SOUND
                .setAutoCancel(true) // REMOVE ALARM NOTIFICATION JUST BY SWIPE
                .setSmallIcon(R.mipmap.ic_launcher) //SHOWED IN STATUS BAR
                .setColor(200) //Notification text color
                .setLights(0xff493C7C, 1000, 1000)
                .build();

        nfm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nfm.notify(MY_NOTIFICATION_ID, ntf);
    }

}
