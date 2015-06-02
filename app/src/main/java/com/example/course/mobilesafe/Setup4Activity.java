package com.example.course.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
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

import com.example.course.mobilesafe.receiver.MyAdmin;


public class Setup4Activity extends Activity {
    private CheckBox cb_setup4_protect;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setup4);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        cb_setup4_protect = (CheckBox)findViewById(R.id.cb_setup4_protect);
        boolean protecting = sp.getBoolean("protecting", false);


        cb_setup4_protect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sp.edit();
                if(isChecked){
                    editor.putBoolean("protecting", true);
                    cb_setup4_protect.setText("Lost protection on");
                }else{
                    cb_setup4_protect.setText("Lost protection off");
                    editor.putBoolean("protecting", false);
                }
                editor.commit();
            }
        });
        cb_setup4_protect.setChecked(protecting);
    }

    public void activeDeviceAdmin(View view){

        ComponentName mAdminName = new ComponentName(this, MyAdmin.class);

        DevicePolicyManager dm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);

        if (!dm.isAdminActive(mAdminName)) {
            Intent intent = new Intent(
                    DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
            startActivity(intent);
        }
    }

    public void next(View view){
        if(!cb_setup4_protect.isChecked()){//����������û�п���������һ���Ի�����ʾ��������
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Hint");
            builder.setMessage("Highly recommend to open lost protection");
            builder.setPositiveButton("ON", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    cb_setup4_protect.setChecked(true);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("issetup", true);
                    editor.commit();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("issetup", true);
                    editor.commit();
                }
            });
            builder.create().show();


            return ;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("issetup", true);
        editor.commit();

        Intent intent = new Intent(this,LostProtectedActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }
    public void pre(View view){
        Intent intent = new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

}
