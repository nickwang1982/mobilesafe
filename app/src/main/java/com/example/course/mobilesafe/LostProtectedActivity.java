package com.example.course.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.course.mobilesafe.utils.Md5Encoder;


public class LostProtectedActivity extends Activity implements View.OnClickListener{

    private SharedPreferences sp;

    // items for first time dailog
    private EditText et_first_dialog_pwd;
    private EditText et_first_dialog_pwd_confirm;
    private Button bt_first_dialog_ok;
    private Button bt_first_dialog_cancel;

    // items for normal dailog
    private EditText et_normal_dialog_pwd;
    private Button bt_normal_ok;
    private Button bt_normal_cancel;
    private AlertDialog alertDialog;


    private TextView tv_lost_protect_number;
    private RelativeLayout rl_lost_protect_setting;
    private CheckBox cb_lost_protect_setting;
    private TextView tv_lost_protect_reentry_setup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        sp = getSharedPreferences("config" , MODE_PRIVATE);
        if (isSetupPwd()) {
            showNormalEntryDialog();
        } else {
            showFirstEntryDialog();
        }
    }

    private void showFirstEntryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = View.inflate(this, R.layout.first_entry_dialog, null);
        et_first_dialog_pwd = (EditText) view
                .findViewById(R.id.et_first_dialog_pwd);
        et_first_dialog_pwd_confirm = (EditText) view
                .findViewById(R.id.et_first_dialog_pwd_confirm);
        bt_first_dialog_ok = (Button) view
                .findViewById(R.id.bt_first_dialog_ok);
        bt_first_dialog_cancel = (Button) view
                .findViewById(R.id.bt_first_dialog_cancel);
        bt_first_dialog_cancel.setOnClickListener(this);
        bt_first_dialog_ok.setOnClickListener(this);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void showNormalEntryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });

        View view = View.inflate(this, R.layout.normal_entry_layout, null);

        et_normal_dialog_pwd = (EditText) view.findViewById(R.id.et_normal_dialog_pwd);
        bt_normal_ok = (Button) view.findViewById(R.id.bt_normal_dialog_ok);
        bt_normal_cancel = (Button) view.findViewById(R.id.bt_normal_dialog_cancel);

        bt_normal_ok.setOnClickListener(this);
        bt_normal_cancel.setOnClickListener(this);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_first_dialog_cancel:
                alertDialog.cancel();
                finish();
                break;
            case R.id.bt_first_dialog_ok:
                String pwd = et_first_dialog_pwd.getText().toString().trim();
                String pwd_confirm = et_first_dialog_pwd_confirm.getText()
                        .toString().trim();
                if (TextUtils.isEmpty(pwd_confirm) || TextUtils.isEmpty(pwd)) {
                    Toast.makeText(this, "Password is null !!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Compare two edit text contents
                if (pwd.equals(pwd_confirm)) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("password", Md5Encoder.encode(pwd));
                    editor.commit();

                    alertDialog.dismiss();
                    finish();
                } else {
                    Toast.makeText(this, "password is different", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;

            case R.id.bt_normal_dialog_cancel:
                alertDialog.cancel();
                finish();
                break;
            case R.id.bt_normal_dialog_ok:
                String userentrypwd = et_normal_dialog_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(userentrypwd)) {
                    Toast.makeText(this, "password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String savedpwd = sp.getString("password", null);
                if (savedpwd.equals(Md5Encoder.encode(userentrypwd))) {

                    Toast.makeText(this, "password correct !", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
//                    Intent intent = new Intent(this, Setup1Activity.class);
//                    startActivity(intent);
                    if(isSetupAlready()){

                        setContentView(R.layout.lost_protected);

                        tv_lost_protect_number = (TextView) findViewById(R.id.tv_lost_protect_number);
                        String safenumber = sp.getString("safenumber", "");
                        tv_lost_protect_number.setText(safenumber);

                        rl_lost_protect_setting = (RelativeLayout)findViewById(R.id.rl_lost_protect_setting);

                        cb_lost_protect_setting = (CheckBox)findViewById(R.id.cb_lost_protect_setting);
                        boolean protecting = sp.getBoolean("protecting", false);
                        cb_lost_protect_setting.setChecked(protecting);
                        if(protecting){
                            cb_lost_protect_setting.setText("Protection ON");
                        }else{
                            cb_lost_protect_setting.setText("Protection OFF");
                        }

                        tv_lost_protect_reentry_setup = (TextView)findViewById(R.id.tv_lost_protect_reentry_setup);

                        rl_lost_protect_setting.setOnClickListener(this);
                        tv_lost_protect_reentry_setup.setOnClickListener(this);

                    }else{

                        Intent intent = new Intent(this,Setup1Activity.class);

                        finish();
                        startActivity(intent);
                    }


                    return;
                } else {
                    Toast.makeText(this, "password incorrectly !", Toast.LENGTH_SHORT).show();
                    return;
                }
            case R.id.tv_lost_protect_reentry_setup:
                Intent reentryIntent = new Intent(this,Setup1Activity.class);
                startActivity(reentryIntent);
                finish();
                break;
            case R.id.rl_lost_protect_setting:
                SharedPreferences.Editor editor =	sp.edit();
                if(cb_lost_protect_setting.isChecked()){
                    cb_lost_protect_setting.setChecked(false);
                    cb_lost_protect_setting.setText("Protection OFF");
                    editor.putBoolean("protecting", false);

                }else{
                    cb_lost_protect_setting.setChecked(true);
                    cb_lost_protect_setting.setText("Protection ON");
                    editor.putBoolean("protecting", true);
                }
                editor.commit();
                break;
        }
    }

    private boolean isSetupAlready() {
        return sp.getBoolean("issetup", false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, "Change name");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final EditText et = new EditText(this);
            et.setHint("Change your app name");

            builder.setView(et);
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String newname = et.getText().toString().trim();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("newname", newname);
                            editor.commit();
                        }
                    });
            builder.create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isSetupPwd() {
        String savedPwd = null;
        savedPwd = sp.getString("password" , "");
        if (!TextUtils.isEmpty(savedPwd)) {
            return true;
        } else {
            return false;
        }
    }
}
