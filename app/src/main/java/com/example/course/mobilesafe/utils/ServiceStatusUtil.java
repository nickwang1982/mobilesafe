package com.example.course.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceStatusUtil {


	public static boolean isServiceRunning(Context context,String serviceClassName){
		ActivityManager  am  = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningServiceInfo>  infos = am.getRunningServices(100);
		for(RunningServiceInfo info: infos){
			if(serviceClassName.equals(info.service.getClassName())){
				return true;
			}
		}
		return false;
	}
}
