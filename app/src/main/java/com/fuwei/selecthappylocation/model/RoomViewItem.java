package com.fuwei.selecthappylocation.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.fuwei.selecthappylocation.view.SeatLayoutView;

public class RoomViewItem implements IRoom {

    private Context mContext;
    private String mName;
    private String mRoomId;
    private int mRoomNumber;
    private View mLayoutView;

    public RoomViewItem(Context context, RoomDataItem roomDataItem) {
        mContext = context;
        mName = roomDataItem.getmRoomName();
        mRoomId = roomDataItem.getmRoomId();
        mRoomNumber = roomDataItem.getmRoomNumber();
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getRoomId() {
        return mRoomId;
    }

    @Override
    public int getRoomNumber() {
        return mRoomNumber;
    }

    @Override
    public View getLayoutView() {
        if(mLayoutView == null)
            mLayoutView = new SeatLayoutView(mContext);
        return mLayoutView;
    }
}
