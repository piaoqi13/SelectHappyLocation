package com.fuwei.selecthappylocation.util;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.fuwei.selecthappylocation.FuWeiApplication;

/**
 * Created by collin on 2015-10-02.
 */
public class BaiduMapUtil {
    private static BaiduMapUtil mBaiduMapUtil = null;
    private LocationClient mLocationClient = null;
    private LocationListener mLocationListener = null;

    public static  BaiduMapUtil getInstance() {
        if (mBaiduMapUtil == null) {
            mBaiduMapUtil = new BaiduMapUtil();
        }
        return mBaiduMapUtil;
    }

    public void startLocation(Context context) {
        mLocationClient = new LocationClient(context);
        mLocationListener = new LocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        // 设置属性
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(true);
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
        // 定位开始
        mLocationClient.start();
    }

    public class LocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // 拿到当前省份城市名称
            FuWeiApplication.getInstance().mProvince = location.getProvince();
            FuWeiApplication.getInstance().mCity = location.getCity();
        }
    }

    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }
}
