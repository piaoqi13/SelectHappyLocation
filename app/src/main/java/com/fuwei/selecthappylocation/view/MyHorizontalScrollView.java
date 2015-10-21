package com.fuwei.selecthappylocation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.model.IRoom;

import java.util.List;

/**
 *
 */
public class MyHorizontalScrollView extends HorizontalScrollView {

    private LinearLayout mRoomContainer;
    private List<IRoom> mRooms;
    private LayoutInflater mLayoutInflater;
    private View mCurrentSelectedView;

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRoomContainer = (LinearLayout) findViewById(R.id.room_container);
    }

    // 初始化界面数据
    public void initViews(List<IRoom> rooms) {

        mRooms = rooms;
        int size = rooms.size();

        for (int i = 0; i < size; i++) {
            View view = createRoomView(i, mRooms.get(i));
            view.setOnClickListener(mOnClickListener);
            view.setTag(mRooms.get(i));
            mRoomContainer.addView(view);
        }
    }

    private View createRoomView(int index, IRoom room) {
        TextView tv = (TextView) mLayoutInflater.inflate(R.layout.room_select_view, mRoomContainer, false);
        tv.setText(room.getRoomNumber()+"");

        int mlRes = R.dimen.margin_15dp;
        if(index == 0) {
            mlRes = R.dimen.margin_10dp;
        }

        int marginLeft = (int) getResources().getDimension(mlRes);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv.getLayoutParams();
        params.setMargins(marginLeft,0,0,0);

        return tv;
    }

    // 聚焦到第一个子 View
    public void focusToFirstChild() {
        mCurrentSelectedView = mRoomContainer.getChildAt(0);
        mCurrentSelectedView.callOnClick();
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            // 改变背景色；
            changeItemBacnground(v);

            IRoom room = (IRoom) v.getTag();
            if(mOnRoomSelectedListener != null) {
                mOnRoomSelectedListener.onRoomSelected(room);
            }
        }
    };

    private void changeItemBacnground(View view) {

        if(mCurrentSelectedView != null) {
            mCurrentSelectedView.setBackgroundResource(R.mipmap.room_unselected);
            view.setBackgroundResource(R.mipmap.room_selected);
            mCurrentSelectedView = view;
        }
    }

    private OnRoomSelectedListener mOnRoomSelectedListener;
    public void setmOnRoomSelectedListener(OnRoomSelectedListener onRoomSelectedListener) {
        mOnRoomSelectedListener = onRoomSelectedListener;
    }
    public interface OnRoomSelectedListener {
        public void onRoomSelected(IRoom room);
    }
}
