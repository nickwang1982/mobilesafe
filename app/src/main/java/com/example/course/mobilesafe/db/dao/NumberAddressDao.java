package com.example.course.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Get number address
 * @author
 */
public class NumberAddressDao {

	/**
	 * Get number address
	 * @param number
	 * @return
	 */
	public static String getAddress(String number) {
		// No address found , then return current number
		String address = number;
		// Data location for current packages
		String path = "/data/data/com.example.course.mobilesafe/files/address.db";
		// Open database
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		// Whether Database is opened
		if (db.isOpen()) {
			// match phone number
			if (number.matches("^1[3458]\\d{9}$")) {
                // The number should be length 11.
                // First number is 1, second number comes from 3,4,5,8
                // Followed by 9 numbers
				Cursor cursor = db
						.rawQuery(
								"select city from address_tb where _id=(select outkey from numinfo where mobileprefix =?)",
								new String[] { number.substring(0, 7) });
				if (cursor.moveToFirst()) {
					address = cursor.getString(0);
				}
				cursor.close();

			} else {
				Cursor cursor;
				switch (number.length()) {
				case 4:
					address = "Emulator";
					break;
				case 7:
					address = "Local number";
					break;
				case 8:
					address = "Local number";
					break;
				case 10:
                    // Only get the first item
					cursor = db
							.rawQuery(
									"select city from address_tb where area = ? limit 1",
									new String[] { number.substring(0, 3) });
					if (cursor.moveToFirst()) {
						address = cursor.getString(0);
					}
					cursor.close();
					break;
				case 12:// 4 area number + 8 phone number
					cursor = db
							.rawQuery(
									"select city from address_tb where area = ? limit 1",
									new String[] { number.substring(0, 4) });
					if (cursor.moveToFirst()) {
						address = cursor.getString(0);
					}
					cursor.close();
					break;
				case 11:// 3 area number + 8 number || 4 area number + 7 number
					cursor = db
							.rawQuery(
									"select city from address_tb where area = ? limit 1",
									new String[] { number.substring(0, 3) });
					if (cursor.moveToFirst()) {
						address = cursor.getString(0);
					}
					cursor.close();
					cursor = db
							.rawQuery(
									"select city from address_tb where area = ? limit 1",
									new String[] { number.substring(0, 4) });
					if (cursor.moveToFirst()) {
						address = cursor.getString(0);
					}
					cursor.close();
					break;
				}
			}

			db.close();
		}

		return address;
	}
}
