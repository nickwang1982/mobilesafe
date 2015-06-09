package com.example.course.mobilesafe.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;

/**
 * Copy db files to user device
 * 
 * @author
 * 
 */
public class AssetCopyUtil {
	private Context context;

	public AssetCopyUtil(Context context) {
		this.context = context;
	}


	public boolean copyFile(String srcfilename, File file, ProgressDialog pd) {
		try {
			// Use Asset manager to get files from Asset folder
			AssetManager am = context.getAssets();
			// Open file to a input stream
			InputStream is = am.open(srcfilename);
			// Get file size
			int max = is.available();
			// Set max size for progress dialog
			pd.setMax(max);
			// Create output stream
			FileOutputStream fos = new FileOutputStream(file);
			// create buffer
			byte[] buffer = new byte[1024];
			int len = 0;
			// start from 0
			int process = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				// keep updating
				process += len;
				pd.setProgress(process);
			}
			// Close file
			fos.flush();
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
}
