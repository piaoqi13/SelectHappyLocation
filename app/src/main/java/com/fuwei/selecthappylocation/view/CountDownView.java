package com.fuwei.selecthappylocation.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.fuwei.selecthappylocation.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 倒计时规则：
 * 1. 第一次选好号时，将当前时间放入 SP 中；
 * 2. 每次进来时，先判断 SP 中是否有对应的时间，然后开始倒计时；
 */
public class CountDownView extends TextView {
    /**
     * 存放下单时间的文件
     */
    private final static String TIME_REMAIN = "time_remain";
    /**
     * 下单时间
     */
    private final static String ORDER_TIME = "order_time";
    /**
     * 两个小时的时间戳
     */
    private final static long TWO_HOURS = 7200;
    /**
     * 时分秒
     */
    private int mSecond, mMinute, mHour;

    private Timer mTimer;
    private Context mContext;

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mTimer = new Timer();
    }

    public void startCount(Context context, long orderTime) {
        // 首先判断是否存在倒计时
        long remain;
        if (orderTime != -1) {
            remain = TWO_HOURS + orderTime - System.currentTimeMillis()/1000;
        } else {
            remain = TWO_HOURS;
        }
        Log.i("CollinWang","remain=" + remain);
        display(remain);
    }

    public void onStop() {
        mTimer.cancel();
        mTimer = null;
    }

    private void display(long remain) {
        // 如果还没结束；
        if (remain > 0) {
            mHour = (int) (remain / 3600);
            mMinute = (int) (remain % 3600 / 60);
            mSecond = (int) (remain % 3600 % 60);
            mTimer.schedule(mTimerTask, 1000, 1000);
        } else {
            setText(R.string.end_count_down);
        }
    }


    private TimerTask mTimerTask = new TimerTask() {
        public void run() {
            calculate();
        }
    };

    /**
     * 倒计时计算
     */
    private void calculate() {
        if (mSecond == 0) {
            if (mMinute == 0) {
                if (mHour == 0) {
                    // end
                    mHandler.sendEmptyMessage(0);
                    return;
                } else {
                    mHour--;
                    mMinute = 59;
                    mSecond = 60;
                }
            } else {
                mMinute--;
                mSecond = 60;
            }
        }
        mSecond--;
        mHandler.sendEmptyMessage(1);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    setText(R.string.end_count_down);
                    break;
                case 1:
                    String str = mContext.getString(R.string.valid_time_remain);
                    setText(String.format(str, mHour,
                            mMinute < 10 ? "0" + mMinute : mMinute + "",
                            mSecond < 10 ? "0" + mSecond : mSecond + ""));
                    break;
            }
        }
    };
}
