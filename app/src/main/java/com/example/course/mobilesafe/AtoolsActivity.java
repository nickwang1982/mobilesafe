package com.example.course.mobilesafe;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.course.mobilesafe.utils.AssetCopyUtil;

public class AtoolsActivity extends Activity implements OnClickListener {
	protected static final int COPY_SUCCESS = 30;
	protected static final int COPY_FAILED = 31;
	protected static final int COPY_COMMON_NUMBER_SUCCESS = 32;
	private TextView tv_atools_address_query;// Click this item, copy db files
	private TextView tv_atools_common_num;//
	private TextView tv_atools_applock;
	private ProgressDialog pd;// progress update
	// Copy db file is time consuming work, use handler to update Main UI
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// No matter success or not, just lose progress dialog.
			pd.dismiss();
			switch (msg.what) {
			case COPY_SUCCESS:
				// Success, go to Query UI
				loadQueryUI();
				break;
			case COPY_COMMON_NUMBER_SUCCESS:
//				loadCommNumUI();
				break;
			case COPY_FAILED:
				Toast.makeText(getApplicationContext(), "Copy failed", Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.atools);
		tv_atools_address_query = (TextView) findViewById(R.id.tv_atools_address_query);
		tv_atools_address_query.setOnClickListener(this);
		pd = new ProgressDialog(this);
		tv_atools_common_num = (TextView) findViewById(R.id.tv_atools_common_num);
		tv_atools_common_num.setOnClickListener(this);
		tv_atools_applock = (TextView)findViewById(R.id.tv_atools_applock);
		tv_atools_applock.setOnClickListener(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_atools_address_query:
			// Create target file name : data\data\<package Name>\files\address.db
			final File file = new File(getFilesDir(), "address.db");
			if (file.exists() && file.length() > 0) {
				// file exits, go to Query UI
				loadQueryUI();
			} else {
				// Start to copy
				pd.show();
				// Create sub back groud thread to do file copy
				new Thread() {
					public void run() {
						AssetCopyUtil asu = new AssetCopyUtil(
								getApplicationContext());
						// return result
						boolean result = asu.copyFile("naddress.db", file, pd);
						if (result) {
							Message msg = Message.obtain();
							msg.what = COPY_SUCCESS;
							handler.sendMessage(msg);
						} else {
							Message msg = Message.obtain();
							msg.what = COPY_FAILED;
							handler.sendMessage(msg);
						}
					};
				}.start();
			}
			break;
		case R.id.tv_atools_common_num:

			final File commonnumberfile = new File(getFilesDir(),
					"commonnum.db");
			if (commonnumberfile.exists() && commonnumberfile.length() > 0) {
//				loadCommNumUI();
			} else {
				pd.show();
				new Thread() {
					public void run() {
						AssetCopyUtil asu = new AssetCopyUtil(
								getApplicationContext());
						boolean result = asu.copyFile("commonnum.db",
								commonnumberfile, pd);
						if (result) {
							Message msg = Message.obtain();
							msg.what = COPY_COMMON_NUMBER_SUCCESS;
							handler.sendMessage(msg);
						} else {
							Message msg = Message.obtain();
							msg.what = COPY_FAILED;
							handler.sendMessage(msg);
						}
					};
				}.start();
			}
			break;
//		case R.id.tv_atools_applock:
//			Intent applockIntent = new Intent(this,AppLockActivity.class);
//			startActivity(applockIntent);
//			break;
		}
	}

	/**
	 * Enter common number UI
	 */
//	private void loadCommNumUI() {
//		Intent intent = new Intent(this, CommonNumActivity.class);
//		startActivity(intent);
//	}

	/**
	 * Enter query UI
	 */
	private void loadQueryUI() {
		Intent intent = new Intent(this, NumberQueryActivity.class);
		startActivity(intent);
	}
}
