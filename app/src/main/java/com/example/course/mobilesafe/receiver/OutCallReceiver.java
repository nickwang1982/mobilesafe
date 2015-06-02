package com.example.course.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.course.mobilesafe.LostProtectedActivity;

/**
 * Out call receiver for enter mobile safe function
 */
public class OutCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the broadcast data
        String outnumber = getResultData();
        // Set the number which allow user to enter this function
        String enterPhoneBakNumber = "110" ;
        // See whether reciver number is the one
        if (enterPhoneBakNumber.equals(outnumber)) {
            Intent lostIntent = new Intent(context, LostProtectedActivity.class);
            lostIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(lostIntent);
            setResultData(null);
        }
    }
}
