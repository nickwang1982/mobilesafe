package com.example.course.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.course.mobilesafe.R;
import com.example.course.mobilesafe.db.dao.NumberAddressDao;
    // Back ground service to listen call in
    public class ShowCallLocationService extends Service {
    private TelephonyManager tm;
	private MyPhoneListener listener;
	private WindowManager windowManager;
	private SharedPreferences sp;
	private static final  int[] bgs = {R.drawable.call_locate_white,R.drawable.call_locate_orange,
			R.drawable.call_locate_blue,R.drawable.call_locate_green,R.drawable.call_locate_gray};
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * Service create
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		sp =getSharedPreferences("config",MODE_PRIVATE);
		listener = new MyPhoneListener();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		// Set call listener, param 1 : listener , param 2 : listen call state.
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
	}

	private class MyPhoneListener extends PhoneStateListener {
		private View view;
		// int: state, string incoming call number
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// Call ringing
				// query the number location
				String address = NumberAddressDao.getAddress(incomingNumber);
                view = View.inflate(getApplicationContext(), R.layout.show_address, null);

				LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_show_address);
				int which = sp.getInt("which", 0);
				ll.setBackgroundResource(bgs[which]);

				TextView tv = (TextView) view.findViewById(R.id.tv_show_address);

				tv.setText(address);

	            final LayoutParams params = new LayoutParams();
	            params.gravity = Gravity.CENTER | Gravity.CENTER;

	            params.x = sp.getInt("lastx", 0);
	            params.y = sp.getInt("lasty", 0);
	            params.height = LayoutParams.WRAP_CONTENT;
	            params.width = LayoutParams.WRAP_CONTENT;
	            // View has no focus, no clickable, keep screen on
	            params.flags = LayoutParams.FLAG_NOT_FOCUSABLE
	                    | LayoutParams.FLAG_NOT_TOUCHABLE
	                    | LayoutParams.FLAG_KEEP_SCREEN_ON;
	            // window style is translucent
	            params.format = PixelFormat.TRANSLUCENT;
	            // View type is toast
	            params.type = LayoutParams.TYPE_TOAST;

				windowManager.addView(view, params);
				break;

			case TelephonyManager.CALL_STATE_IDLE:
				if(view!=null){
					windowManager.removeView(view);
					view = null;
				}
				break;

			case TelephonyManager.CALL_STATE_OFFHOOK: // call connected
				/*if(view!=null){
					windowManager.removeView(view);
					view = null;
				}*/
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}
	
	/**
	 * cancel listener
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
	}
}
