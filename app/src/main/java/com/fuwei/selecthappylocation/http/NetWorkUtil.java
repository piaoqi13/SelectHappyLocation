package com.fuwei.selecthappylocation.http;

import com.fuwei.selecthappylocation.event.Event;
import com.fuwei.selecthappylocation.http.HttpClient.HttpCallBack;
import com.fuwei.selecthappylocation.model.ResultLoginInfo;
import com.fuwei.selecthappylocation.util.Constant;
import com.fuwei.selecthappylocation.util.EasyLogger;
import com.fuwei.selecthappylocation.util.HttpParams;
import com.fuwei.selecthappylocation.util.Settings;
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

    private static final String TAG = "NetWorkUtil";

    // 登录
    public static void login(final ReqListener listener, final String bodyId) {
        HttpClient.getInstance().doWork(Constant.getLoginUrl(), HttpParams.getLoginParams(bodyId), new HttpCallBack() {
            @Override
            public void succeed(int statusCode, String content) {

                EasyLogger.i(TAG, "succeed");

                try {
                    JSONObject jsonObject = new JSONObject(content);
                    if (jsonObject.optInt("error") == 1) {// error等于1flag等于-1

                        EasyLogger.i(TAG, "succeed0");

                        listener.onUpdate(Event.EVENT_LOGIN_FAIL, jsonObject.optString("msg"));
                    } else {

                        // TODO 将 bodyId 存到 Settings 中去
                        Settings.setString(Settings.BODY.BODY_ID, bodyId, true);

                        if (jsonObject.optInt("flag") == 0) {   // 有订单,无论是否支付字段一样
                            listener.onUpdate(Event.EVENT_LOGIN_SUCCESS, null);

                            EasyLogger.i(TAG, "succeed1");

                        } else {// flag等于1或2都走此分支

                            EasyLogger.i(TAG, "succeed2");

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
                EasyLogger.e(TAG, "login failed------error=" + error + "；content=" + content);
                listener.onUpdate(Event.EVENT_LOGIN_FAIL, setFailedInfo(error));
            }
        });
    }

    // 随机选号
    public static void randomSelection(final ReqListener listener, String bodyId) {
        HttpClient.getInstance().doWork(Constant.getRandomNumber(), HttpParams.getLoginParams(bodyId), new HttpCallBack() {

            @Override
            public void succeed(int statusCode, String content) {

                try {
                    JSONObject jsonObject = new JSONObject(content);

                    if (jsonObject.optInt("error") == 1) {  // 出错
                        listener.onUpdate(Event.EVENT_RANDOM_SELECTION_FAIL, jsonObject.optString("msg"));
                    } else {    // 正常

                        int count = jsonObject.optInt("count");         // 次数
                        String number = jsonObject.optString("number"); // 房号
                        int flag = jsonObject.optInt("flag");           // 0 表示新用户

                        if (number != null) {
                            listener.onUpdate(Event.EVENT_RANDOM_SELECTION_SUCCESS, number);
                        }

                        if (flag == 1 || flag == 2) { // 当flag =1或者flag = 2说明已经有了订单，直接到详情页即可
                            Gson gson = new Gson();
                            Object obj = gson.fromJson(jsonObject.toString(), ResultLoginInfo.class);
                            listener.onUpdate(Event.EVENT_RANDOM_SELECTION_HAS_ORDER, obj);
                        }

                    }
                } catch (JSONException e) {
                    EasyLogger.e("NetWorkUtil", "random Catch=", e);
                    listener.onUpdate(Event.EVENT_RANDOM_SELECTION_FAIL, e.toString());
                }

            }

            @Override
            public void failed(Throwable error, String content) {
                EasyLogger.e("NetWorkUtil", "random failed------error=" + error + "；content=" + content);
                listener.onUpdate(Event.EVENT_RANDOM_SELECTION_FAIL, setFailedInfo(error));
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
