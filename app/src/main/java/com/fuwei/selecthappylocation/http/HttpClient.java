package com.fuwei.selecthappylocation.http;

import android.content.Context;

import com.fuwei.selecthappylocation.util.EasyLogger;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.protocol.HTTP;

/**
 * Created by collin on 2015-10-02.
 */
public class HttpClient {
    private static HttpClient mHttpClient = null;
    private AsyncHttpClient mAsyncHttpClient = null;

    private HttpClient() {
        super();
    }

    public static HttpClient getInstance() {
        if (mHttpClient == null) {
            mHttpClient = new HttpClient();
        }
        return mHttpClient;
    }

    public void init(Context context) {
        mAsyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
        //mAsyncHttpClient.setCookieStore(myCookieStore);
        mAsyncHttpClient.addHeader("charset", HTTP.UTF_8);
        mAsyncHttpClient.setTimeout(10000);
    }

    public void doWork(String url, RequestParams params, final HttpCallBack callBack) {
        // Log输出
        EasyLogger.i("HttpClient", "Url=" + url + (params.toString().equals("") ? "" : "?" + params.toString()));// 正确打印
        mAsyncHttpClient.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                EasyLogger.i("HttpClient", "JSON=" + content);
                callBack.succeed(statusCode, content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                callBack.failed(error, content);
            }

        });
    }

    // 上传图片
    public void doUpLoad(String url, RequestParams params, final HttpCallBack callBack) {
        // Log输出
        EasyLogger.i("HttpClient", "Url=" + url + "?" + params.toString());
        mAsyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String content) {
                EasyLogger.i("HttpClient", "JSON=" + content);
                callBack.succeed(statusCode, content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                callBack.failed(error, content);
            }
        });
    }

    // 请求结果接口
    public interface HttpCallBack {
        void succeed(int statusCode, String content);

        void failed(Throwable error, String content);
    }
}
