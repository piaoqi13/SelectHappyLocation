package com.fuwei.selecthappylocation.model;

/**
 * Created by collin on 2015-10-13.
 */
public class ResultBirthdaySelectInfo {
    private int result = 0;
    private BirthdaySelectLocation data = null;
    private String msg = null;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public BirthdaySelectLocation getData() {
        return data;
    }

    public void setData(BirthdaySelectLocation data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
