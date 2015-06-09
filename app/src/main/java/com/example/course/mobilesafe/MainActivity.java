package com.example.course.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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

        gv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // Safe Mobile
                        // Navigate to Lost protection

                        Intent lostprotecedIntent = new Intent(MainActivity.this,
                                LostProtectedActivity.class);
                        startActivity(lostprotecedIntent);
                        break;
                    case 7: // Advanced tools
                        Intent atoolsIntent = new Intent(MainActivity.this,
                                AtoolsActivity.class);
                        startActivity(atoolsIntent);
                        break;
                    case 8:
                        Intent settingintent = new Intent(MainActivity.this,
                                SettingCenterActivity.class);
                        startActivity(settingintent);
                        break;
                }
            }
        });
    }

}
