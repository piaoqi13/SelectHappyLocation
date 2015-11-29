package com.fuwei.selecthappylocation.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.model.IRoom;
import com.fuwei.selecthappylocation.model.RoomDataItem;
import com.fuwei.selecthappylocation.model.SeatMo;
import com.fuwei.selecthappylocation.util.EasyLogger;
import com.fuwei.selecthappylocation.util.RoomInitUtils;
import com.fuwei.selecthappylocation.util.Utils;
import com.fuwei.selecthappylocation.view.MyHorizontalScrollView;
import com.fuwei.selecthappylocation.view.SeatTableView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 高端定制
 */
public class AdvanceBookActivity extends BaseActivity implements View.OnClickListener {

    private MyHorizontalScrollView mMyHorizontalScrollView;
    private FrameLayout mSeatContainer;

    private List<RoomDataItem> mRoomDataItems;
    private LinearLayout mLlSeatRaw;
    private LinearLayout mLlSeatColumn;
    private TextView mTvSelectedNumber;
    private int mDefHeight;
    private int mDefWidth;
    private int mHeightGap;
    private int mWidthGap;

    public static SeatMo mSeatMoSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.advance_book_view);
        mRoomDataItems = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            mRoomDataItems.add(new RoomDataItem(i + "", i, i + ""));
        }

        mDefHeight = getResources().getDimensionPixelSize(R.dimen.def_height);
        mHeightGap = getResources().getDimensionPixelSize(R.dimen.height_gap);

        mDefWidth = getResources().getDimensionPixelSize(R.dimen.def_width);
        mWidthGap = getResources().getDimensionPixelSize(R.dimen.width_gap);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mTvSelectedNumber = (TextView) findViewById(R.id.selected_number);
        mSeatContainer = (FrameLayout) findViewById(R.id.seat_container);
        mLlSeatRaw = (LinearLayout) findViewById(R.id.seatraw);
        mLlSeatColumn = (LinearLayout) findViewById(R.id.seatcolumn);

        mMyHorizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.room_scroll_view);
        List<IRoom> rooms = RoomInitUtils.initRooms(this, mRoomDataItems);
        mMyHorizontalScrollView.initViews(rooms);
        mMyHorizontalScrollView.setmOnRoomSelectedListener(mOnRoomSelectedListener);
        mMyHorizontalScrollView.focusToFirstChild();
    }

    @Override
    public void initData() {
        setTitle(R.string.title_advance_book_text);
    }

    @Override
    public void initListener() {
        getLeftBtn().setOnClickListener(this);
    }

    private SeatTableView mCurSeatTableView;
    private MyHorizontalScrollView.OnRoomSelectedListener mOnRoomSelectedListener
            = new MyHorizontalScrollView.OnRoomSelectedListener() {
        public void onRoomSelected(IRoom room) {
            if(mSeatContainer.getChildCount() > 0) {
                mSeatContainer.removeAllViews();
            }
            mCurSeatTableView = (SeatTableView) room.getLayoutView();
            mCurSeatTableView.setmOnViewChangeListener(mOnViewChangeListener);
            mCurSeatTableView.setmOnSeatSelectedListener(mOnSeatSelectedListener);
            mSeatContainer.addView(mCurSeatTableView);
            mSeatContainer.addView(mLlSeatRaw);
            mSeatContainer.addView(mLlSeatColumn);
        }
    };

    private SeatTableView.OnSeatSelectedListener mOnSeatSelectedListener
            = new SeatTableView.OnSeatSelectedListener() {
        public void onSeatSelected(SeatMo seatMo) {
            mTvSelectedNumber.setText(seatMo.seatName);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }

    // 当视图改变时；
    private SeatTableView.OnViewChangeListener mOnViewChangeListener = new SeatTableView.OnViewChangeListener() {
        public void onViewChange(int columnSize, float focusX, int rowSize, float focusY, float scaleFactor) {
            EasyLogger.d("AdvanceBookActivity", "AdvanceBookActivity:onViewChange "
                    + "threadId: " + Thread.currentThread().getId());
           onChange(columnSize, focusX, rowSize, focusY, scaleFactor);
        }
    };

    private void onChange(int columnSize, float focusX, int rowSize, float focusY, float scaleFactor) {
        // 行号处理
        mLlSeatRaw.removeAllViews();
        mLlSeatRaw.setPadding(0, (int) focusY - mHeightGap, 0, 0);  // 上下移动

        for (int i = 0; i < rowSize; i++) {
            TextView textView = new TextView(mActivity);
            textView.setText(i + "");
            textView.setTextSize(8.0f * scaleFactor);
            textView.setTextColor(Color.LTGRAY);
            textView.setGravity(Gravity.CENTER);

            int defHeight = 0;
            if(i == 0 || i == rowSize - 1) {
                defHeight = (int) (mDefHeight * scaleFactor + mHeightGap / 2);
            } else {
                defHeight = (int) (mDefHeight * scaleFactor + mHeightGap);
            }

            textView.setLayoutParams(new ViewGroup.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, defHeight));
            mLlSeatRaw.addView(textView);
        }

        // 列号处理
        mLlSeatColumn.removeAllViews();
        mLlSeatColumn.setPadding((int) focusX - mWidthGap, 0, 0, 0);

        for (int j = 0; j < columnSize; j++) {
            TextView textView = new TextView(mActivity);
            textView.setText(j + "");
            textView.setTextSize(8.0f * scaleFactor);
            textView.setTextColor(Color.LTGRAY);
            textView.setGravity(Gravity.CENTER);

            int defWidth = 0;
            if(j == 0 || j == columnSize - 1) {
                defWidth = (int) (mDefWidth * scaleFactor + mWidthGap / 2);
            } else {
                defWidth = (int) (mDefWidth * scaleFactor + mWidthGap);
            }

            textView.setLayoutParams(new ViewGroup.LayoutParams(defWidth,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            mLlSeatColumn.addView(textView);
        }
    }

    public  int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
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
            default:
                break;
        }
    }
}
