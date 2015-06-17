package com.example.course.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.course.mobilesafe.db.BlackNumberDBOpenHelper;
import com.example.course.mobilesafe.domain.BlackNumber;

public class BlackNumberDao {
	private BlackNumberDBOpenHelper helper;

	public BlackNumberDao(Context context) {
		helper = new BlackNumberDBOpenHelper(context);
	}


	public boolean find(String number) {
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select * from blacknumber where number =?",
					new String[] { number });
			if (cursor.moveToFirst()) {
				result = true;
			}
			cursor.close();
			db.close();
		}
		return result;
	}

	public int findNumberMode(String number) {
		// 3 Types of block mode: 0 for SMS, 1 for tel, 2 for tel & sms. defaut -1 : no block
		int result = -1;
		SQLiteDatabase db = helper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select mode from blacknumber where number =?",
					new String[] { number });
			if (cursor.moveToFirst()) {
				result = cursor.getInt(0);
			}
			cursor.close();
			db.close();
		}
		return result;
	}


	public boolean add(String number, String mode) {
		if (find(number))

			return false;
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("insert into blacknumber (number,mode) values (?,?)",
					new Object[] { number, mode });
			db.close();
		}
		return find(number);
	}

	public void delete(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("delete from blacknumber where number=?",
					new Object[] { number });
			db.close();
		}
	}

	/**
	 * Update black number list
	 * 
	 * @param oldnumber
	 *            
	 * @param newnumber
	 *            Support null
	 * @param mode
	 *            New mode
	 */
	public void update(String oldnumber, String newnumber, String mode) {

		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			if (TextUtils.isEmpty(newnumber)) {
				
				newnumber = oldnumber;
			}

			db.execSQL(
					"update blacknumber set number=?, mode=? where number=?",
					new Object[] { newnumber, mode, oldnumber });
			db.close();
		}
	}

	/**
	 *  Find all black numbers.
	 * 
	 * @return
	 */
	public List<BlackNumber> findAll() {
		List<BlackNumber> numbers = new ArrayList<BlackNumber>();
		SQLiteDatabase db = helper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select number,mode from blacknumber",
					null);
			while (cursor.moveToNext()) {
				BlackNumber blackNumber = new BlackNumber();
				blackNumber.setNumber(cursor.getString(0));
				blackNumber.setMode(cursor.getInt(1));
				numbers.add(blackNumber);
				blackNumber = null;
			}
			cursor.close();
			db.close();
		}
		return numbers;
	}
}
