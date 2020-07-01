package com.vaapglkns;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.vaapglkns.utils.Constant;

//FOR REDIRECTION
//Bundle bundle = new Bundle();
//        bundle.putString(Constant.bURL, "http://asiafora.com/");
//        bundle.putString(Constant.bTITLE, "Terms of service");
//        startNewActivity(new Intent(getActivity(), WebViewActivity.class), bundle);

public class WebViewActivity extends BaseActivity {
    private WebView webView;
    private ProgressBar progressBar;

    private boolean loadingFinished = true;
    private boolean redirect = false;
    private String url;
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initBackPress(true);
        getBundleData();
        initializeWidget();
        setWidgetOperations();
        setData();
        onCreateActions();
        setWebView();
    }

    //region FOR INITIALIZE WIDGET
    private void initializeWidget() {
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
    }
    //endregion

    //region FOR GET BUNDLE DATA
    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        this.url = bundle.getString(Constant.bURL, "http://google.com/");
        this.title = bundle.getString(Constant.bTITLE, getResources().getString(R.string.app_name));
    }
    //endregion

    //region FOR SET WEB VIEW
    private void setWebView() {
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progressBar.setProgress((progress), true);
                } else {
                    progressBar.setProgress(progress);
                }
            }
        });
        webView.loadUrl(url);
    }
    //endregion

    //region FOR WEB-VIEW-CLIENT
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!loadingFinished) {
                redirect = true;
            }
            loadingFinished = false;
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            loadingFinished = false;
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!redirect) {
                loadingFinished = true;
            }
            if (loadingFinished && !redirect) {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.GONE);
            } else {
                redirect = false;
            }
        }
    }
    //endregion

    //region FOR ON CREATE ACTIONS
    private void onCreateActions() {
    }
    //endregion

    //region FOR SET DATA
    @SuppressLint("SetTextI18n")
    private void setData() {
        setTitleText(title);
    }
    //endregion

    //region FOR SET WIDGET OPERATIONS
    private void setWidgetOperations() {
    }
    //endregion

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
