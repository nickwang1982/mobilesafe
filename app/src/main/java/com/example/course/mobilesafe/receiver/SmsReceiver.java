package com.example.course.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.example.course.mobilesafe.R;
import com.example.course.mobilesafe.db.dao.BlackNumberDao;
import com.example.course.mobilesafe.engine.GPSInfoProvider;

/**
 * To receive the SMS command from security number
 */
public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";
    private SharedPreferences sp;
    private BlackNumberDao dao;

    @Override
    public void onReceive(Context context, Intent intent) {
        sp = context.getSharedPreferences("config" , Context.MODE_PRIVATE);
        String safenumber = sp.getString("safenumber", "");
        Object[] objs = (Object[]) intent.getExtras().get("pdus");

        DevicePolicyManager dm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

        ComponentName mAdminName = new ComponentName(context, MyAdmin.class);


        for (Object obj : objs) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            String sender = smsMessage.getOriginatingAddress();
			int result = dao.findNumberMode(sender);
			if(result==1||result==2){// Check whether this message needs to block
				Log.i(TAG,"Block black list message");
				abortBroadcast();
			}
            String body = smsMessage.getMessageBody();
            if  ("#*location*#".equals(body)) {
                Log.i(TAG, "Sending location info");

                String lastlocation = GPSInfoProvider.getInstance(context).getLocation();
                if(!TextUtils.isEmpty(lastlocation)) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(safenumber, null, lastlocation, null, null);
                }
                abortBroadcast();
            } else if ("#*alarm*#".equals(body)) {
                Log.i(TAG, "Sending Alarm info");
                MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                player.setVolume(1.0f, 1.0f);
                player.start();
                abortBroadcast();
            } else if ("#*wipedata*#".equals(body)) {
                Log.i(TAG, "Wipe data");
                if(dm.isAdminActive(mAdminName)) {
                    dm.wipeData(0);
                }
                abortBroadcast();
            } else if ("#*lockscreen*#".equals(body)) {
                Log.i(TAG, "Remote Lock");
                if(dm.isAdminActive(mAdminName)) {
                    dm.resetPassword("123" , 0) ;
                    dm.lockNow();
                }
                abortBroadcast();
            }
        }
    }
}
