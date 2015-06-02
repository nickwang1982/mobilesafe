package com.example.course.mobilesafe.engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSInfoProvider {
    private static GPSInfoProvider mGPSInfoProvider;
    private static LocationManager lm;
    private static MyListener listener;
    private static SharedPreferences sp;
    private GPSInfoProvider(){

    }

    // singlton mode
    public synchronized static GPSInfoProvider getInstance(Context context){
        if(mGPSInfoProvider==null){
            mGPSInfoProvider = new GPSInfoProvider();
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            // get query object , its a map inside
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
            criteria.setAltitudeRequired(true);
            criteria.setSpeedRequired(true);
            // get current device provider,
            // param1: query selection
            // param2: true for only available location can be return
            String provider = lm.getBestProvider(criteria, true);
            // System.out.println(provider);
            listener = new GPSInfoProvider().new MyListener();

            lm.requestLocationUpdates(provider, 60000, 100, listener);
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return mGPSInfoProvider;
    }

    /**
     * cancel listen
     */
    public void stoplisten(){
        lm.removeUpdates(listener);
        listener = null;
    }

    protected class MyListener implements LocationListener {

        /**
         * When location changed occurring
         */
        public void onLocationChanged(Location location) {
            String latitude = "latitude :" + location.getLatitude();
            String longitude = "longitude: " + location.getLongitude();
            String meter = "accuracy :" + location.getAccuracy();
            System.out.println(latitude + "-" + longitude + "-" + meter);

            SharedPreferences.Editor editor = sp.edit();
            editor.putString("last_location", latitude + "-" + longitude + "-"
                    + meter);
            editor.commit();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onProviderDisabled(String provider) {

        }
    }

    public String getLocation(){
        return sp.getString("last_location", "");
    }

}
