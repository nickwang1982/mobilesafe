package com.example.course.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


public class SettingCenterActivity extends Activity {

    // Whether auto update is on
    private SharedPreferences sp;
    // Auto Update text to show
    private TextView tv_setting_autoupdate_status;
    // Checkbox for user
    private CheckBox cb_stting_autoupdate;

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
    }


}
