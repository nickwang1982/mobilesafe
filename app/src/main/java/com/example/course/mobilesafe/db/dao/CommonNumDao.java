package com.example.course.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonNumDao {


	public static int getGroupCount() {
		int count = 0;

		String path = "/data/data/com.example.course.mobilesafe/files/commonnum.db";

		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from classlist", null);
			count = cursor.getCount();
			cursor.close();
			db.close();
		}
		return count;
	}


	public static List<String> getGroupNames() {

		List<String> groupNames = new ArrayList<String>();
		String path = "/data/data/com.example.course.mobilesafe/files/commonnum.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select name from classlist", null);
			while (cursor.moveToNext()) {
				String groupName = cursor.getString(0);
				groupNames.add(groupName);
				groupName = null;
			}
			cursor.close();
			db.close();
		}
		return groupNames;
	}


	public static String getGroupNameByPosition(int groupPosition) {
		String name = null;
		String path = "/data/data/com.example.course.mobilesafe/files/commonnum.db";

		int newposition = groupPosition + 1;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select name from classlist where idx=?",
					new String[] { newposition + "" });
			if (cursor.moveToFirst()) {
				name = cursor.getString(0);
			}
			cursor.close();
			db.close();
		}
		return name;
	}


	public static int getChildrenCount(int groupPosition) {
		int count = 0;
		String path = "/data/data/com.example.course.mobilesafe/files/commonnum.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);

		int newposition = groupPosition + 1;
		String sql = "select * from table" + newposition;
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(sql, null);

			count = cursor.getCount();
			cursor.close();
			db.close();
		}
		return count;
	}


	public static String getChildNameByPosition(int groupPosition,
			int childPosition) {
		String result = null;
		String path = "/data/data/com.example.course.mobilesafe/files/commonnum.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		int newGroupPosition = groupPosition + 1;
		int newChildPosition = childPosition + 1;

		String sql = "select name,number from table" + newGroupPosition
				+ " where _id=?";
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(sql, new String[] { newChildPosition
					+ "" });
			if (cursor.moveToFirst()) {

				String name = cursor.getString(0);
				String number = cursor.getString(1);
				result = name + "\n" + number;
			}
			cursor.close();
			db.close();
		}
		return result;
	}


	public static List<String> getChildNameByPosition(int groupPosition) {
		String result = null;
		List<String> results = new ArrayList<String>();
		String path = "/data/data/com.example.course.mobilesafe/files/commonnum.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		int newGroupPosition = groupPosition + 1;

		String sql = "select name,number from table" + newGroupPosition;
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {

				String name = cursor.getString(0);
				String number = cursor.getString(1);
				result = name + "\n" + number;
				results.add(result);
				result = null;
			}
			cursor.close();
			db.close();
		}
		return results;
	}

}
