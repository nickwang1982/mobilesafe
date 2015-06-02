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
    private ListView lv_select_contact;//����չ����ϵ�˵��б�
    private ContactInfoProvider provider;//��ȡ�ֻ���ϵ�˵Ķ���
    private List<ContactInfo> infos;//���ջ�ȡ����������ϵ��

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        lv_select_contact = (ListView) findViewById(R.id.lv_select_contact);
        provider = new ContactInfoProvider(this);
        infos = provider.getContactInfos();
        //Ϊlv_select_contact����һ����������������ڽ�������ϵ��չ�ֵ�������
        lv_select_contact.setAdapter(new ContactAdapter());
        //Ϊlv_select_contact�е�item���ü���
        lv_select_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //��ȡ�����item��Ӧ����ϵ�˵���Ϣ����
                ContactInfo info = (ContactInfo) lv_select_contact.getItemAtPosition(position);
                //��ȡ������ϵ�˵ĺ���
                String number = info.getPhone();
                //������ϵ�˵ĺ��뷵�ظ�ǰActivity��Activity
                Intent data = new Intent();
                //����ݴ��룬���ڷ��ظ�Activity
                data.putExtra("number", number);
                //������ݣ�����һ�����ؽ����  ��������������
                setResult(0, data);
                //�رյ�ǰ��activity
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
            tv.setTextColor(Color.WHITE);
            tv.setText(info.getName()+"\n"+info.getPhone());
            return tv;
        }

    }

}
