package com.fuwei.selecthappylocation.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;

import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.util.Utils;

import antistatic.spinnerwheel.AbstractWheel;

import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.NumericWheelAdapter;

/**
 * Created by linky on 15-10-12.
 * Modify by collin on 2015-10-14
 */
public class RanSecActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = "RanSecActivity";
    private Button mBegin = null;
    private boolean wheelScrolled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.random_selection);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        initWheel(R.id.passw_1);
        initWheel(R.id.passw_2);
        initWheel(R.id.passw_3);
        initWheel(R.id.passw_4);

        mBegin = (Button)findViewById(R.id.begin_selection);
        mBegin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mixWheel(R.id.passw_1);
                mixWheel(R.id.passw_2);
                mixWheel(R.id.passw_3);
                mixWheel(R.id.passw_4);
            }
        });
    }

    @Override
    public void initData() {
        setTitle(R.string.random_selection);
    }

    @Override
    public void initListener() {
        getLeftBtn().setOnClickListener(this);
    }

    private void mixWheel(int id) {
        AbstractWheel wheel = getWheel(id);
        wheel.scroll(-25 + (int) (Math.random() * 50), 2000);
    }

    private AbstractWheel getWheel(int id) {
        return (AbstractWheel) findViewById(id);
    }

    private void initWheel(int id) {
        AbstractWheel wheel = getWheel(id);
        wheel.setViewAdapter(new NumericWheelAdapter(this, 0, 9));
        //wheel.setCurrentItem((int) (Math.random() * 10));
        wheel.setCurrentItem(8);
        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
        wheel.setCyclic(true);
        wheel.setEnabled(false);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }

    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
            if (!wheelScrolled) {
                Log.d(TAG, "onChanged ");
            }
        }
    };

    private OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(AbstractWheel wheel) {
            wheelScrolled = true;
        }
        public void onScrollingFinished(AbstractWheel wheel) {
            wheelScrolled = false;
        }
    };

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
            default:
                break;
        }
    }
}
