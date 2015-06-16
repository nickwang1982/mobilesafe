package com.example.course.mobilesafe;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.course.mobilesafe.service.WatchDogService;

public class EnterPwdActivity extends Activity {
	private EditText et_password;
	private TextView tv_name;
	private ImageView iv_icon;
	private Intent serviceIntent;
	private IService iService;
	private MyConn conn;
	private String packname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_pwd);
		et_password = (EditText) findViewById(R.id.et_password);
		Intent intent = getIntent();
		// Get data from intent (The package name needs to unlock to enter)
		packname = intent.getStringExtra("packname");
		tv_name = (TextView) findViewById(R.id.tv_enterpwd_name);
		iv_icon = (ImageView) findViewById(R.id.iv_enterpwd_icon);
		serviceIntent = new Intent(this,WatchDogService.class);
		conn = new MyConn();
		// use bind service not start service
		bindService(serviceIntent, conn, BIND_AUTO_CREATE);
		
		try {
			PackageInfo info = getPackageManager().getPackageInfo(packname, 0);
			//info.applicationInfo.loadLabel(getPackageManager()) 
			tv_name.setText(info.applicationInfo.loadLabel(getPackageManager()));
			//info.applicationInfo.loadIcon(getPackageManager())
			iv_icon.setImageDrawable(info.applicationInfo.loadIcon(getPackageManager()));
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private class MyConn implements ServiceConnection{
		public void onServiceConnected(ComponentName name, IBinder service) {
			// Ibinder implement IService interface
			iService = (IService) service;
		}
		// When service is crashed or connection lost.
		public void onServiceDisconnected(ComponentName name) {
			
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(conn);
	}
	/**
	 * Click enter 
	 */
	public void enterPassword(View view){

		String pwd = et_password.getText().toString().trim();
		if(TextUtils.isEmpty(pwd)){
			Toast.makeText(this, "Password can't be null", Toast.LENGTH_SHORT).show();
			return ;
		}
		// Check password: Here use 123 as default password. 
		if("123".equals(pwd)){
			// Service stop protect this ap
			iService.callTempStopProtect(packname);
			/*Intent intent = new Intent();
			intent.setAction("cn.itcast.mobilesafe.stopprotect");
			intent.putExtra("packname", packname);
			sendBroadcast(intent);*/
			finish();
			
		}else{
			Toast.makeText(this, "Password incorrectlly", Toast.LENGTH_SHORT).show();
			return ;
		}
	}
	
	/**
	 * When enter UI, block back key
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getAction()==KeyEvent.ACTION_DOWN&&event.getKeyCode()==KeyEvent.KEYCODE_BACK){
			return true;//Consume the back click
		}
		return super.onKeyDown(keyCode, event);
	}
}
