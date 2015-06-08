package com.example.course.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.course.mobilesafe.domain.ContactInfo;
import com.example.course.mobilesafe.engine.ContactInfoProvider;

import java.util.List;


public class SelectContactActivity extends Activity {
    private ListView lv_select_contact;
    private ContactInfoProvider provider;
    private List<ContactInfo> infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        lv_select_contact = (ListView) findViewById(R.id.lv_select_contact);
        provider = new ContactInfoProvider(this);
        infos = provider.getContactInfos();

        lv_select_contact.setAdapter(new ContactAdapter());

        lv_select_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ContactInfo info = (ContactInfo) lv_select_contact.getItemAtPosition(position);

                String number = info.getPhone();

                Intent data = new Intent();

                data.putExtra("number", number);

                setResult(0, data);

                finish();
            }
        });

    }
    private class ContactAdapter extends BaseAdapter {

        public int getCount() {
            return infos.size();
        }

        public Object getItem(int position) {
            return infos.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ContactInfo info = infos.get(position);
            TextView tv = new TextView(getApplicationContext());
            tv.setTextSize(24);
            tv.setTextColor(Color.BLACK);
            tv.setText(info.getName()+"\n"+info.getPhone());
            return tv;
        }

    }

}
