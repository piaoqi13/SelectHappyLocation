package com.fuwei.selecthappylocation.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.util.Utils;

/**
 * Created by collin on 2015-10-03.
 */
public class StartPageActivity extends Activity {
    private Context mContext = null;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(mContext, MainActivity.class);
            Utils.toLeftAnim(mContext, intent, true);
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.start_page_activity);
        mHandler.sendEmptyMessageDelayed(1, 2 * 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
