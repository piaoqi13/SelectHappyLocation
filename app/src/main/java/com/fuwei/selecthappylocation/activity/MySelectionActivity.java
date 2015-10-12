package com.fuwei.selecthappylocation.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.view.CountDownView;

public class MySelectionActivity extends BaseActivity {

    private TextView mTvMySelection;
    private TextView mTvArea;
    private Button mBtnEndChoose;
    private CountDownView mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.selection_result);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mTvMySelection = (TextView) findViewById(R.id.bianhao);
        mTvArea = (TextView) findViewById(R.id.region);
        mBtnEndChoose = (Button) findViewById(R.id.end_choose);

        mCountDownTimer = (CountDownView) findViewById(R.id.count_down_timer);
        mCountDownTimer.startCount(this);
    }

    @Override
    public void initData() {

        String quyu = getString(R.string.fuweiquyu);
        mTvArea.setText(String.format(quyu, "456"));

    }

    @Override
    public void initListener() {
        mBtnEndChoose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCountDownTimer.onStop();
    }
}
