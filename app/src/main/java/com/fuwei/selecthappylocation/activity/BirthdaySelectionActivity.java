package com.fuwei.selecthappylocation.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fuwei.selecthappylocation.FuWeiApplication;
import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.dialog.LoadingDialog;
import com.fuwei.selecthappylocation.dialog.SpinnerWheelOneDialog;
import com.fuwei.selecthappylocation.dialog.SpinnerWheelThreeDialog;
import com.fuwei.selecthappylocation.event.Event;
import com.fuwei.selecthappylocation.http.NetWorkUtil;
import com.fuwei.selecthappylocation.http.ReqListener;
import com.fuwei.selecthappylocation.util.Utils;

import java.util.Calendar;

/**
 * created by collin on 2015-10-13.
 */
public class BirthdaySelectionActivity extends BaseActivity implements View.OnClickListener, ReqListener {
    private static final int GET_BIRTHDAY_SELECT_SUCCEED = 1;
    private static final int GET_BIRTHDAY_SELECT_FAILED = 2;

    private EditText mEdtName = null;
    private TextView mTvBirthday = null;
    private TextView mTvBirthdayDetail = null;
    private Button mBtnStartSelect = null;

    private SpinnerWheelThreeDialog mSpinnerWheelThreeDialog = null;
    private SpinnerWheelOneDialog mSpinnerWheelOneDialog = null;

    private LoadingDialog mLoading = null;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GET_BIRTHDAY_SELECT_SUCCEED:
                    mLoading.dismiss();
                    //Intent intent = new Intent(mContext, SelectionDetailActivity.class);
                    //Utils.toLeftAnim(mContext, intent, false);
                    break;
                case GET_BIRTHDAY_SELECT_FAILED:
                    mLoading.dismiss();
                    String tip = (String)msg.obj;
                    toShow(tip);
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.birthday_selection_activity);
        super.onCreate(savedInstanceState);
        FuWeiApplication.getInstance().addActivity(this);
    }

    @Override
    public void initView() {
        mEdtName = (EditText) findViewById(R.id.edt_real_name);
        mTvBirthday = (TextView) findViewById(R.id.tv_birthday);
        mTvBirthdayDetail = (TextView) findViewById(R.id.tv_birthday_detail);
        mBtnStartSelect = (Button) findViewById(R.id.btn_start_select);
    }

    @Override
    public void initData() {
        setTitle(R.string.title_birthday_text);
    }

    @Override
    public void initListener() {
        getLeftBtn().setOnClickListener(this);
        mTvBirthday.setOnClickListener(this);
        mTvBirthdayDetail.setOnClickListener(this);
        mBtnStartSelect.setOnClickListener(this);
    }

    @Override
    protected void HandleLeftNavBtn() {
        Utils.toRightAnim(mContext);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                HandleLeftNavBtn();
                break;
            case R.id.tv_birthday:
                mSpinnerWheelThreeDialog = new SpinnerWheelThreeDialog(this, FuWeiApplication.getInstance().mYearArray, FuWeiApplication.getInstance().mMonthArray, FuWeiApplication.getInstance().mDay30Array);
                Calendar calendar = Calendar.getInstance();
                mSpinnerWheelThreeDialog.setDefault(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
                mSpinnerWheelThreeDialog.show();
                mSpinnerWheelThreeDialog.setConfirmListener(this);
                break;
            case R.id.btn_confirm:
                mSpinnerWheelThreeDialog.dismiss();
                String tipOne = mSpinnerWheelThreeDialog.getResult();
                mTvBirthday.setText(tipOne);
                break;
            case R.id.tv_birthday_detail:
                mSpinnerWheelOneDialog = new SpinnerWheelOneDialog(this, FuWeiApplication.getInstance().mShiChenArray);
                mSpinnerWheelOneDialog.show();
                mSpinnerWheelOneDialog.setConfirmListener(this);
                break;
            case R.id.btn_one_wheel_confirm:
                mSpinnerWheelOneDialog.dismiss();
                String tip = mSpinnerWheelOneDialog.getResult();
                mTvBirthdayDetail.setText(tip);
                break;
            case R.id.btn_start_select:
                if (mEdtName.getText().toString().equals("")) {
                    toShow("请填写你的姓名");
                    return;
                } else if (mTvBirthday.getText().toString().equals("")) {
                    toShow("请选择你的生日");
                    return;
                } else if (mTvBirthdayDetail.getText().toString().equals("")) {
                    toShow("请选择你的生日时辰");
                    return;
                }
                mLoading = new LoadingDialog(mContext);
                mLoading.showDialog("加载中");
                // 接口走起
                NetWorkUtil.getBirthdaySelection(BirthdaySelectionActivity.this,mEdtName.getText().toString(),
                        mTvBirthday.getText().toString(), mTvBirthdayDetail.getText().toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void onUpdate(Event event, Object obj) {
        Message msg = new Message();
        switch (event) {
            case EVENT_GET_BIRTHDAY_SELECT_SUCCESS:
                msg.what = GET_BIRTHDAY_SELECT_SUCCEED;
                if (mHandler != null) {
                    mHandler.sendMessage(msg);
                }
                break;
            case EVENT_GET_BIRTHDAY_SELECT_FAIL:
                String tip = (String)obj;
                msg.what = GET_BIRTHDAY_SELECT_FAILED;
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
