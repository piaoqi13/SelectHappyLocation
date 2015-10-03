package com.fuwei.selecthappylocation;
/**
 * Created by collin on 2015-10-02.
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.fuwei.selecthappylocation.http.HttpClient;
import com.fuwei.selecthappylocation.util.Settings;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

import java.util.LinkedList;

public class FuWeiApplication extends Application {
    public static Context mContext = null;
    private static FuWeiApplication mFuWeiApplication = null;
    private ImageLoader mImageLoader = ImageLoader.getInstance();
    // 装载打开Act
    private LinkedList<Activity> mActList = new LinkedList<Activity>();

    // 滚轮数据统一在此初始化
    public String[] mYearArray = null;
    public String[] mMonthArray = null;
    public String[] mDay30Array = null;// 4、6、9、11月
    public String[] mDay31Array = null;// 1、3、5、7、8、10、12月
    public String[] mDay29Array = null;// 闰年2月
    public String[] mDay28Array = null;// 平年2月
    public String[] mHourArray = null;
    public String[] mMinuteArray = null;

    public String mProvince = null;// 当前省份
    public String mCity = null;// 当前城市

    @Override
    public void onCreate() {
        super.onCreate();
        mFuWeiApplication = this;
        mContext = getApplicationContext();
        // ImageLoader初始化
        mImageLoader.init(getImageLoaderConfig());
        // 滚轮数据初始化
        initWheelData();
        // 数据存储初始化
        Settings.initPreferences(mContext);
        // 网络请求初始化
        HttpClient.getInstance().init(this);
        // 禁止默认统计
        MobclickAgent.openActivityDurationTrack(false);
        // 友盟错误上传
        MobclickAgent.setCatchUncaughtExceptions(true);
    }

    public static FuWeiApplication getInstance() {
        return mFuWeiApplication;
    }

    private void initWheelData() {
        mYearArray = new String[100];
        for (int i=1; i<=mYearArray.length; i++) {
            mYearArray[i-1] = (String.valueOf(i + 1915)) + "年";
        }

        mMonthArray = new String[12];
        for (int i=1; i<=mMonthArray.length; i++) {
            mMonthArray[i-1] = (String.valueOf(i).length()==1 ? "0" + String.valueOf(i) : String.valueOf(i)) + "月";
        }

        // 一个月30天
        mDay30Array = new String[30];
        for (int i=1; i<=mDay30Array.length; i++) {
            mDay30Array[i-1] = (String.valueOf(i).length()==1 ? "0" + String.valueOf(i) : String.valueOf(i)) + "日";
        }

        // 一个月31天
        mDay31Array = new String[31];
        for (int i=1; i<=mDay31Array.length; i++) {
            mDay31Array[i-1] = (String.valueOf(i).length()==1 ? "0" + String.valueOf(i) : String.valueOf(i)) + "日";
        }

        // 一个月29天
        mDay29Array = new String[29];
        for (int i=1; i<=mDay29Array.length; i++) {
            mDay29Array[i-1] = (String.valueOf(i).length()==1 ? "0" + String.valueOf(i) : String.valueOf(i)) + "日";
        }

        // 一个月28天
        mDay28Array = new String[28];
        for (int i=1; i<=mDay28Array.length; i++) {
            mDay28Array[i-1] = (String.valueOf(i).length()==1 ? "0" + String.valueOf(i) : String.valueOf(i)) + "日";
        }

        mHourArray = new String[24];
        for (int i=1; i<=mHourArray.length; i++) {
            mHourArray[i-1] = (String.valueOf(i-1).length()==1 ? "0" + String.valueOf(i-1) : String.valueOf(i-1)) + "时";
        }

        mMinuteArray = new String[60];
        for (int i=1; i<=mMinuteArray.length; i++) {
            mMinuteArray[i-1] = (String.valueOf(i-1).length()==1 ? "0" + String.valueOf(i-1) : String.valueOf(i-1)) + "分";
        }
    }

    // 图片默认属性
    public ImageLoaderConfiguration getImageLoaderConfig() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize((int) (2 * 1024 * 1024))
                .memoryCacheSizePercentage(13)
                .discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
                .build();
        return config;
    }

    // 图片默认参数
    public DisplayImageOptions initLoaderParam(int id) {
        DisplayImageOptions mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(id)
                .showImageForEmptyUri(id)
                .showImageOnFail(id).cacheInMemory(true)
                .cacheOnDisc(true).considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer()).build();
        return mOptions;
    }

    // 添加Activity到集合
    public void addActivity(Activity activity) {
        if (mActList == null) {
            mActList = new LinkedList<Activity>();
        }
        mActList.add(activity);
    }

    // 结束所有打开Activity
    public void exit() {
        if (mActList == null) {
            return;
        }
        for (Activity activity : mActList) {
            if (activity != null) {
                activity.finish();
            }
        }
        mActList.clear();
        mActList = null;
    }
}
