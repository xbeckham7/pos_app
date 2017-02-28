package com.example.xuyiwei.myapplication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.example.xuyiwei.myapplication.HtmlContent;

import com.unionpay.cloudpos.AlgorithmConstants;
import com.unionpay.cloudpos.DeviceException;
import com.unionpay.cloudpos.OperationListener;
import com.unionpay.cloudpos.OperationResult;
import com.unionpay.cloudpos.POSTerminal;
import com.unionpay.cloudpos.TerminalSpec;
import com.unionpay.cloudpos.emv.EMVCardReaderResult;
import com.unionpay.cloudpos.emv.EMVConstants;
import com.unionpay.cloudpos.emv.EMVDevice;
import com.unionpay.cloudpos.emv.EMVTransData;
import com.unionpay.cloudpos.emv.EMVTransListener;
import com.unionpay.cloudpos.emv.OnlineResult;
import com.unionpay.cloudpos.emv.PINResult;
import com.unionpay.cloudpos.hsm.HSMDevice;
import com.unionpay.cloudpos.led.LEDDevice;
import com.unionpay.cloudpos.msr.MSRDevice;
import com.unionpay.cloudpos.msr.MSROperationResult;
import com.unionpay.cloudpos.msr.MSRTrackData;
import com.unionpay.cloudpos.pinpad.KeyInfo;
import com.unionpay.cloudpos.pinpad.PINPadDevice;
import com.unionpay.cloudpos.pinpad.PINPadOperationResult;
import com.unionpay.cloudpos.printer.Format;
import com.unionpay.cloudpos.printer.PrinterDevice;
import com.unionpay.cloudpos.printer.PrinterDeviceSpec;
import com.example.xuyiwei.myapplication.util.CommonUtil;
import com.example.xuyiwei.myapplication.util.LogCat;


import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private WebView webview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webview = (WebView) findViewById(R.id.webview);
        //设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);

        //js调用java方法
        context = this;
        webview.addJavascriptInterface(new WebHost(context,webview), "js");
        webview.addJavascriptInterface(new WebHostcard(context,webview), "jss");
        webview.addJavascriptInterface(new WebHostshuacard(context,webview), "jsss");

        class JsObject {
            @JavascriptInterface
            public String toString() { return "injectedObject"; }
        }
        webview.addJavascriptInterface(new JsObject(), "injectedObject");
        
        //设置WebView属性，能够执行localstorage
        webview.getSettings().setDomStorageEnabled(true);
        //加载需要显示的网页
        webview.loadUrl("file:///android_asset/UI/index.html" );
        //设置Web视图
        webview.setWebViewClient(new HelloWebViewClient ());
    }

    @Override
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return false;
    }

    //Web视图
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
