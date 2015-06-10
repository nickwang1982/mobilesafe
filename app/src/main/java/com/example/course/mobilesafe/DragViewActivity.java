package com.example.course.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class DragViewActivity extends Activity {
	protected static final String TAG = "DragViewActivity";
	private ImageView iv_drag_view; // the view to drag
	private TextView tv_drag_view;
	private int windowHeight;
	private int windowWidth;
	private SharedPreferences sp;
	private long firstclicktime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drag_view);
		iv_drag_view = (ImageView) findViewById(R.id.iv_drag_view);
		tv_drag_view = (TextView) findViewById(R.id.tv_drag_view);
		windowHeight = getWindowManager().getDefaultDisplay().getHeight();
		windowWidth = getWindowManager().getDefaultDisplay().getWidth();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// Init the last location of this view
		RelativeLayout.LayoutParams params = (LayoutParams) iv_drag_view
				.getLayoutParams();
		params.leftMargin = sp.getInt("lastx", 0);
		params.topMargin = sp.getInt("lasty", 0);
		iv_drag_view.setLayoutParams(params);

        // Handle double-click move to center.
		iv_drag_view.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.i(TAG, "Been Touched.......................");
				if (firstclicktime > 0) {// default value of firstclicktime is 0
					long secondclickTime = System.currentTimeMillis();
					if (secondclickTime - firstclicktime < 500) {
						Log.i(TAG, "Double clicked.......................");

						firstclicktime = 0;
						int right = iv_drag_view.getRight();
						int left = iv_drag_view.getLeft();
						int iv_width = right - left;

						int iv_left = windowWidth / 2 - iv_width / 2;
						int iv_right = windowWidth / 2 + iv_width / 2;

						iv_drag_view.layout(iv_left, iv_drag_view.getTop(),
								iv_right, iv_drag_view.getBottom());
						Editor editor = sp.edit();
						int lasty = iv_drag_view.getTop();
						int lastx = iv_drag_view.getLeft();
						editor.putInt("lastx", lastx);
						editor.putInt("lasty", lasty);
						editor.commit();

					}
				}
				firstclicktime = System.currentTimeMillis();
				// Fix user abnormal case, click once then wait chen double-click
				new Thread() {
					public void run() {
						try {
							Thread.sleep(500);
							firstclicktime = 0;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					};
				}.start();
			}
		});

		iv_drag_view.setOnTouchListener(new OnTouchListener() {
			int startx;
			int starty;

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Log.i(TAG, "Touch");
					startx = (int) event.getRawX();
					starty = (int) event.getRawY();

					break;

				case MotionEvent.ACTION_MOVE:
					int x = (int) event.getRawX();
					int y = (int) event.getRawY();

					int tv_height = tv_drag_view.getBottom()
							- tv_drag_view.getTop();

					if (y > (windowHeight / 2)) {// Move to bottom of the screen
						// Move hint textview to the up side of the screen
						tv_drag_view.layout(tv_drag_view.getLeft(), 60,
								tv_drag_view.getRight(), 60 + tv_height);
					} else {
						tv_drag_view.layout(tv_drag_view.getLeft(),
								windowHeight - 20 - tv_height,
								tv_drag_view.getRight(), windowHeight - 20);
					}

					int dx = x - startx;// Move distance of x
					int dy = y - starty;// Move distance of y

					int t = iv_drag_view.getTop();
					int b = iv_drag_view.getBottom();
					int l = iv_drag_view.getLeft();
					int r = iv_drag_view.getRight();

					int newl = l + dx;
					int newt = t + dy;
					int newr = r + dx;
					int newb = b + dy;
					// See whether the new location is out of screen
					if (newl < 0 || newt < 0 || newr > windowWidth
							|| newb > windowHeight) {
						break;
					}
                    // draw this on layout
					iv_drag_view.layout(newl, newt, newr, newb);
					startx = (int) event.getRawX();
					starty = (int) event.getRawY();
					Log.i(TAG, "Move");
					break;
				case MotionEvent.ACTION_UP:
					Log.i(TAG, "Up");

					Editor editor = sp.edit();
					int lasty = iv_drag_view.getTop();
					int lastx = iv_drag_view.getLeft();
					editor.putInt("lastx", lastx);
					editor.putInt("lasty", lasty);
					editor.commit();
					break;
				}
				return false;
			}
		});
	}
}