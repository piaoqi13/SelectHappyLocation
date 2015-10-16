package com.fuwei.selecthappylocation.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuwei.selecthappylocation.R;

/**
 * 高端定制
 */
public class AdvanceBookActivity extends BaseActivity {

    private TextView mTvSelectedNumber;
    private LinearLayout mLlContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.advance_book_view);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {

        mTvSelectedNumber = (TextView) findViewById(R.id.selected_number);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
