package com.example.course.mobilesafe;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class SplashActivity extends Activity {

    private TextView tv_splash_version;
    private RelativeLayout rl_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_splash_version.setText("Version : " + getVersion());

        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(2000);
        rl_splash.startAnimation(aa);
    }


    /**
     *  Get current application version
     */
    private String getVersion() {
        String currentVersion = null;
        // Use pacakge manager to get current string version
        PackageManager pm = this.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            currentVersion = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return currentVersion;
    }
}
