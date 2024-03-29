package com.tanobel.it_yoga.tis_mobile.model;

/**
 * Created by IT_Yoga on 23/10/2018.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.app.NotificationCompat;

import android.os.Build;
import android.text.Html;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.tanobel.it_yoga.tis_mobile.R;

/**
 * Created by Ravi on 31/03/15.
 */

public class MyNotificationManager {

    public static final int ID_BIG_NOTIFICATION = 234;
    public static final int ID_SMALL_NOTIFICATION = 235;

    private Context mCtx;
    String channel_id = "";

    public MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    //the method will show a big notification with an image
    //parameters are title for message title, message for message text, url of the big image and an intent that will open
    //when you will tap on the notification
    public void showBigNotification(String title, String message, String url, Intent intent) {
        int pendingFlags;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingFlags=PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT;
        }else {
            pendingFlags=PendingIntent.FLAG_UPDATE_CURRENT;
        }
        PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            mCtx,
                            ID_BIG_NOTIFICATION,
                            intent,
                            pendingFlags
                    );
        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = null;
            notificationChannel = new NotificationChannel("Tes_123", "TIS MOBILE", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }

            channel_id = notificationManager.getNotificationChannel("Tes_123").getId();
        }

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(getBitmapFromURL(url));

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, channel_id);
        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.ic_tis).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setStyle(bigPictureStyle)
                .setSmallIcon(R.mipmap.ic_tis)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_tis))
                .setContentText(message)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(ID_BIG_NOTIFICATION, notification);
    }

    //the method will show a small notification
    //parameters are title for message title, message for message text and an intent that will open
    //when you will tap on the notification
    public void showSmallNotification(String title, String message, Intent intent) {
        int pendingFlags;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingFlags = PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_ONE_SHOT;
        }else{
            pendingFlags = PendingIntent.FLAG_ONE_SHOT;
        }
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                mCtx,
                ID_SMALL_NOTIFICATION,
                intent,
                pendingFlags
        );
        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = null;
            notificationChannel = new NotificationChannel("Tes_123", "TIS MOBILE", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }

            channel_id = notificationManager.getNotificationChannel("Tes_123").getId();
        }

        NotificationCompat.Builder mBuilder = null;
        mBuilder = new NotificationCompat.Builder(mCtx, channel_id);

        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.ic_tis).setTicker(title).setWhen(0)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_tis)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_tis))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);
    }

    //The method will return Bitmap from an image URL
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}