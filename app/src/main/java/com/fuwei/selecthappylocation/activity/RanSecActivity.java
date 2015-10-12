package com.fuwei.selecthappylocation.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;

import com.fuwei.selecthappylocation.R;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.NumericWheelAdapter;

/**
 * Created by linky on 15-10-12.
 */
public class RanSecActivity extends BaseActivity {

    private final static String TAG = "RanSecActivity";

    private Button mBegin;

    // Wheel scrolled flag
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

    /**
     * Mixes spinnerwheel
     * @param id the spinnerwheel id
     */
    private void mixWheel(int id) {
        AbstractWheel wheel = getWheel(id);
        wheel.scroll(-25 + (int) (Math.random() * 50), 2000);
    }

    /**
     * Returns spinnerwheel by Id
     * @param id the spinnerwheel Id
     * @return the spinnerwheel with passed Id
     */
    private AbstractWheel getWheel(int id) {
        return (AbstractWheel) findViewById(id);
    }

    /**
     * Initializes spinnerwheel
     * @param id the spinnerwheel wheel Id
     */
    private void initWheel(int id) {
        AbstractWheel wheel = getWheel(id);
        wheel.setViewAdapter(new NumericWheelAdapter(this, 0, 9));
        wheel.setCurrentItem((int) (Math.random() * 10));
        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
        wheel.setCyclic(true);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }

    // Wheel changed listener
    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
            if (!wheelScrolled) {
                Log.d(TAG, "onChanged ");
            }
        }
    };

    // Wheel scrolled listener
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(AbstractWheel wheel) {
            wheelScrolled = true;
        }
        public void onScrollingFinished(AbstractWheel wheel) {
            wheelScrolled = false;
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }
}
