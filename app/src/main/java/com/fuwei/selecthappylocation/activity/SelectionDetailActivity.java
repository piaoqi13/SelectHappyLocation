package com.fuwei.selecthappylocation.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.event.Event;
import com.fuwei.selecthappylocation.http.ReqListener;
import com.fuwei.selecthappylocation.util.Utils;

/**
 * created by collin on 2015-10-13.
 */
public class SelectionDetailActivity extends BaseActivity implements View.OnClickListener, ReqListener {
    private static final int GET_SELECTION_DETAIL_SUCCEED = 1;
    private static final int GET_SELECTION_DETAIL_FAILED = 2;

    private TextView mTvConfirmSelection = null;
    private TextView mTvCancelSelection = null;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GET_SELECTION_DETAIL_SUCCEED:
                    break;
                case GET_SELECTION_DETAIL_FAILED:
                    String tip = (String)msg.obj;
                    toShow(tip);
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.selection_detail_activity);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mTvConfirmSelection = (TextView) findViewById(R.id.tv_confirm_selection);
        mTvCancelSelection = (TextView) findViewById(R.id.tv_cancel_selection);
    }

    @Override
    public void initData() {
        setTitle(R.string.title_selection_detail_text);
    }

    @Override
    public void initListener() {
        getLeftBtn().setOnClickListener(this);
        mTvConfirmSelection.setOnClickListener(this);
        mTvCancelSelection.setOnClickListener(this);
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
            case R.id.tv_confirm_selection:
                Intent intent = new Intent(mContext, MySelectionActivity.class);
                Utils.toLeftAnim(mContext, intent, true);
                break;
            case R.id.tv_cancel_selection:
                HandleLeftNavBtn();
                break;
            default:
                break;
        }
    }

    @Override
    public void onUpdate(Event event, Object obj) {
        Message msg = new Message();
        switch (event) {
            case EVENT_GET_SELECTION_DETAIL_SUCCESS:
                break;
            case EVENT_GET_SELECTION_DETAIL_FAIL:
                String tip = (String)obj;
                msg.what = GET_SELECTION_DETAIL_SUCCEED;
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
