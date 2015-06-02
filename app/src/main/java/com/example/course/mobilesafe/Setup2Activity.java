package com.example.course.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class Setup2Activity extends Activity  implements View.OnClickListener {
    private RelativeLayout rl_setup2_bind;
    private ImageView iv_setup2_bind_status;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setup2);
        rl_setup2_bind = (RelativeLayout) findViewById(R.id.rl_setup2_bind);
        rl_setup2_bind.setOnClickListener(this);
        iv_setup2_bind_status = (ImageView) findViewById(R.id.iv_setup2_bind_status);
        sp = getSharedPreferences("config", MODE_PRIVATE);

        String simseral = sp.getString("simserial", "");
        if(TextUtils.isEmpty(simseral)){
            iv_setup2_bind_status.setImageResource(R.drawable.switch_off_normal);
        }else{
            iv_setup2_bind_status.setImageResource(R.drawable.switch_on_normal);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_setup2_bind:

                String simseral = sp.getString("simserial", "");
                if(TextUtils.isEmpty(simseral)){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("simserial", getSimSerial());
                    editor.commit();

                    iv_setup2_bind_status.setImageResource(R.drawable.switch_on_normal);
                }else{
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("simserial", "");
                    editor.commit();
                    iv_setup2_bind_status.setImageResource(R.drawable.switch_off_normal);
                }

                break;
        }
    }
    private String getSimSerial(){

        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        return tm.getSimSerialNumber();
    }

    public void next(View view){

        Intent intent = new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    public void pre(View view){
        Intent intent = new Intent(this,Setup1Activity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }
}
