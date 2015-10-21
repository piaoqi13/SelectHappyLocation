package com.fuwei.selecthappylocation.activity;

import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.model.IRoom;
import com.fuwei.selecthappylocation.model.RoomDataItem;
import com.fuwei.selecthappylocation.model.SeatMo;
import com.fuwei.selecthappylocation.util.RoomInitUtils;
import com.fuwei.selecthappylocation.view.MyHorizontalScrollView;
import com.fuwei.selecthappylocation.view.SeatTableView;

import java.util.ArrayList;
import java.util.List;

/**
 * 高端定制
 */
public class AdvanceBookActivity extends BaseActivity {

    private Matrix mMatrix = new Matrix();
    private float mPreScaleFactor = 1.0f;
    private float mScaleFactor = 1.0f;
    private float mPreFocusX = 0.f;
    private float mFocusX = 0.f;
    private float mPreFocusY = 0.f;
    private float mFocusY = 0.f;

    private TextView mTvSelectedNumber;
    private SeatTableView mSeatTableView;
    private SeatMo[][] mSeatTable;
    private int mDefWidth;
    private LinearLayout mRowView;

    private MyHorizontalScrollView mMyHorizontalScrollView;
    private FrameLayout mSeatContainer;

    private List<RoomDataItem> mRoomDataItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.advance_book_view);
        mRoomDataItems = new ArrayList<>();
        for(int i = 0; i < 8; i++) {
            mRoomDataItems.add(new RoomDataItem(i+"",i,i+""));
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {

        mTvSelectedNumber = (TextView) findViewById(R.id.selected_number);
        mSeatContainer = (FrameLayout) findViewById(R.id.seat_container);

        mMyHorizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.room_scroll_view);
        List<IRoom> rooms = RoomInitUtils.initRooms(this, mRoomDataItems);
        mMyHorizontalScrollView.initViews(rooms);
        mMyHorizontalScrollView.setmOnRoomSelectedListener(mOnRoomSelectedListener);
        mMyHorizontalScrollView.focusToFirstChild();

    }

    private MyHorizontalScrollView.OnRoomSelectedListener mOnRoomSelectedListener
            = new MyHorizontalScrollView.OnRoomSelectedListener() {
        public void onRoomSelected(IRoom room) {
            if(mSeatContainer.getChildCount() > 0) {
                mSeatContainer.removeViewAt(0);
            }
            mSeatContainer.addView(room.getLayoutView());
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }
}
