package com.fuwei.selecthappylocation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.model.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linky on 15-10-20.
 */
public class MyHorizontalScrollView extends HorizontalScrollView {

    private LinearLayout mRoomContainer;
    private List<Room> mRooms;
    private LayoutInflater mLayoutInflater;

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRoomContainer = (LinearLayout) findViewById(R.id.room_container);
        initData();
        initView();
    }

    private void initData() {

        mRooms = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Room room = new Room();
            room.setName(i+"");;
            mRooms.add(room);
        }
    }

    private void initView() {

        for (int i = 0; i < mRooms.size(); i++) {
            View view = createRoomView(i, mRooms.get(i));
            mRoomContainer.addView(view);
        }
    }

    private View createRoomView(int index, Room room) {
        TextView tv = (TextView) mLayoutInflater.inflate(R.layout.room_select_view, mRoomContainer, false);
        tv.setText(room.getName());

        int mlRes = R.dimen.margin_15dp;
        if(index == 0) {
            mlRes = R.dimen.margin_10dp;
        }

        int marginLeft = (int) getResources().getDimension(mlRes);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv.getLayoutParams();
        params.setMargins(marginLeft,0,0,0);

        return tv;
    }
}
