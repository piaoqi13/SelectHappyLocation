package com.fuwei.selecthappylocation.activity;

import android.app.Notification;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.fuwei.selecthappylocation.FuWeiApplication;
import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.util.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * Created by collin on 2015-10-02.
 */
public class MainActivity extends BaseActivity implements OnClickListener {
    private final String mPageName = "MainActivity";
    private Resources mResource = null;
    private String mPackageName = null;
    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        mResource = this.getResources();
        mPackageName = this.getPackageName();
        // 装载界面集合
        FuWeiApplication.getInstance().addActivity(this);
        // 友盟更新走起
        UmengUpdateAgent.update(this);
        // 百度推送走起
        startBaiduPush();
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        setTitle(R.string.title_index_text);
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_settings:
                break;
            default:
                break;
        }
    }

    private void startBaiduPush() {
        PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY, Utils.getMetaValue(this, "api_key"));
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                mResource.getIdentifier("notification_custom_builder", "layout", mPackageName),
                mResource.getIdentifier("notification_icon", "id", mPackageName),
                mResource.getIdentifier("notification_title", "id", mPackageName),
                mResource.getIdentifier("notification_text", "id", mPackageName));
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(R.mipmap.ic_launcher);
        PushManager.setNotificationBuilder(this, 1, cBuilder);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return doubleClickToExit();
        }
        return super.dispatchKeyEvent(event);
    }

    private boolean doubleClickToExit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
        return true;
    }
}
