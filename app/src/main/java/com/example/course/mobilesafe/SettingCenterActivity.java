package com.example.course.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.course.mobilesafe.service.ShowCallLocationService;
import com.example.course.mobilesafe.utils.ServiceStatusUtil;


public class SettingCenterActivity extends Activity implements View.OnClickListener {

    // Whether auto update is on
    private SharedPreferences sp;
    // Auto Update text to show
    private TextView tv_setting_autoupdate_status;
    // Checkbox for user
    private CheckBox cb_stting_autoupdate;


    private TextView tv_setting_show_location_status;
    private CheckBox cb_setting_show_location;
    private RelativeLayout rl_setting_show_location;
    private Intent showLocationIntent;
    private RelativeLayout rl_setting_change_bg;
    private TextView tv_setting_show_bg;
    private RelativeLayout rl_setting_change_location;

    private TextView tv_setting_app_lock_status;
    private CheckBox cb_setting_applock;
    private RelativeLayout rl_setting_app_lock;
    private Intent watchDogIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting_center);


        sp = getSharedPreferences("config", MODE_PRIVATE);

        cb_stting_autoupdate = (CheckBox)findViewById(R.id.cb_setting_autoupdate);
        tv_setting_autoupdate_status = (TextView)findViewById(R.id.tv_setting_autoupdate_status);

        // init UI, by default set auto update to on
        boolean autoupdate = sp.getBoolean("autoupdate" , true);
        if (autoupdate) {
            tv_setting_autoupdate_status.setText("Auto Update is ON");
            cb_stting_autoupdate.setChecked(true);
        } else {
            tv_setting_autoupdate_status.setText("Auto Update is OFF");
            cb_stting_autoupdate.setChecked(false);
        }
        // set checkbox listener
        cb_stting_autoupdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("autoupdate" , isChecked);
                editor.commit();
                if (isChecked) {
                    tv_setting_autoupdate_status.setText("Auto Update is ON");
                } else {
                    tv_setting_autoupdate_status.setText("Auto Update is OFF");
                }
            }
        });

        tv_setting_show_location_status = (TextView) findViewById(R.id.tv_setting_show_location_status);
        cb_setting_show_location = (CheckBox) findViewById(R.id.cb_setting_show_location);
        rl_setting_show_location = (RelativeLayout) findViewById(R.id.rl_setting_show_location);
        showLocationIntent = new Intent(this, ShowCallLocationService.class);

        rl_setting_show_location.setOnClickListener(this);

        rl_setting_change_bg = (RelativeLayout) findViewById(R.id.rl_setting_change_bg);
        tv_setting_show_bg = (TextView) findViewById(R.id.tv_setting_show_bg);

        rl_setting_change_bg.setOnClickListener(this);

        rl_setting_change_location = (RelativeLayout) findViewById(R.id.rl_setting_change_location);
        rl_setting_change_location.setOnClickListener(this);
    }


    @Override
    protected void onResume() {

        if (ServiceStatusUtil.isServiceRunning(this,
                "com.example.course.mobilesafe.service.ShowCallLocationService")) {
            cb_setting_show_location.setChecked(true);
            tv_setting_show_location_status.setText("Incomming call location ON");
        } else {
            cb_setting_show_location.setChecked(false);
            tv_setting_show_location_status.setText("Incomming call location OFF");
        }

        super.onResume();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.rl_setting_show_location:
                if (cb_setting_show_location.isChecked()) {
                    tv_setting_show_location_status.setText("Call location Off");
                    stopService(showLocationIntent);
                    cb_setting_show_location.setChecked(false);
                } else {
                    tv_setting_show_location_status.setText("Call location ON");
                    startService(showLocationIntent);
                    cb_setting_show_location.setChecked(true);
                }
                break;
            case R.id.rl_setting_change_bg:
                showChooseBgDialog();
                break;
            case R.id.rl_setting_change_location:
                Intent intent = new Intent(this, DragViewActivity.class);
                startActivity(intent);
                break;

        }
    }

	private void showChooseBgDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.notification);
		builder.setTitle("Location toast style");
		final String[] items = { "Translucent", "Orange", "Blue", "Green", "Gray" };
		int which = sp.getInt("which", 0);

		builder.setSingleChoiceItems(items, which,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences.Editor editor = sp.edit();
						editor.putInt("which", which);
						editor.commit();
						tv_setting_show_bg.setText(items[which]);
						dialog.dismiss();
					}
				});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create().show();
	}
}
