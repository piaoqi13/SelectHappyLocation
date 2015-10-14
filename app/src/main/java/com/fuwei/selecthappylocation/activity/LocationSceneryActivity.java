package com.fuwei.selecthappylocation.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fuwei.selecthappylocation.R;
import com.fuwei.selecthappylocation.util.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.onlineconfig.OnlineConfigAgent;

/**
 * created by collin on 2015-10-13.
 */
public class LocationSceneryActivity extends BaseActivity implements View.OnClickListener {
    private final String mPageName = "LocationSceneryActivity";
    private WebView mWvLocationScenery = null;

    private boolean isLoadImageAuto = true;
    private boolean isJavaScriptEnabled = true;
    private boolean isJavaScriptCanOpenWindowAuto = false;
    private boolean isRememberPassword = true;
    private boolean isSaveFormData = true;
    private boolean isLoadPageInOverviewMode = true;
    private boolean isUseWideViewPort = true;
    private boolean isLightTouch = false;
    private int minimumFontSize = 8;
    private int minimumLogicalFontSize = 8;
    private int defaultFontSize = 16;
    private int defaultFixedFontSize = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.location_scenery_activity);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mWvLocationScenery = (WebView) findViewById(R.id.wv_location_scenery);
        WebSettings webSettings = mWvLocationScenery.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUserAgentString(null);
        webSettings.setUseWideViewPort(isUseWideViewPort);
        webSettings.setLoadsImagesAutomatically(isLoadImageAuto);
        webSettings.setJavaScriptEnabled(isJavaScriptEnabled);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(isJavaScriptCanOpenWindowAuto);
        webSettings.setMinimumFontSize(minimumFontSize);
        webSettings.setMinimumLogicalFontSize(minimumLogicalFontSize);
        webSettings.setDefaultFontSize(defaultFontSize);
        webSettings.setDefaultFixedFontSize(defaultFixedFontSize);
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        webSettings.setLightTouchEnabled(isLightTouch);
        webSettings.setSaveFormData(isSaveFormData);
        webSettings.setSavePassword(isRememberPassword);
        webSettings.setLoadWithOverviewMode(isLoadPageInOverviewMode);
        webSettings.setNeedInitialFocus(false);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
    }

    @Override
    public void initData() {
        setTitle(R.string.title_location_scenery_text);
        String url = OnlineConfigAgent.getInstance().getConfigParams(mContext, "LocationSceneryUrl");
        mWvLocationScenery.loadUrl(url);
    }

    @Override
    public void initListener() {
        getLeftBtn().setOnClickListener(this);
        mWvLocationScenery.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void HandleLeftNavBtn() {
        Utils.toRightAnim(mContext);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                HandleLeftNavBtn();
                break;
            default:
                break;
        }
    }
}
