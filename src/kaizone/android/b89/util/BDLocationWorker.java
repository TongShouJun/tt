
package kaizone.android.b89.util;

import kaizone.songfeng.whoareu.BuildConfig;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class BDLocationWorker {
    public static final String TAG = "BDLocationWorker";

    public static int sEnable = 0;

    public LocationClient mLocationClient = null;

    private Context mContext;

    private MyListener mListener = new MyListener();

    private BDLocation mLocation;

    public BDLocationWorker(Context context) {
        mContext = context;
    }

    public void control(Activity activity) {
        if (sEnable > 0)
            return;
        new AlertDialog.Builder(activity).setTitle("提示").setMessage("是否开启定位")
                .setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setupAndStart();
                    }
                }).setNegativeButton("取消", null).show();
    }

    public void setupAndStart() {
        setupLocationClient();
        start();
        sEnable = 1;
    }

    public boolean isEnable() {
        if (sEnable > 0)
            return true;
        return false;
    }

    public void setupLocationClient() {
        mLocationClient = new LocationClient(mContext);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setProdName("locSDKDemo2"); // 设置产品线名称
        option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
        option.setPoiExtraInfo(true);
        option.setAddrType("all");
        option.setScanSpan(5000); // 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
        option.setPoiNumber(10);
        option.disableCache(true);

        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(mListener);
    }

    public Context getContext() {
        return mContext;
    }

    public void start() {
        if (mLocationClient == null)
            return;
        mLocationClient.start();
    }

    public void stop() {
        if (mLocationClient != null)
            return;
        mLocationClient.stop();
    }

    public void requestLocation() {
        if (mLocationClient != null)
            return;
        mLocationClient.requestLocation();
    }

    public class MyListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(com.baidu.location.BDLocation arg0) {
            mLocation = arg0;
            // log(mContext, mLocation);
        }

        @Override
        public void onReceivePoi(com.baidu.location.BDLocation arg0) {

        }
    }

    public BDLocation getLocation() {
        return mLocation;
    }

    public static void log(Context context, BDLocation location) {
        if (!BuildConfig.DEBUG)
            return;
        StringBuffer sb = new StringBuffer(256);
        sb.append("time : ");
        sb.append(location.getTime());
        sb.append("\nerror code : ");
        sb.append(location.getLocType());
        sb.append("\nlatitude : ");
        sb.append(location.getLatitude());
        sb.append("\nlontitude : ");
        sb.append(location.getLongitude());
        sb.append("\nradius : ");
        sb.append(location.getRadius());
        if (location.getLocType() == BDLocation.TypeGpsLocation) {
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());
            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());
        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
            // sb.append("\n省：");
            // sb.append(location.getProvince());
            // sb.append("\n市：");
            // sb.append(location.getCity());
            // sb.append("\n区/县：");
            // sb.append(location.getDistrict());
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
        }
        sb.append("\nsdk version : ");
        sb.append("\n");
        sb.append("\nisCellChangeFlag : ");
        sb.append(location.isCellChangeFlag());
        if (BuildConfig.DEBUG) {
            Log.e(TAG, sb.toString());
            // Toast.makeText(context, sb.toString(), 1000).show();
        }
    }

}
