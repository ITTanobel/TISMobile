package com.tanobel.it_yoga.tis_mobile.util;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.tanobel.it_yoga.tis_mobile.LoginActivity;
import com.tanobel.it_yoga.tis_mobile.model.MyNotificationManager;
import com.tanobel.it_yoga.tis_mobile.ApproveSMP_Activity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yoga on 03/11/16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    SharedPreferences shp;
    Intent intent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");
            String menu = data.getString("menu").trim();

            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            shp = this.getSharedPreferences("UserInfo", MODE_PRIVATE);
            String userid = shp.getString("UserId", "none");

            if (userid.equals("none") || userid.trim().equals("")) {
                intent = new Intent(getApplicationContext(), LoginActivity.class);
            } else {
                ((MainModule) this.getApplication()). setUserCode(userid);
                if (menu.equals("APP01")) {
                    intent = new Intent(getApplicationContext(), ApproveSMP_Activity.class);
                }  else {
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                }
            }

            //if there is no image
            if(imageUrl.equals("null")){
                //displaying small notification
                mNotificationManager.showSmallNotification(title, message, intent);
            }else{
                //if there is an image
                //displaying a big notification
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

}

