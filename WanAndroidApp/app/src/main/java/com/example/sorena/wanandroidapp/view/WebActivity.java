package com.example.sorena.wanandroidapp.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.util.LogUtil;

public class WebActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private String url;
    private TextView mMySystemBarTextViewMessage;
    private ImageView mMySystemBarImageViewShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initWebData();
        initView();
        initWebView();
    }

    private void initWebData() {
        Intent intent = getIntent();
        this.url = intent.getStringExtra("url");
        if (url == null || url.equals("")){
            url = "https://www.chtholly.ac.cn/";
        }
        LogUtil.d("日志","加载:" + url);
    }

    private void initWebView() {

        WebView mWebView = (WebView) findViewById(R.id.webActivity_webView_showWeb);
        WebSettings mWebSettings = mWebView.getSettings();
        /* 设置支持Js,必须设置的,不然网页基本上不能看 */
        mWebSettings.setJavaScriptEnabled(true);
        /* 设置WebView是否可以由JavaScript自动打开窗口，默认为false，通常与JavaScript的window.open()配合使用。*/
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        /* 设置缓存模式,我这里使用的默认,不做多讲解 */
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        /* 设置为true表示支持使用js打开新的窗口 */
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        /* 大部分网页需要自己保存一些数据,这个时候就的设置下面这个属性 */
        mWebSettings.setDomStorageEnabled(true);
        /* 设置为使用webview推荐的窗口 */
        mWebSettings.setUseWideViewPort(true);
        /* HTML5的地理位置服务,设置为true,启用地理定位 */
        mWebSettings.setGeolocationEnabled(true);
        /* 设置显示水平滚动条,就是网页右边的滚动条*/
        mWebView.setHorizontalScrollBarEnabled(false);
        /* 指定垂直滚动条是否有叠加样式 */
        mWebView.setVerticalScrollbarOverlay(true);
        /* 设置滚动条的样式 */
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        //设置可以支持缩放
        mWebSettings.setSupportZoom(true);
        //设置出现缩放工具
        mWebSettings.setBuiltInZoomControls(false);
        //扩大比例的缩放
        mWebSettings.setUseWideViewPort(true);
        //自适应屏幕
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebSettings.setLoadWithOverviewMode(true);
        /* 这个不用说了,重写WebChromeClient监听网页加载的进度,从而实现进度条 */
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

        });
        /* 同上,重写WebViewClient可以监听网页的跳转和资源加载等等... */
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("scheme:") || url.startsWith("scheme:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,  String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }


        });
        mWebView.loadUrl(url);
    }

    //初始化组件
    private void initView() {
        mProgressBar = (ProgressBar) findViewById(R.id.web_progressBar_showProgress);
        mMySystemBarTextViewMessage = (TextView) findViewById(R.id.mySystemBar_textView_message);
        mMySystemBarImageViewShare = (ImageView) findViewById(R.id.mySystemBar_imageView_share);
        mMySystemBarTextViewMessage.setVisibility(View.GONE);
        mMySystemBarImageViewShare.setVisibility(View.VISIBLE);
        mMySystemBarImageViewShare.setOnClickListener((v)->{
           shareText(WebActivity.this,"您的好友向你分享了干货,快来看看吧:" + url);
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
    }

    public static void shareText(Context context, String text){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,text);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "发送"));
    }
}
