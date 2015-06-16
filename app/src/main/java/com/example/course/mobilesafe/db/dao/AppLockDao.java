package com.example.course.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.course.mobilesafe.db.AppLockDBOpenHelper;

public class AppLockDao {
	private AppLockDBOpenHelper helper;

	public AppLockDao(Context context) {
		helper = new AppLockDBOpenHelper(context);
	}
	/**
	 * Find package name for a app
	 * return true: the package can be found   false: not found
	 */
	public boolean find(String packname) {
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select * from applock where packname =?",
					new String[] { packname });
			if (cursor.moveToFirst()) {
				result = true;
			}
			cursor.close();
			db.close();
		}
		return result;
	}
	/**
	 * Add a package name to database
	 */
	public boolean add(String packname) {
		// First query this item
		if (find(packname))
			return false;
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("insert into applock (packname) values (?)",
					new Object[] { packname });
			db.close();
		}
		return find(packname);
	}

	/**
	 * Delete one item by package name
	 */
	public void delete(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("delete from applock where packname=?",
					new Object[] { packname });
			db.close();
		}
	}
	/**
	 * Find all apps
	 * 
	 * @return
	 */
	public List<String> findAll() {
		List<String> packnames = new ArrayList<String>();
		SQLiteDatabase db = helper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select packname from applock",
					null);
			while (cursor.moveToNext()) {
				packnames.add(cursor.getString(0));
			}
			cursor.close();
			db.close();
		}
		return packnames;
	}
}
