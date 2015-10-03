package com.fuwei.selecthappylocation.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.fuwei.selecthappylocation.activity.MainActivity;

import java.util.List;

/**
 * Created by collin on 2015-10-02.
 */
public class BaiduPushReceiver extends PushMessageReceiver {
    @Override
    public void onBind(Context context, int errorCode, String appid, String userId, String channelId, String requestId) {
        // ToDo
    }

    @Override
    public void onDelTags(Context context, int errorCode, List<String> sucessTags, List<String> failTags, String requestId) {
        // ToDo
    }

    @Override
    public void onListTags(Context context, int errorCode, List<String> tags, String requestId) {
        // ToDo
    }

    @Override
    public void onMessage(Context context, String message, String customContentString) {
        // ToDo
    }

    @Override
    public void onNotificationArrived(Context context, String title, String description, String customContentString) {
        // ToDo
    }

    @Override
    public void onNotificationClicked(Context context, String title, String description, String customContentString) {
        String notifyString = "通知点击title=\"" + title + "\" description=\"" + description + "\" content=" + customContentString;
        Log.i("CollinWang", "Push=" + notifyString);
        Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }

    @Override
    public void onSetTags(Context context, int errorCode, List<String> sucessTags, List<String> failTags, String requestId) {
        // ToDo
    }

    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        // ToDo
    }
}
