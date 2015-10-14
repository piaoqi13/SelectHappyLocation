package com.fuwei.selecthappylocation.activity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.fuwei.selecthappylocation.FuWeiApplication;
import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.adapter.ViewPagerAdapter;
import com.fuwei.selecthappylocation.util.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by collin on 2015-10-02.
 */
public class MainActivity extends BaseActivity implements OnClickListener {
    private final String mPageName = "MainActivity";
    private Resources mResource = null;
    private String mPackageName = null;
    private long mExitTime = 0;

    private LinearLayout mLlLocationScenery = null;
    private LinearLayout mLlAdvancedCustomization = null;
    private LinearLayout mLlBirthdaySelect = null;
    private LinearLayout mLlRandomSelect = null;
    private LinearLayout mLlMineLocation = null;

    private View mView = null;
    private ViewPager mViewPager = null;
    private LinearLayout mLlPointPlaceLayout = null;
    private List<View> mViews = null;
    private ImageView[] mPoints = null;
    private int mCurrentPage = 0;
    private Timer mAdvertiseTimer = null;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message message) {
            if(message.what == 1) {
                mViewPager.setCurrentItem(mCurrentPage, true);
            }
        };
    };

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
        mViewPager = (ViewPager) findViewById(R.id.vp_advertisement_viewpager);
        mLlPointPlaceLayout = (LinearLayout) findViewById(R.id.ll_place_point);
        mLlLocationScenery = (LinearLayout) findViewById(R.id.ll_location_scenery);
        mLlAdvancedCustomization = (LinearLayout) findViewById(R.id.ll_advanced_customization);
        mLlBirthdaySelect = (LinearLayout) findViewById(R.id.ll_birthday_select);
        mLlRandomSelect = (LinearLayout) findViewById(R.id.ll_random_select);
        mLlMineLocation = (LinearLayout) findViewById(R.id.ll_mine_location);
    }

    @Override
    public void initData() {
        setTitle(R.string.title_index_text);
        mViews = new ArrayList<View>();
        ImageView picture1 = new ImageView(mContext);
        ImageView picture2 = new ImageView(mContext);
        ImageView picture3 = new ImageView(mContext);
        picture1.setBackgroundResource(R.drawable.main_advertisement);
        picture2.setBackgroundResource(R.drawable.main_advertisement);
        picture3.setBackgroundResource(R.drawable.main_advertisement);
        mViews.add(picture1);
        mViews.add(picture2);
        mViews.add(picture3);

        mLlPointPlaceLayout.removeAllViews();
        mPoints = new ImageView[mViews.size()];
        for (int i = 0, size = mViews.size(); i < size; i++) {
            mPoints[i] = new ImageView(mContext);
            mPoints[i].setImageResource(R.drawable.circle_white);
            mPoints[i].setLayoutParams(new LayoutParams(Utils.dip2px(mContext,15f), Utils.dip2px(mContext,15f)));
            mPoints[i].setPadding(5, 5, 5, 5);
            mLlPointPlaceLayout.addView(mPoints[i]);
        }
        mPoints[0].setImageResource(R.drawable.circle_gray);

        mViewPager.setAdapter(new ViewPagerAdapter(mViews));
        mViewPager.setCurrentItem(mCurrentPage);
        mAdvertiseTimer = new Timer();
    }

    @Override
    public void initListener() {
        mLlLocationScenery.setOnClickListener(this);
        mLlAdvancedCustomization.setOnClickListener(this);
        mLlBirthdaySelect.setOnClickListener(this);
        mLlRandomSelect.setOnClickListener(this);
        mLlMineLocation.setOnClickListener(this);

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0, size = mViews.size(); i < size; i++) {
                    mCurrentPage = position;
                    if (position == i) {
                        mPoints[i].setImageResource(R.drawable.circle_gray);
                    } else {
                        mPoints[i].setImageResource(R.drawable.circle_white);
                    }
                }
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) { }
            public void onPageScrollStateChanged(int arg0) { }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
        mAdvertiseTimer = new Timer();
        mAdvertiseTimer.schedule(new AdvertiseTimer(), 5000, 5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
        mAdvertiseTimer.cancel();
        mAdvertiseTimer = null;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ll_location_scenery:
                intent = new Intent(mContext, LocationSceneryActivity.class);
                Utils.toLeftAnim(mContext, intent, false);
                break;
            case R.id.ll_advanced_customization:
                break;
            case R.id.ll_birthday_select:
                intent = new Intent(mContext, BirthdaySelectionActivity.class);
                Utils.toLeftAnim(mContext, intent, false);
                break;
            case R.id.ll_random_select:
                intent = new Intent(mContext, RanSecActivity.class);
                Utils.toLeftAnim(mContext, intent, false);
                break;
            case R.id.ll_mine_location:
                intent = new Intent(mContext, MySelectionActivity.class);
                Utils.toLeftAnim(mContext, intent, false);
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

    public class AdvertiseTimer extends TimerTask {
        @Override
        public void run() {
            if (mViews != null && mViews.size() > 0) {
                mCurrentPage ++;
                if (mCurrentPage == mViews.size()) {
                    mCurrentPage = 0;
                }
                mHandler.sendEmptyMessage(1);
            }
        }
    }
}
