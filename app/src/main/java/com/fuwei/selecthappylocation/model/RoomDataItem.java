package com.fuwei.selecthappylocation.model;


public class RoomDataItem {

    private String mRoomId;
    private int mRoomNumber;
    private String mRoomName;

    public RoomDataItem(String id, int number, String name) {
        mRoomId = id;
        mRoomNumber = number;
        mRoomName = name;
    }

    public String getmRoomId() {
        return mRoomId;
    }

    public void setmRoomId(String mRoomId) {
        this.mRoomId = mRoomId;
    }

    public String getmRoomName() {
        return mRoomName;
    }

    public void setmRoomName(String mRoomName) {
        this.mRoomName = mRoomName;
    }

    public int getmRoomNumber() {
        return mRoomNumber;
    }

    public void setmRoomNumber(int mRoomNumber) {
        this.mRoomNumber = mRoomNumber;
    }
}
