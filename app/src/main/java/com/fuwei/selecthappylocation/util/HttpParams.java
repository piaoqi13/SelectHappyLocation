package com.fuwei.selecthappylocation.util;

import com.loopj.android.http.RequestParams;

/**
 * Created by collin on 2015-10-02.
 */
public class HttpParams {
    // 登录参数
    public static RequestParams getLoginParams(String bodyId) {
        RequestParams params = new RequestParams();
        params.put("bodyId", bodyId);
        return params;
    }

    // 生辰八字选号参数
    public static RequestParams getBirthdayNumberParams(String username, String birthday, String stime) {
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("userbirth", birthday);
        params.put("stime", stime);
        return params;
    }

    // 随机选号参数
    public static RequestParams getRandomNumberParams(String currentTime) {
        RequestParams params = new RequestParams();
        params.put("currentTime", currentTime);
        return params;
    }

    // 我的选号参数
    public static RequestParams getMyNumberParams(String username) {
        RequestParams params = new RequestParams();
        params.put("username", username);
        return params;
    }
}
