package com.fuwei.selecthappylocation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.dialog.LoadingDialog;
import com.fuwei.selecthappylocation.event.Event;
import com.fuwei.selecthappylocation.http.NetWorkUtil;
import com.fuwei.selecthappylocation.http.ReqListener;
import com.fuwei.selecthappylocation.util.EasyLogger;
import com.fuwei.selecthappylocation.util.Settings;
import com.fuwei.selecthappylocation.util.Utils;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.WheelVerticalView;
import antistatic.spinnerwheel.adapters.NumericWheelAdapter;

/**
 * Created by linky on 15-10-12.
 * Modify by collin on 2015-10-14
 */
public class RanSecActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = "RanSecActivity";
    private Button mBegin = null;
    private Button mConfirm = null;
    private boolean wheelScrolled = false;

    private WheelVerticalView mWheelVerticalView[] = new WheelVerticalView[4];
    private int[] mPassw = {R.id.passw_1, R.id.passw_2, R.id.passw_3, R.id.passw_4};
    private OnWheelScrollListener mScrolledListener[] = new OnWheelScrollListener[4];

    private String mResultNumber;
    private int isHaveTimeSelection = 0;
    private LoadingDialog mLoading = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.random_selection);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        // 初始化滑动监听器
        initScrollListener();
        for (int i = 0; i < 4; i++) {
            mWheelVerticalView[i] = (WheelVerticalView) findViewById(mPassw[i]);
            initWheel(mWheelVerticalView[i], i);
        }
        mBegin = (Button) findViewById(R.id.begin_selection);
        mConfirm = (Button) findViewById(R.id.confirm_selection);
        mBegin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 初始化滑动
                mixWheel();
                // 设置获取number
                onButtonClick();
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
        mConfirm.setOnClickListener(this);
    }

    private void mixWheel() {
        for (int i = 0; i < 4; i++) {
            mWheelVerticalView[i].scroll(-25 + (int) (Math.random() * 50), 2000 + i * 100);
        }
    }

    private void initWheel(AbstractWheel wheel, int index) {
        wheel.setViewAdapter(new NumericWheelAdapter(this, 0, 9));
        wheel.setCurrentItem(8);
        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(mScrolledListener[index]);
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

    private void initScrollListener() {
        mScrolledListener[0] = new OnWheelScrollListener() {
            public void onScrollingStarted(AbstractWheel wheel) {
                wheelScrolled = true;
            }

            public void onScrollingFinished(AbstractWheel wheel) {
                final int index = Integer.parseInt(mResultNumber.substring(0, 1));
                wheel.setCurrentItem(index);
            }
        };

        mScrolledListener[1] = new OnWheelScrollListener() {
            public void onScrollingStarted(AbstractWheel wheel) {
                wheelScrolled = true;
            }

            public void onScrollingFinished(AbstractWheel wheel) {
                final int index = Integer.parseInt(mResultNumber.substring(1, 2));
                wheel.setCurrentItem(index);
            }
        };

        mScrolledListener[2] = new OnWheelScrollListener() {
            public void onScrollingStarted(AbstractWheel wheel) {
                wheelScrolled = true;
            }

            public void onScrollingFinished(AbstractWheel wheel) {
                final int index = Integer.parseInt(mResultNumber.substring(2, 3));
                wheel.setCurrentItem(index);
            }
        };

        mScrolledListener[3] = new OnWheelScrollListener() {
            public void onScrollingStarted(AbstractWheel wheel) {
                wheelScrolled = true;
            }

            public void onScrollingFinished(AbstractWheel wheel) {
                final int index = Integer.parseInt(mResultNumber.substring(3, 4));
                wheel.setCurrentItem(index);
            }
        };
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
            case R.id.confirm_selection:
                mLoading = new LoadingDialog(mContext);
                mLoading.showDialog("正在提交");
                NetWorkUtil.submitRandomSelection(new ReqListener() {
                    @Override
                    public void onUpdate(Event event, final Object obj) {
                        switch (event) {
                            case EVENT_SUBMIT_RANDOM_SELECTION_SUCCESS:
                                mLoading.dismiss();
                                Intent intent = new Intent(mContext, MySelectionActivity.class);
                                Utils.toLeftAnim(mContext, intent, true);
                                break;
                            case EVENT_SUBMIT_RANDOM_SELECTION_FAIL:
                                mLoading.dismiss();
                                String tip = (String)obj;
                                toShow(tip);
                                break;
                        }

                    }
                }, Settings.getString(Settings.BODY.BODY_ID, "", true), mResultNumber);
                break;
            default:
                break;
        }
    }

    public void onButtonClick() {
        // 先获取 bodyId
        String bodyId = Settings.getString(Settings.BODY.BODY_ID, "", true);
        if (bodyId == null) {
            Toast.makeText(RanSecActivity.this, "身份证号为空", Toast.LENGTH_SHORT).show();
            return;
        }

        NetWorkUtil.randomSelection(new ReqListener() {
            @Override
            public void onUpdate(Event event, final Object obj) {
                switch (event) {
                    case EVENT_RANDOM_SELECTION_SUCCESS:
                        isHaveTimeSelection ++;// 第一次记录
                        String number = (String) obj;
                        EasyLogger.d("DebugLog", "number" + number);
                        // 设置到界面中去
                        if (number != null) {
                            StringBuilder sb = new StringBuilder();
                            if (number.length() < 4) {
                                int remainBits = 4 - number.length();
                                for (int i = 0; i < remainBits; i++) {
                                    sb.append("0");
                                }
                            }
                            sb.append(number);
                            mResultNumber = sb.toString();
                            EasyLogger.i("DebugLog", " resultNumber : " + mResultNumber);
                            // 暂时放在这里CollinWang1130
                            if (isHaveTimeSelection == 1) {
                                mBegin.setText("再选一次");
                                mConfirm.setVisibility(View.VISIBLE);
                            } else if (isHaveTimeSelection == 2) {
                                mBegin.setEnabled(false);
                                mConfirm.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                    case EVENT_RANDOM_SELECTION_FAIL:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RanSecActivity.this,(String) obj,Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                }
            }
        }, bodyId);
    }
}
