package com.example.course.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

/**
 * Receive system boot complete status.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean protecting = sp.getBoolean("protecting" , false);
        if (protecting) {
            String safemuber = sp.getString("safenumber", "");
            // See whether the sim card number is the same with current one
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String realsim = tm.getSimSerialNumber();
            String savedSim = sp.getString("simserial", "");
            if (!savedSim.equals(realsim)) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(safemuber, null, "SIM card changed" ,null, null);
            }
        }
    }
}
