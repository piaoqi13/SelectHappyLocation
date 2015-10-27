package com.fuwei.selecthappylocation.activity;

import android.graphics.Color;
import android.graphics.Matrix;
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

/**
 * 高端定制
 */
public class AdvanceBookActivity extends BaseActivity {

    private MyHorizontalScrollView mMyHorizontalScrollView;
    private FrameLayout mSeatContainer;

    private List<RoomDataItem> mRoomDataItems;
    private LinearLayout mLlSeatRaw;
    private TextView mTvSelectedNumber;
    private int mDefWidth;

    private Button mBtnBeginSelection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.advance_book_view);
        mRoomDataItems = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            mRoomDataItems.add(new RoomDataItem(i + "", i, i + ""));
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        mDefWidth = getResources().getDimensionPixelSize(R.dimen.padding_20dp);
    }

    @Override
    public void initView() {
        mTvSelectedNumber = (TextView) findViewById(R.id.selected_number);
        mSeatContainer = (FrameLayout) findViewById(R.id.seat_container);
        mLlSeatRaw = (LinearLayout) findViewById(R.id.seatraw);
        mMyHorizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.room_scroll_view);
        List<IRoom> rooms = RoomInitUtils.initRooms(this, mRoomDataItems);
        mMyHorizontalScrollView.initViews(rooms);
        mMyHorizontalScrollView.setmOnRoomSelectedListener(mOnRoomSelectedListener);
        mMyHorizontalScrollView.focusToFirstChild();
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
            mSeatContainer.addView(mCurSeatTableView);
            mSeatContainer.addView(mLlSeatRaw);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }

    // 当视图改变时；
    private SeatTableView.OnViewChangeListener mOnViewChangeListener = new SeatTableView.OnViewChangeListener() {
        public void onViewChange(int rowSize, float focusY, float scaleFactor) {
            EasyLogger.d("AdvanceBookActivity", "AdvanceBookActivity:onViewChange "
                    + "threadId: " + Thread.currentThread().getId());
           onChange(rowSize, focusY, scaleFactor);
        }
    };

    private void onChange(int rowSize, float focusY, float scaleFactor) {
        mLlSeatRaw.removeAllViews();
        mLlSeatRaw.setPadding(getResources().getDimensionPixelSize(R.dimen.padding_1dp),(int) (focusY), 0, 0);  // 上下移动

        for (int i = 0; i < rowSize; i++) {
            TextView textView = new TextView(mActivity);
            textView.setText(i+"");
            textView.setTextSize(8.0f * scaleFactor);
            textView.setTextColor(Color.LTGRAY);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(new ViewGroup.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, (int)(mDefWidth * scaleFactor)));
            textView.setPadding(getResources().getDimensionPixelSize(R.dimen.padding_2dp),
                    0, getResources().getDimensionPixelSize(R.dimen.padding_2dp), 0);
            mLlSeatRaw.addView(textView);
        }
    }
}
