package com.example.course.mobilesafe.service;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.example.course.mobilesafe.db.dao.BlackNumberDao;

public class CallFirewallService extends Service {
	public static final String TAG = "CallFirewallService";
	private TelephonyManager tm;
	private MyPhoneListener listener;
	private BlackNumberDao dao;
//	private long  starttime;
//	private long endtime;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dao = new BlackNumberDao(this);
		listener = new MyPhoneListener();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

	}

	private class MyPhoneListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				//starttime = System.currentTimeMillis();
				int mode = dao.findNumberMode(incomingNumber);
				if (mode == 0 || mode == 2) {
					Log.i(TAG, "Hang up");
					endcall(incomingNumber);
				}
				break;

			case TelephonyManager.CALL_STATE_IDLE: // Call idle state
				break;

			case TelephonyManager.CALL_STATE_OFFHOOK:// Call connected state
				break;
			}

			super.onCallStateChanged(state, incomingNumber);
		}

	}

	/**
	 * Cancel listener
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
		
	}


	/**
	 * Hang up
	 * Needs two aidl files
	 * Needs add <uses-permission android:name="android.permission.CALL_PHONE" />
	 * @param incomingNumber
	 */
	public void endcall(String incomingNumber) {
		try {
			// use reflector to get system method
			Method method = Class.forName("android.os.ServiceManager")
					.getMethod("getService", String.class);
			IBinder binder = (IBinder) method.invoke(null,
					new Object[] { TELEPHONY_SERVICE });
			// use aidl invoke method
			ITelephony telephony = ITelephony.Stub.asInterface(binder);
			telephony.endCall();
			
			//deleteCallLog(incomingNumber);

			getContentResolver().registerContentObserver(
					CallLog.Calls.CONTENT_URI, true, new MyObserver(new Handler(), incomingNumber));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private class MyObserver extends ContentObserver {
		private String incomingNumber;
		public MyObserver(Handler handler, String incomingNumber) {
			super(handler);
			this.incomingNumber = incomingNumber;
		}
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			// delete
			deleteCallLog(incomingNumber);
			getContentResolver().unregisterContentObserver(this);
		}
	}

	/**
	 * Clear call log
	 * 
	 * @param incomingNumber
	 */
	private void deleteCallLog(String incomingNumber) {

		Uri uri = Uri.parse("content://call_log/calls");
		// CallLog.Calls.CONTENT_URI;
		Cursor cursor = getContentResolver().query(uri, new String[] { "_id" },
				"number=?", new String[] { incomingNumber }, null);
		while (cursor.moveToNext()) {
			String id = cursor.getString(0);
			getContentResolver().delete(uri, "_id=?", new String[] { id });
		}
		cursor.close();
	}
}
