package com.example.course.mobilesafe.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.example.course.mobilesafe.db.dao.AppLockDao;
/**
 * Content provier, just for add and delete.
 * @author Administrator
 *
 */
public class AppLockDBProvider extends ContentProvider {
	private static final int ADD = 1;
	// content://com.example.course.applock/ADD
	// content://com.example.course.applock/DELETE
	private AppLockDao dao;
    // Define the match code
	private static final int DELETE = 2;
	public static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	// Define match path
	static {
		// match Uri
		// param 1: host
		// param 2: the operation add/delete
		// param 3: match code matcher.match(Uri)
		matcher.addURI("com.example.course.applock", "ADD", ADD);
		matcher.addURI("com.example.course.applock", "DELETE", DELETE);
	}
	@Override
	public boolean onCreate() {
		dao = new AppLockDao(getContext());
		return false;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return null;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// Match Uri
		int result = matcher.match(uri);
		// See whether Add
		if (result == ADD) {
			// Get package name from add
			String packname = values.getAsString("packname");
			// Add to database
			dao.add(packname);
			// Publish database changes
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return null;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int result = matcher.match(uri);
		if (result == DELETE) {
			dao.delete(selectionArgs[0]);
			// Publish database changes
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}
}
