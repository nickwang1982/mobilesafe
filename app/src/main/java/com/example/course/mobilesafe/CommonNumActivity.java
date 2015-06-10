package com.example.course.mobilesafe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.example.course.mobilesafe.db.dao.CommonNumDao;

public class CommonNumActivity extends Activity {
	protected static final String TAG = "CommonNumActivity";
	private ExpandableListView elv_common_num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_num);
		elv_common_num = (ExpandableListView) findViewById(R.id.elv_common_num);
		elv_common_num.setAdapter(new CommonNumberAdapter());

		elv_common_num.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				TextView tv = (TextView) v;
				String number = tv.getText().toString().split("\n")[1];

				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:"+number));
				startActivity(intent);
				return false;
			}
		});
	}

	private class CommonNumberAdapter extends BaseExpandableListAdapter {

		private List<String> groupNames;

		private Map<Integer, List<String>> childrenCache; 

		public CommonNumberAdapter() {
			childrenCache = new HashMap<Integer, List<String>>();
		}


		public int getGroupCount() {
			// groupNames = CommonNumDao.getGroupNames();
			// return groupNames.size();
			return CommonNumDao.getGroupCount();
		}


		public int getChildrenCount(int groupPosition) {
			// if(childrenCache.containsKey(groupPosition)){
			// return childrenCache.get(groupPosition).size();
			// }else{
			// List<String> results =
			// CommonNumDao.getChildNameByPosition(groupPosition);
			// childrenCache.put(groupPosition, results);
			// return results.size();
			// }
			return CommonNumDao.getChildrenCount(groupPosition);
		}

		public Object getGroup(int groupPosition) {
			return null;
		}

		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public boolean hasStableIds() {
			return false;
		}


		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView tv;

			if (convertView == null) {
				tv = new TextView(getApplicationContext());
			} else {
				tv = (TextView) convertView;
			}
			tv.setTextSize(28);
            tv.setTextColor(Color.BLACK);
			if (groupNames != null) {
				tv.setText("          " + groupNames.get(groupPosition));
			} else {
				groupNames = CommonNumDao.getGroupNames();
				tv.setText("          " + groupNames.get(groupPosition));
			}

			return tv;
		}


		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView tv;
			if (convertView == null) {

				tv = new TextView(getApplicationContext());
			} else {
				tv = (TextView) convertView;
			}
			tv.setTextSize(20);
            tv.setTextColor(Color.BLACK);
			String result = null;
			if (childrenCache.containsKey(groupPosition)) {
				result = childrenCache.get(groupPosition).get(childPosition);
			} else {
				List<String> results = CommonNumDao
						.getChildNameByPosition(groupPosition);
				childrenCache.put(groupPosition, results);
				result = results.get(childPosition);
			}
			tv.setText(result);
			return tv;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}
}
