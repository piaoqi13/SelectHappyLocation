package com.fuwei.selecthappylocation.model;

/**
 * Created by collin on 2015-10-02.
 */
public class ResultLoginInfo {
    private int result = 0;
    private LoginInfo data = null;
    private String msg = null;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public LoginInfo getData() {
        return data;
    }

    public void setData(LoginInfo data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
