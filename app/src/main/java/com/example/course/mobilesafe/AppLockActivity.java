package com.example.course.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.course.mobilesafe.db.dao.AppLockDao;
import com.example.course.mobilesafe.domain.AppInfo;
import com.example.course.mobilesafe.engine.AppInfoProvider;

public class AppLockActivity extends Activity {
	// List all applications
	private ListView lv_applock;
	// For progress and textview
	private LinearLayout ll_loading;
	// application informations
	private AppInfoProvider provider;
	// Applications list
	private List<AppInfo> appinfos;
	// for data access of locked app
	private AppLockDao dao;
	private List<String> lockedPacknames;
	// Handle all application info from sub thread
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			ll_loading.setVisibility(View.INVISIBLE);
			lv_applock.setAdapter(new AppLockAdapter());
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.app_lock);
		super.onCreate(savedInstanceState);
		provider = new AppInfoProvider(this);
		lv_applock = (ListView) findViewById(R.id.lv_applock);
		ll_loading = (LinearLayout) findViewById(R.id.ll_applock_loading);
		dao =new AppLockDao(this);
		// Find all locked app name from db
		lockedPacknames = dao.findAll();
		// Loading progress
		ll_loading.setVisibility(View.VISIBLE);
		// New sub thread for all install apps
		new Thread(){
			public void run() {
				appinfos = provider.getInstalledApps();
				handler.sendEmptyMessage(0);
			};
		}.start();
		// Set lick event for all listview items
		lv_applock.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AppInfo appinfo = (AppInfo) lv_applock.getItemAtPosition(position);
				String packname = appinfo.getPackname();
				ImageView iv = (ImageView) view.findViewById(R.id.iv_applock_status);
				// Set animation
				TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.2f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
				ta.setDuration(200);
				
				if(lockedPacknames.contains(packname)){// Locked status
					//dao.delete(packname);
					// User content provider to observe the database changes
					Uri uri = Uri.parse("content://com.example.course.applock/DELETE");
					getContentResolver().delete(uri, null, new String[]{packname});
					// Unlock
					iv.setImageResource(R.drawable.unlock);
					// Remove from current package list, then UI will refresh
					lockedPacknames.remove(packname);
				}else{// Un Locked
					//dao.add(packname);
					Uri uri = Uri.parse("content://com.example.course.applock/ADD");
					ContentValues values = new ContentValues();
					values.put("packname", packname);
					getContentResolver().insert(uri, values);
					iv.setImageResource(R.drawable.lock);
					lockedPacknames.add(packname);
				}
				view.startAnimation(ta);
			}
		});
		
	}
	// Customer adapter
	private class AppLockAdapter extends BaseAdapter{

		public int getCount() {
			return appinfos.size();
		}

		public Object getItem(int position) {
			return appinfos.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if(convertView==null){
				view = View.inflate(getApplicationContext(),R.layout.app_lock_item, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView)view.findViewById(R.id.iv_applock_icon);
				holder.iv_status = (ImageView)view.findViewById(R.id.iv_applock_status);
				holder.tv_name = (TextView)view.findViewById(R.id.tv_applock_appname);
				view.setTag(holder);
			}else{
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			AppInfo appInfo = appinfos.get(position);
			holder.iv_icon.setImageDrawable(appInfo.getAppicon());
			holder.tv_name.setText(appInfo.getAppname());
            holder.tv_name.setTextColor(Color.BLACK);
			if(lockedPacknames.contains(appInfo.getPackname())){
				holder.iv_status.setImageResource(R.drawable.lock);
			}else{
				holder.iv_status.setImageResource(R.drawable.unlock);
			}
			return view;
		}
	}

	public static class ViewHolder{
		ImageView iv_icon;
		ImageView iv_status;
		TextView tv_name;
	}
}
