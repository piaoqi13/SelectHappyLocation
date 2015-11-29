package com.fuwei.selecthappylocation.model;

/**
 * Created by collin on 2015-10-02.
 * modified by collin on 2015-11-29.
 */
public class ResultLoginInfo {
    private OrderInfo orderInfo = null;
    private RoomInfo roomInfo = null;
    private PosInfo posInfo = null;
    private UserInfo userInfo = null;

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }

    public PosInfo getPosInfo() {
        return posInfo;
    }

    public void setPosInfo(PosInfo posInfo) {
        this.posInfo = posInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
