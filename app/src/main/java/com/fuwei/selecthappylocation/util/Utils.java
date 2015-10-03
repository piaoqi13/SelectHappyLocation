package com.fuwei.selecthappylocation.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.fuwei.selecthappylocation.R;

/**
 * Created by collin on 2015-10-02.
 */
public class Utils {
    // 获取百度ApiKey值CollinWang
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("CollinWang", "Catch=", e);
        }
        return apiKey;
    }

    // 是否闰年CollinWang
    public static boolean isLudgeYear(long year) {
        boolean isLeadyear;
        if (year % 4 == 0 && year % 100 != 0) {
            isLeadyear = true;
        } else if (year % 400 == 0) {
            isLeadyear = true;
        } else
            isLeadyear = false;
        return isLeadyear;
    }

    // 开始Act左动画Collin
    public static void toLeftAnim(Context mContext, Intent intent, boolean isFinished) {
        Activity mActivity = (Activity) mContext;
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.right_to_current, R.anim.curent_to_left);
        if (isFinished) {
            mActivity.finish();
        }
    }

    // 开始Act右动画Collin
    public static void toRightAnimBai(Context mContext, Intent intent, boolean isFinished) {
        Activity mActivity = (Activity) mContext;
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.left_to_current, R.anim.curent_to_right);
        if (isFinished) {
            mActivity.finish();
        }
    }
}
