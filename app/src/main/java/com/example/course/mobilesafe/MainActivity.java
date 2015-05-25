package com.example.course.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.GridView;

import com.example.course.mobilesafe.adapter.MainAdapter;


public class MainActivity extends Activity {
    private GridView gv_main; // The GridView in main.xml

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        gv_main = (GridView) findViewById(R.id.gv_main);
        // Set a adapter for gv_main.
        gv_main.setAdapter(new MainAdapter(this));
    }

}
