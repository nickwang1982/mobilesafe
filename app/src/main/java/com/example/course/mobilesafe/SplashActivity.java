package com.example.course.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.course.mobilesafe.domain.UpdateInfo;
import com.example.course.mobilesafe.engine.UpdateInfoParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class SplashActivity extends Activity {

    private TextView tv_splash_version;
    private RelativeLayout rl_splash;

    // Define server connection status
    private static final int GET_INFO_SUCCESS = 10;
    private static final int SERVER_ERROR = 11;
    private static final int SERVER_URL_ERROR = 12;
    private static final int PROTOCOL_ERROR = 13;
    private static final int IO_ERROR = 14;
    private static final int XML_PARSE_ERROR = 15;

    protected static final String TAG = "SplashActivity";

    private UpdateInfo info;
    private long startTime;
    private long endTime;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SERVER_ERROR:
                    Toast.makeText(getApplicationContext(), "Server error!", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case SERVER_URL_ERROR:
                    Toast.makeText(getApplicationContext(), "Server url address error!",
                            Toast.LENGTH_SHORT).show();
                    break;
                case PROTOCOL_ERROR:
                    Toast.makeText(getApplicationContext(), "Unsupported protocol!", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case IO_ERROR:
                    Toast.makeText(getApplicationContext(), "IO error!", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case XML_PARSE_ERROR:
                    Toast.makeText(getApplicationContext(), "XML parser error!", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case GET_INFO_SUCCESS:
                    String serverVerison = info.getVersion();
                    String currentVersion = getVersion();
                    if (!serverVerison.equals(currentVersion)) {
                        showUpdateDialog();
                    } else {
                        Toast.makeText(getApplicationContext(), "This is current Version", Toast.LENGTH_LONG);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set as no title window
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set as full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
        tv_splash_version = (TextView)findViewById(R.id.tv_splash_version);
        tv_splash_version.setText(getString(R.string.label_version) + getVersion());

        AlphaAnimation aa = new AlphaAnimation(0.3f , 1.0f);
        aa.setDuration(2000);
        rl_splash.startAnimation(aa);

        // Connect to server to get latest version info
        new Thread(new CheckVersionTask()){

        }.start();

    }

    private class CheckVersionTask implements Runnable {

        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            Message msg = Message.obtain();
            try {
                String serverurl = getResources().getString(R.string.serverurl);
                URL url = new URL(serverurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                int code = conn.getResponseCode();
                if (code == 200) {
                    // Response code is 200, means connect successfully.
                    InputStream is = conn.getInputStream();
                    info = UpdateInfoParser.getUpdatedInfo(is);
                    endTime = System.currentTimeMillis();
                    long resultTime = endTime - startTime;
                    if (resultTime < 2000) {
                        try {
                            Thread.sleep(2000 - resultTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    msg.what = GET_INFO_SUCCESS;
                    handler.sendMessage(msg);
                } else {
                    // Server status error:
                    msg.what = SERVER_ERROR;
                    handler.sendMessage(msg);
                    endTime = System.currentTimeMillis();
                    long resultTime = endTime - startTime;
                    if ( resultTime < 2000 ) {
                        try {
                            Thread.sleep( 2000 - resultTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                msg.what = SERVER_URL_ERROR;
                handler.sendMessage(msg);
            } catch (ProtocolException e) {
                e.printStackTrace();
                msg.what = PROTOCOL_ERROR;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
                msg.what = IO_ERROR;
                handler.sendMessage(msg);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                msg.what = XML_PARSE_ERROR;
                handler.sendMessage(msg);
            }

        }
    }

    protected void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(getResources().getDrawable(R.drawable.notification));
        builder.setTitle("Update app");
        builder.setMessage(info.getDescription());
        builder.setPositiveButton("Update" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
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
