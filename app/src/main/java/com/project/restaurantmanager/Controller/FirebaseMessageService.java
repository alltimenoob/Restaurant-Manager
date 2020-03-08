package com.project.restaurantmanager.Controller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.project.restaurantmanager.Model.AdminActivity;
import com.project.restaurantmanager.Model.CustomerActivity;
import com.project.restaurantmanager.Model.EmployeeActivity;
import com.project.restaurantmanager.R;

public class FirebaseMessageService extends FirebaseMessagingService {
    public FirebaseMessageService() {
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(getApplicationContext());
        sharedPreferencesHandler.setToken(s);

        Log.d("firebasetoken", "onNewToken: "+s);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        PendingIntent pendingIntent;
        Intent intent = null;


            if (new SharedPreferencesHandler(getApplicationContext()).getPost().equals("Customer")) {
                intent = new Intent(getApplicationContext(), CustomerActivity.class);
            } else if (new SharedPreferencesHandler(getApplicationContext()).getPost().equals("Employee")) {
                intent = new Intent(getApplicationContext(), EmployeeActivity.class);
            } else if (new SharedPreferencesHandler(getApplicationContext()).getPost().equals("Admin")) {
                intent = new Intent(getApplicationContext(), AdminActivity.class);

            }

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


            NotificationChannel channel = new NotificationChannel("ID", "CHANNEL1", NotificationManager.IMPORTANCE_DEFAULT);

            Notification.Builder builder = new Notification.Builder(getApplicationContext())
                    .setChannelId("ID")
                    .setSmallIcon(R.mipmap.icon)
                    .setContentIntent(resultPendingIntent)
                    .setAutoCancel(true)
                    .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.notification))
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setContentTitle(remoteMessage.getNotification().getTitle());

            if (getApplicationInfo().enabled) {

            }

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            Notification notification = builder.build();
            channel.setSound(Uri.parse("android.resource://"
                    + getApplicationContext().getPackageName() + "/" + R.raw.notification), audioAttributes);

            notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR | Notification.FLAG_AUTO_CANCEL;

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            manager.notify(1, notification);




    }

    public static void removeToken(String user,String id)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user);
        databaseReference.child(id).removeValue();
    }
}
