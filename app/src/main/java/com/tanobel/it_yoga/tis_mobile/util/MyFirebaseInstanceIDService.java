package com.tanobel.it_yoga.tis_mobile.util;

/**
 * Created by IT_Yoga on 23/10/2018.
 */

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Created by Belal on 03/11/16.
 */


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIIDService";


    public void onTokenRefresh() {

        //Getting registration token
        final String[] refreshedToken = {null};
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                refreshedToken[0] = task.getResult();
            }
        });

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken[0]);

        //calling the method store token and passing token
        storeToken(refreshedToken[0]);
    }

    private void storeToken(String token) {
        //we will save the token in sharedpreferences later
        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
    }
}
