package com.fuwei.selecthappylocation.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fuwei.selecthappylocation.FuWeiApplication;
import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.dialog.LoadingDialog;
import com.fuwei.selecthappylocation.event.Event;
import com.fuwei.selecthappylocation.http.NetWorkUtil;
import com.fuwei.selecthappylocation.http.ReqListener;
import com.fuwei.selecthappylocation.model.OrderInfo;
import com.fuwei.selecthappylocation.model.PosInfo;
import com.fuwei.selecthappylocation.model.ResultLoginInfo;
import com.fuwei.selecthappylocation.model.RoomInfo;
import com.fuwei.selecthappylocation.model.UserInfo;
import com.fuwei.selecthappylocation.util.Utils;
import com.fuwei.selecthappylocation.view.CountDownView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by linky on 15-10-12.
 * Modify by collin on 2015-10-14
 */

public class MySelectionActivity extends BaseActivity implements View.OnClickListener, ReqListener {
    private final String mPageName = "MySelectionActivity";
    private static final int GET_MY_SELECTION_SUCCEED = 1;
    private static final int GET_MY_SELECTION_FAILED = 2;
    private static final int EXIT = 3;

    private TextView mTvMySelection;
    private TextView mTvArea;
    private Button mBtnEndChoose;
    private CountDownView mCountDownTimer;
    private LoadingDialog mLoading = null;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GET_MY_SELECTION_SUCCEED:
                    mLoading.dismiss();
                    OrderInfo orderInfo = (OrderInfo)msg.obj;
                    mTvArea.setText(orderInfo.getOrder_room_id() + "区域");
                    mTvMySelection.setText(orderInfo.getOrder_location_id());
                    mCountDownTimer.startCount(MySelectionActivity.this, Long.valueOf(orderInfo.getOrder_datetime()));
                    break;
                case GET_MY_SELECTION_FAILED:
                    mLoading.dismiss();
                    String tip = (String)msg.obj;
                    toShow(tip);
                    break;
                case EXIT:
                    Intent intent2 = new Intent(mContext, LoginActivity.class);
                    Utils.toLeftAnim(mContext, intent2, true);
                    FuWeiApplication.getInstance().exit();
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.selection_result);
        super.onCreate(savedInstanceState);
        FuWeiApplication.getInstance().addActivity(this);
    }

    @Override
    public void initView() {
        mTvMySelection = (TextView) findViewById(R.id.bianhao);
        mTvArea = (TextView) findViewById(R.id.region);
        mBtnEndChoose = (Button) findViewById(R.id.end_choose);

        mCountDownTimer = (CountDownView) findViewById(R.id.count_down_timer);
    }

    @Override
    public void initData() {
        setTitle(R.string.title_my_selection_text);
        String quyu = getString(R.string.fuweiquyu);
        mTvArea.setText(String.format(quyu, "456"));
        mLoading = new LoadingDialog(mContext);
        mLoading.showDialog("正在查询");
        NetWorkUtil.getMyNumber(this);

    }

    @Override
    public void initListener() {
        getLeftBtn().setOnClickListener(this);
        mBtnEndChoose.setOnClickListener(this);
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
        mCountDownTimer.onStop();
    }

    @Override
    protected void HandleLeftNavBtn() {
        Utils.toRightAnim(mContext);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            mBtnEndChoose.performClick();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
            case R.id.end_choose:
                toShow("选号结束");
                mHandler.sendEmptyMessageDelayed(EXIT,114);
                break;
            default:
                break;
        }
    }

    @Override
    public void onUpdate(Event event, Object obj) {
        Message msg = new Message();
        switch (event) {
            case EVENT_GET_MY_SELECTION_SUCCESS:
                OrderInfo orderInfo = ((ResultLoginInfo) obj).getOrderInfo();
                RoomInfo roomInfo =  ((ResultLoginInfo) obj).getRoomInfo();
                PosInfo posInfo =  ((ResultLoginInfo) obj).getPosInfo();
                UserInfo userInfo =  ((ResultLoginInfo) obj).getUserInfo();
                msg.what = GET_MY_SELECTION_SUCCEED;
                msg.obj = orderInfo;
                if (mHandler != null) {
                    mHandler.sendMessage(msg);
                }
                break;
            case EVENT_GET_MY_SELECTION_FAIL:
                String tip = (String)obj;
                msg.what = GET_MY_SELECTION_FAILED;
                msg.obj = tip;
                if (mHandler != null) {
                    mHandler.sendMessage(msg);
                }
                break;
            default:
                break;
        }
    }
}
