package com.example.course.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.course.mobilesafe.db.dao.BlackNumberDao;
import com.example.course.mobilesafe.domain.BlackNumber;

public class CallSmsSafeActivity extends Activity {
	protected static final int LOAD_DATA_FINISH = 40;
	public static final String TAG = "CallSmsSafeActivity";
	private ListView lv_call_sms_safe;
	private BlackNumberDao dao;
	private List<BlackNumber> blacknumbers;
	private BlackNumberAdapter adpater;
	private LinearLayout ll_call_sms_safe_loading;
	
	//private String initnumber;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_DATA_FINISH:// Finish loading data from DB
				ll_call_sms_safe_loading.setVisibility(View.INVISIBLE);
				adpater = new BlackNumberAdapter();
				lv_call_sms_safe.setAdapter(adpater);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_sms_safe);
		ll_call_sms_safe_loading = (LinearLayout) findViewById(R.id.ll_call_sms_safe_loading);
		dao = new BlackNumberDao(this);
		lv_call_sms_safe = (ListView) findViewById(R.id.lv_call_sms_safe);
		
		ll_call_sms_safe_loading.setVisibility(View.VISIBLE);
				
		/*Intent intent  = getIntent();// Get intent info
		initnumber = intent.getStringExtra("blacknumber");
		Log.i(TAG,"initnumber:"+initnumber);
		if(initnumber!=null){
			showBlackNumberDialog(0, 0);
		}*/

		registerForContextMenu(lv_call_sms_safe);
		// Database query operation use sub thread
		new Thread() {
			public void run() {
				blacknumbers = dao.findAll();

				Message msg = Message.obtain();
				msg.what = LOAD_DATA_FINISH;
				handler.sendMessage(msg);
			};
		}.start();
	}

	/*@Override
	protected void onNewIntent(Intent intent) {
		initnumber = intent.getStringExtra("blacknumber");
		Log.i(TAG,"initnumber:"+initnumber);
		if(initnumber!=null){
			showBlackNumberDialog(0, 0);
		}
		super.onNewIntent(intent);
	}*/
	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.call_sms_safe_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = (int) info.id; 
		switch (item.getItemId()) {
		case R.id.item_delete:
			Log.i(TAG, "Delete black number");
			deleteBlackNumber(position);
			return true;
		case R.id.item_update:
			Log.i(TAG, "Updata black number");
			updateBlackNumber(position);

			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	/**
	 *  Update black number
	 * 
	 * @param position
	 */
	private void updateBlackNumber(int position) {
		showBlackNumberDialog(1, position);
	}

	/**
	 * Delete black number
	 * 
	 * @param position
	 */
	private void deleteBlackNumber(int position) {
		BlackNumber blackNumber = (BlackNumber) lv_call_sms_safe
				.getItemAtPosition(position);
		String number = blackNumber.getNumber();
		dao.delete(number); 
		blacknumbers.remove(blackNumber); 
		adpater.notifyDataSetChanged();
	}

	private class BlackNumberAdapter extends BaseAdapter {

		public int getCount() {
			return blacknumbers.size();
		}

		public Object getItem(int position) {
			return blacknumbers.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if (convertView == null) {

				view = View.inflate(getApplicationContext(),
						R.layout.call_sms_item, null);
				holder = new ViewHolder();
				holder.tv_number = (TextView) view
						.findViewById(R.id.tv_callsms_item_number);
				holder.tv_mode = (TextView) view
						.findViewById(R.id.tv_callsms_item_mode);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			BlackNumber blacknumber = blacknumbers.get(position);
			holder.tv_number.setText(blacknumber.getNumber());
			int mode = blacknumber.getMode();
			if (mode == 0) {
				holder.tv_mode.setText("Tel Block");
			} else if (mode == 1) {
				holder.tv_mode.setText("SMS Block");
			} else {
				holder.tv_mode.setText("All Block");
			}
			return view;
		}
	}
	
	private static class ViewHolder {
		TextView tv_number;
		TextView tv_mode;
	}


	public void addBlackNumber(View view) {
		showBlackNumberDialog(0, 0);
	}

	/**
	 * Add/Update dailog
	 * 
	 * @param flag
	 *            0 add / 1 Modify
	 * @param position
	 *            
	 */
	private void showBlackNumberDialog(final int flag, final int position) {

		AlertDialog.Builder builder = new Builder(this);

		View dialogview = View.inflate(this, R.layout.add_black_number, null);

		final EditText et_number = (EditText) dialogview
				.findViewById(R.id.et_add_black_number);
		/*if(!TextUtils.isEmpty(initnumber)){
			et_number.setText(initnumber);
		}*/

		final CheckBox cb_phone = (CheckBox) dialogview
				.findViewById(R.id.cb_block_phone);
		final CheckBox cb_sms = (CheckBox) dialogview
				.findViewById(R.id.cb_block_sms);
		TextView tv_title = (TextView) dialogview
				.findViewById(R.id.tv_black_number_title);
		if (flag == 1) {
			tv_title.setText("Modify");

			BlackNumber blackNumber = (BlackNumber) lv_call_sms_safe
					.getItemAtPosition(position);
			String oldnumber = blackNumber.getNumber();
			et_number.setText(oldnumber);
			int m = blackNumber.getMode();

			if(m==0){//TEL Block
				cb_phone.setChecked(true);
				cb_sms.setChecked(false);
			}else if(m==1){//SMS Block
				cb_sms.setChecked(true);
				cb_phone.setChecked(false);
			}else{//All
				cb_phone.setChecked(true);
				cb_sms.setChecked(true);
			}
		}
		builder.setView(dialogview);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				String number = et_number.getText().toString().trim();

				if(flag==1&&dao.find(number)){
					Toast.makeText(getApplicationContext(), "The number already exist",Toast.LENGTH_SHORT).show();
					return ;
				}

				if (TextUtils.isEmpty(number)) {
					return;
				} else {
					boolean result = false;
					BlackNumber blacknumber = new BlackNumber();
					blacknumber.setNumber(number);

					if (cb_phone.isChecked() && cb_sms.isChecked()) {
						if (flag == 0) {
							result = dao.add(number, "2");
							blacknumber.setMode(2);
						} else {

							BlackNumber blackNumber = (BlackNumber) lv_call_sms_safe
									.getItemAtPosition(position);

							dao.update(blackNumber.getNumber(), number, "2");
							blackNumber.setMode(2);
							blackNumber.setNumber(number);

							adpater.notifyDataSetChanged();
						}
					} else if (cb_phone.isChecked()) {
						if (flag == 0) {
							result = dao.add(number, "0");
							blacknumber.setMode(0);
						} else {
							BlackNumber blackNumber = (BlackNumber) lv_call_sms_safe
									.getItemAtPosition(position);

							dao.update(blackNumber.getNumber(), number, "0");
							blackNumber.setMode(0);
							blackNumber.setNumber(number);

							adpater.notifyDataSetChanged();

						}
					} else if (cb_sms.isChecked()) {
						if (flag == 0) {
							result = dao.add(number, "1");
							blacknumber.setMode(1);
						}else{
							BlackNumber blackNumber = (BlackNumber) lv_call_sms_safe
									.getItemAtPosition(position);

							dao.update(blackNumber.getNumber(), number, "1");
							blackNumber.setMode(1);
							blackNumber.setNumber(number);

							adpater.notifyDataSetChanged();
						}
					} else {
						Toast.makeText(getApplicationContext(), "Block Type can't be null", Toast.LENGTH_SHORT)
								.show();
						return;
					}
					if (result) {
						blacknumbers.add(blacknumber);

						adpater.notifyDataSetChanged();
					}
				}
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create().show();
	}
}
