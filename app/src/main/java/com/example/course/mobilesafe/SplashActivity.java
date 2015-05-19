package com.example.course.mobilesafe;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class SplashActivity extends Activity {

    private TextView tv_splash_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set as no title window
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        tv_splash_version = (TextView)findViewById(R.id.tv_splash_version);
        tv_splash_version.setText(getString(R.string.label_version) + getVersion());
    }


    /**
     * Get current app version
     *
     * @return version
     */
    private String getVersion() {

        String currentVersion = null;
        // Use system package manager
        PackageManager pm = this.getPackageManager();

        try {
            // Param1: current application name
            // Param2: additional
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            // return current version
            currentVersion = info.versionName;

        } catch (Exception e) {
            // No package name found exception
            e.printStackTrace();
            return null;
        }
        return currentVersion;
    }
}
