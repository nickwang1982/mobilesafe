package com.example.course.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.course.mobilesafe.domain.AppInfo;
public class AppInfoProvider {
	private PackageManager pm;
	public AppInfoProvider(Context context) {
		pm = context.getPackageManager();
	}
	/**
	 * Get all installed application info
	 * @return
	 */
	public List<AppInfo> getInstalledApps(){
		// Return all info 
		// PackageManager.GET_UNINSTALLED_PACKAGES: including all uninstalled but not clear data apps.
		List<PackageInfo> packageinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		List<AppInfo> appinfos = new ArrayList<AppInfo>();
		for(PackageInfo info : packageinfos){
			AppInfo appinfo = new AppInfo();
			appinfo.setPackname(info.packageName);
			appinfo.setVersion(info.versionName);
			appinfo.setAppicon(info.applicationInfo.loadIcon(pm));
			appinfo.setAppname(info.applicationInfo.loadLabel(pm).toString());
			// Filter all 3rd part applications
			appinfo.setUserapp(filterApp(info.applicationInfo));
			appinfos.add(appinfo);
			appinfo = null;
		}
		return appinfos;
	}
	
	/**
	 * Filter for 3rd part applications,
	 * @param info
	 * @return true 3rd part application
	 *         false System app
	 */
    public boolean filterApp(ApplicationInfo info) {
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }
}
