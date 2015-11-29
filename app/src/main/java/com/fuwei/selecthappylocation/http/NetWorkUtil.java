package com.fuwei.selecthappylocation.http;

import com.fuwei.selecthappylocation.event.Event;
import com.fuwei.selecthappylocation.http.HttpClient.HttpCallBack;
import com.fuwei.selecthappylocation.model.ResultLoginInfo;
import com.fuwei.selecthappylocation.util.Constant;
import com.fuwei.selecthappylocation.util.EasyLogger;
import com.fuwei.selecthappylocation.util.HttpParams;
import com.google.gson.Gson;

import org.apache.commons.httpclient.util.TimeoutController.TimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created by collin on 2015-10-02.
 */
public class NetWorkUtil {
    // 登录
    public static void login(final ReqListener listener, String bodyId) {
        HttpClient.getInstance().doWork(Constant.getLoginUrl(), HttpParams.getLoginParams(bodyId), new HttpCallBack() {
            @Override
            public void succeed(int statusCode, String content) {
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    if (jsonObject.optInt("error") == 1) {// error等于1flag等于-1
                        listener.onUpdate(Event.EVENT_LOGIN_FAIL, jsonObject.optString("msg"));
                    } else {
                        if (jsonObject.optInt("flag") == 0) {// 有订单,无论是否支付字段一样
                            listener.onUpdate(Event.EVENT_LOGIN_SUCCESS, null);
                        } else {// flag等于1或2都走此分支
                            Gson gson = new Gson();
                            Object obj = gson.fromJson(jsonObject.toString(), ResultLoginInfo.class);
                            listener.onUpdate(Event.EVENT_LOGIN_SUCCESS, obj);
                        }
                    }
                } catch (JSONException e) {
                    EasyLogger.e("NetWorkUtil", "loginCatch=", e);
                    listener.onUpdate(Event.EVENT_LOGIN_FAIL, e.toString());
                }
            }

            @Override
            public void failed(Throwable error, String content) {
                EasyLogger.e("NetWorkUtil", "login failed------error=" + error + "；content=" + content);
                listener.onUpdate(Event.EVENT_LOGIN_FAIL, setFailedInfo(error));
            }
        });
    }

    // 非正常失败返回码
    private static String setFailedInfo(Throwable error) {
        String content = "服务器未知错误";// 404及其他
        if (error instanceof TimeoutException) {
            content = "连接超时，请重试";
        } else if (error instanceof SocketTimeoutException) {
            content = "连接超时，请重试";
        } else if (error instanceof ConnectException) {
            content = "未能连接到服务器";
        }
        return content;
    }
}
