
package kaizone.android.b89.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class Locate {

    private final static String TAG = "Locate";

    private double latitude = 0.0;

    private double longitude = 0.0;

    private double altitude = 0.0;

    private long time;

    public Locate(Context context, final Listener listener) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(context, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            // 返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, 0);
            }
            return;
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }

        LocationListener locationListener = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                if (listener != null) {
                    listener.onStatusChanged(provider, status, extras);
                }
                switch (status) {
                // GPS状态为可见时
                    case LocationProvider.AVAILABLE:
                        Log.i(TAG, "当前GPS状态为可见状态");
                        break;
                    // GPS状态为服务区外时
                    case LocationProvider.OUT_OF_SERVICE:
                        Log.i(TAG, "当前GPS状态为服务区外状态");
                        break;
                    // GPS状态为暂停服务时
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        Log.i(TAG, "当前GPS状态为暂停服务状态");
                        break;
                }
                Log.e(TAG, "onStatusChanged");
            }

            @Override
            public void onProviderEnabled(String provider) {
                if (listener != null) {
                    listener.onProviderEnabled(provider);
                }
                Log.e(TAG, "onProviderEnabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                if (listener != null) {
                    listener.onProviderDisabled(provider);
                }
                Log.e(TAG, "onProviderDisabled");
            }

            @Override
            public void onLocationChanged(Location location) {
                if (listener != null) {
                    listener.onLocationChanged(location);
                }
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                altitude = location.getAltitude();
                time = location.getTime();
                Log.e(TAG, "onLocationChanged");
            }
        };

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1,
                    locationListener);
            Location location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
        //
        else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0,
                    locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public long getTime() {
        return time;
    }

    public interface Listener {
        void onStatusChanged(String provider, int status, Bundle extras);

        void onProviderEnabled(String provider);

        void onProviderDisabled(String provider);

        void onLocationChanged(Location location);
    }
}
