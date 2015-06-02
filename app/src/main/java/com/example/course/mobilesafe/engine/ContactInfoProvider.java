package com.example.course.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.course.mobilesafe.domain.ContactInfo;

public class ContactInfoProvider {
	private Context context;

	public ContactInfoProvider(Context context) {
		this.context = context;
	}

	/**
	 * �������е���ϵ�˵���Ϣ
	 * 
	 * @return
	 */
	public List<ContactInfo> getContactInfos() {
		List<ContactInfo> infos = new ArrayList<ContactInfo>();//��������ϵ�˴���ü���
		//��ȡraw_contacts�����Ӧ��Uri
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		//��ȡdata�����Ӧ��Uri
		Uri datauri = Uri.parse("content://com.android.contacts/data");
		//���������Ҫ��ѯ���У�����ϵ�˵�id����ȡһ����ѯ��ݿ���صĽ��
		Cursor cursor = context.getContentResolver().query(uri,
				new String[] { "contact_id" }, null, null, null);
		while (cursor.moveToNext()) {//�ƶ��α�
			//��Ϊ����ֻ��Ҫ��ѯһ�����-��ϵ�˵�id���������Ǵ���0
			String id = cursor.getString(0);
			//���ڷ�װÿ����ϵ�˵ľ�����Ϣ
			ContactInfo info = new ContactInfo();
			//�õ�id������ͨ���id����ѯdata���е���ϵ�˵ľ�����ݣ�data���е�data1�е���ݣ����������null���Ὣ���е��з��ػ���
			//������ѡ������    ����һ����data���в�ѯ��Ľ��
			Cursor dataCursor = context.getContentResolver().query(datauri,
					null, "raw_contact_id=?", new String[] { id }, null);
			while (dataCursor.moveToNext()) {
				//dataCursor.getString(dataCursor.getColumnIndex("mimetype"))��ȡdata1���о�����ݵ�������ͣ������жϵ�����ϵ�˵�����
				if ("vnd.android.cursor.item/name".equals(dataCursor
						.getString(dataCursor.getColumnIndex("mimetype")))) {
					//dataCursor.getString(dataCursor.getColumnIndex("data1"))��ȡdata1���е���ϵ�˵ľ������
					info.setName(dataCursor.getString(dataCursor
							.getColumnIndex("data1")));
				} else if ("vnd.android.cursor.item/phone_v2".equals(dataCursor
						.getString(dataCursor.getColumnIndex("mimetype")))) {//��������Ƿ����ֻ����
					info.setPhone(dataCursor.getString(dataCursor
							.getColumnIndex("data1")));
				}

			}
			//ÿ��ѯһ����ϵ�˺�ͽ�����ӵ�������
			infos.add(info);
			info = null;
			dataCursor.close();//�رս��

		}
		cursor.close();
		return infos;
	}
}
