package com.fuwei.selecthappylocation.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fuwei.selecthappylocation.FuWeiApplication;
import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.event.Event;
import com.fuwei.selecthappylocation.http.ReqListener;
import com.fuwei.selecthappylocation.model.LoginInfo;
import com.fuwei.selecthappylocation.model.ResultLoginInfo;
import com.fuwei.selecthappylocation.util.BaiduMapUtil;
import com.fuwei.selecthappylocation.util.Settings;
import com.fuwei.selecthappylocation.util.Utils;

/**
 * Created by collin on 2015-10-11.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, ReqListener {
    private static final int LOGIN_SUCCEED = 1;
    private static final int LOGIN_FAILED = 2;

    private EditText mEdtIdentityNumber = null;
    private Button mBtnLogin = null;

    private int count = 0;
    private boolean isClicked = false;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case LOGIN_SUCCEED:
                    Settings.setBoolean("isHaveSelection", true, false);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    Utils.toLeftAnim(mContext, intent, true);
                    // 处理动态加载
                    if (mHandler != null && mRun != null) {
                        mHandler.removeCallbacks(mRun);
                    }
                    // 统统清空
                    FuWeiApplication.getInstance().exit();
                    break;
                case LOGIN_FAILED:
                    // 点击禁止控件可用
                    mEdtIdentityNumber.setEnabled(true);
                    isClicked = false;
                    // 点击立即变字
                    mBtnLogin.setText("确认登录");
                    // 点击立即变色
                    mBtnLogin.setBackgroundResource(R.drawable.btn_background_normal);
                    if (mHandler != null && mRun != null) {
                        mHandler.removeCallbacks(mRun);
                    }
                    String tip = (String)msg.obj;
                    toShow(tip);
                    break;
            }
        };
    };

    private Runnable mRun = new Runnable() {
        public void run() {
            String text = "";
            switch (count % 4) {
                case 0:
                    text = "登录中...";
                    break;
                case 3:
                    text = "登录中..";
                    break;
                case 2:
                    text = "登录中.";
                    break;
                case 1:
                    text = "登录中";
                    break;
            }
            mBtnLogin.setText(text);
            count++;
            if(mHandler!=null && mRun!=null)
                mHandler.postDelayed(mRun, 180);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.login_activity);
        super.onCreate(savedInstanceState);
        // 百度定位走起
        BaiduMapUtil.getInstance().startLocation(mContext);
    }

    @Override
    public void initView() {
        mEdtIdentityNumber = (EditText) findViewById(R.id.edt_identity_card);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
    }

    @Override
    public void initData() {
        setTitle(R.string.btn_login_text);
    }

    @Override
    public void initListener() {
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    protected void HandleLeftNavBtn() {
        super.HandleLeftNavBtn();
        // 动画结束
        Utils.toRightAnim(mContext);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (isClicked) {
                    toShow("正在登录...");
                } else {
                    isClicked = true;
                    // 点击禁止控件可用
                    mEdtIdentityNumber.setEnabled(false);
                    // 点击立即变字
                    mBtnLogin.setText("登录中");
                    // 点击立即变色
                    mBtnLogin.setBackgroundResource(R.drawable.btn_background_pressed);
                    count = 0;
                    if(mHandler != null && mRun != null) {
                        mHandler.postDelayed(mRun, 180);
                    }
                    // Demo假数据
                    mHandler.sendEmptyMessageDelayed(LOGIN_SUCCEED,444);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onUpdate(Event event, Object obj) {
        Message msg = new Message();
        switch (event) {
            case EVENT_LOGIN_SUCCESS:
                LoginInfo login = ((ResultLoginInfo) obj).getData();
                msg.what = LOGIN_SUCCEED;
                msg.obj = login;
                if (mHandler != null) {
                    mHandler.sendMessage(msg);
                }
                break;
            case EVENT_LOGIN_FAIL:
                String tip = (String)obj;
                msg.what = LOGIN_FAILED;
                msg.obj = tip;
                if (mHandler != null) {
                    mHandler.sendMessage(msg);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler = null;
        mRun = null;
        // 百度定位关闭
        BaiduMapUtil.getInstance().stopLocation();
    }
}
