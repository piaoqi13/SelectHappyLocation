package com.fuwei.selecthappylocation.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;

import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.util.Settings;
import com.fuwei.selecthappylocation.util.Settings.PublicProperty;

/**
 *
 */
public class BaseActivity extends Activity {
    protected static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
    protected static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";
    protected int mScreenWidth = 0;
    protected Activity mActivity = null;
    protected Context mContext = null;
    protected boolean isPauseOnScroll = false;
    protected boolean isPauseOnFling = true;
    private Toast mToast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mActivity = this;
        Settings.initPreferences(mContext);
        mScreenWidth = Settings.getInt(PublicProperty.SCREEN_WIDTH, 1080, true);
        initView();
        initData();
        initListener();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        isPauseOnScroll = savedInstanceState.getBoolean(STATE_PAUSE_ON_SCROLL, false);
        isPauseOnFling = savedInstanceState.getBoolean(STATE_PAUSE_ON_FLING, true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_PAUSE_ON_SCROLL, isPauseOnScroll);
        outState.putBoolean(STATE_PAUSE_ON_FLING, isPauseOnFling);
    }

    public void toShow(String str) {
        if (!TextUtils.isEmpty(str)) {
            mToast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    public void toShow(int resId) {
        String toastStr = mContext.getString(resId);
        if (!TextUtils.isEmpty(toastStr)) {
            mToast = Toast.makeText(this, toastStr, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    public void setTitle(int titleResId) {
        ((TextView) findViewById(R.id.title_text)).setText(getString(titleResId));
    }

    public void setTitle(String titleResId) {
        ((TextView) findViewById(R.id.title_text)).setText(titleResId);
    }

    // 左按钮
    public View getLeftBtn() {
        View btn = findViewById(R.id.btn_left);
        btn.setVisibility(View.VISIBLE);
        return btn;
    }

    // 左文案
    public View getLeftSettingBtn(String text) {
        TextView btnTv = (TextView) findViewById(R.id.btn_left_setting);
        if (!TextUtils.isEmpty(text)) {
            btnTv.setVisibility(View.VISIBLE);
            btnTv.setText(text);
        }
        return btnTv;
    }

    // 右按钮
    public View getRightBtn() {
        View btnTv = (View) findViewById(R.id.btn_right);
        btnTv.setVisibility(View.VISIBLE);
        return btnTv;
    }

    // 右文案
    public View getRightSettingBtn(String text) {
        TextView btnTv = (TextView) findViewById(R.id.btn_right_setting);
        if (!TextUtils.isEmpty(text)) {
            btnTv.setVisibility(View.VISIBLE);
            btnTv.setText(text);
        }
        return btnTv;
    }

    // 直接设置左按钮
    public View setLeftBtn(int id) {
        TextView btnTv = (TextView) findViewById(R.id.btn_left_setting);
        btnTv.setVisibility(View.VISIBLE);
        btnTv.setOnClickListener(leftNavBtnListener);
        btnTv.setBackgroundResource(id);
        return btnTv;
    }

    // 直接设置右按钮
    public View setRightBtn(int id) {
        ImageView btnTv = (ImageView) findViewById(R.id.btn_right);
        btnTv.setVisibility(View.VISIBLE);
        btnTv.setOnClickListener(rightNavBtnListener);
        btnTv.setBackgroundResource(id);
        return btnTv;
    }

    protected void HandleRightNavBtn() {
        // 处理右导航按钮事件
    }

    protected void HandleLeftNavBtn() {
        // 处理左导航按钮事件
    }

    private View.OnClickListener leftNavBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HandleLeftNavBtn();
        }
    };

    private View.OnClickListener rightNavBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HandleRightNavBtn();
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            HandleLeftNavBtn();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    // 不一定必须重写
    public void initView() { };
    public void initData() { };
    public void initListener() { };
}
