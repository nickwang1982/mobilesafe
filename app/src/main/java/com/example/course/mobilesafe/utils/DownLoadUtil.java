package com.example.course.mobilesafe.utils;

import android.app.ProgressDialog;
import android.widget.ProgressBar;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TreeMap;

/**
 * Download File location
 * Download locations
 * Progress bar
 */
public class DownLoadUtil {

    /**
     * Download one file
     *
     * @param urlpath
     * location
     * @param filepath
     * The local location for saved file
     * @param pd
     * The progress Dialog
     * @return
     * The apk
     *
     */
    public static File getFile(String urlpath, String filepath,
                               ProgressDialog pd) {
        try {
            // start Download
            URL url = new URL(urlpath);
            File file = new File(filepath);
            FileOutputStream fos = new FileOutputStream(file);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // use GET request, the default method of conn is GET
            conn.setRequestMethod("GET");
            // Timeout
            conn.setConnectTimeout(5000);
            // Get the max length of file
            int max = conn.getContentLength();
            // Set progress dialog
            pd.setMax(max);
            // The file input  stream
            InputStream is = conn.getInputStream();
            // set buffer
            byte[] buffer = new byte[1024];
            int len = 0;
            int process  = 0;
            long time = System.currentTimeMillis();
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                process+=len;
                if (System.currentTimeMillis() - time > 30) {
                    pd.setProgress(process);
                }
                Thread.sleep(30);
            }
            // fresh data file
            fos.flush();
            fos.close();
            return file;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @param urlpath
     *
     * @return
     */
    public static String getFileName(String urlpath) {
        return urlpath
                .substring(urlpath.lastIndexOf("/") + 1, urlpath.length());
    }
}
