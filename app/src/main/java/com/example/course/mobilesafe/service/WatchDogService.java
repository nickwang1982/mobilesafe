package com.example.course.mobilesafe.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.course.mobilesafe.EnterPwdActivity;
import com.example.course.mobilesafe.IService;
import com.example.course.mobilesafe.db.dao.AppLockDao;

public class WatchDogService extends Service {
	protected static final String TAG = "WatchDogService";
	// Flag to check whether watch dog needs to run, true for running, false to stop.
	boolean flag;
	// password input UI intent
	private Intent pwdintent;
	// Cach all package names
	private List<String> lockPacknames;

	private AppLockDao dao;

	private List<String> tempStopProtectPacknames;

	private MyBinder binder;
	private MyObserver observer;
	// receive Sreen lock event
	private LockScreenReceiver receiver;
	@Override
	public IBinder onBind(Intent intent) {
		binder = new MyBinder();
		return binder;
	}
	private class MyBinder extends Binder implements IService{
		public void callTempStopProtect(String packname) {
			tempStopProtect(packname);
		}
	}

	public void tempStopProtect(String packname){
		tempStopProtectPacknames.add(packname);
	}
	@Override
	public void onCreate() {
		Uri uri = Uri.parse("content://com.example.course.applock/");
		observer = new MyObserver(new Handler());
		// Second param is true, 
		// content://com.example.course.applock/ match to observe
		// No needs to match "ADD/DELETE"
		getContentResolver().registerContentObserver(uri, true, observer);
		// Regester a broadcast
		IntentFilter filter = new IntentFilter();
		filter.setPriority(1000);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		receiver = new LockScreenReceiver();
		registerReceiver(receiver, filter);
		
		super.onCreate();
		dao = new AppLockDao(this);
		// Start watch dog service, keep it running background
		flag = true;
		tempStopProtectPacknames = new ArrayList<String>();
		lockPacknames = dao.findAll();
		pwdintent = new Intent(this,EnterPwdActivity.class);
		// Service don't have task stack
		// So, needs to create new task stack for activity
		pwdintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Use new thread to running watch dog service
		new Thread() {
			public void run() {
				// Loop while true
				while (flag){
					// Try to see all process running use Activity Manager
					ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
					// Get Top Activity from back stack
					RunningTaskInfo taskinfo = am.getRunningTasks(1).get(0);
					// Get the package name according to tope task  activity
					String packname = taskinfo.topActivity.getPackageName();
					Log.i(TAG,packname);
					// Check this app name in temp stop protect list
					if(tempStopProtectPacknames.contains(packname)){
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// the package is temp protected app, skip from while loop
						continue;
					}
					// Store the package name in intent
					// Use getIntent to get this object
					pwdintent.putExtra("packname", packname);
					// Check the app included in lock application list
					if(lockPacknames.contains(packname)){
						// Enter password input UI
						startActivity(pwdintent);
					}
					try {
						// Watch dog is power consuming programe, sleep 200 for this service
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
	// Clear all resources
	@Override
	public void onDestroy() {
		flag = false;
		getContentResolver().unregisterContentObserver(observer);
		observer = null;
		unregisterReceiver(receiver);
		super.onDestroy();
	}
	private class MyObserver extends ContentObserver{
		public MyObserver(Handler handler) {
			super(handler);
		}
		@Override
		public void onChange(boolean selfChange) {
			lockPacknames = dao.findAll();
			super.onChange(selfChange);
		}
	}
	private class LockScreenReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG,"Locked screen");
			tempStopProtectPacknames.clear();
		}
		
	}
}
