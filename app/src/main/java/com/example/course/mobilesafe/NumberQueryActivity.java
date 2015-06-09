package com.example.course.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.course.mobilesafe.db.dao.NumberAddressDao;

public class NumberQueryActivity extends Activity {
	private EditText et_number_query;
	private TextView tv_number_address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.number_query);
		super.onCreate(savedInstanceState);
		et_number_query = (EditText) findViewById(R.id.et_number_query);
		tv_number_address = (TextView) findViewById(R.id.tv_number_address);
	}

	/**
	 * Click query
	 * 
	 * @param view
	 */
	public void query(View view) {
        // Clear blank
		String number = et_number_query.getText().toString().trim();
		if (TextUtils.isEmpty(number)) {
			Toast.makeText(this, "Number should not be null", Toast.LENGTH_SHORT).show();
			// Add a little animation for this
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			et_number_query.startAnimation(shake);
			return;
		} else {// number is not null, return location
			String address = NumberAddressDao.getAddress(number);
			// Show location on UI
			tv_number_address.setText(address);
		}
	}
}
