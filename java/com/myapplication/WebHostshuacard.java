package com.example.xuyiwei.myapplication;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;
import android.webkit.JavascriptInterface;
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


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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

/**
 * Created by Beckham on 2016/10/18.
 */
public class WebHostshuacard extends Activity{

    public Context context;
    private WebView webview1;
    private MSRDevice msrDevice;


    public WebHostshuacard(Context context,WebView webview){
        this.context = context;
        webview1 = webview;
    }

    @JavascriptInterface
    public void scrdJs(){
        //Toast.makeText(context, "点击了登录按钮！", Toast.LENGTH_SHORT).show();
        //initViews();
        initData();
    }
    public void unscrdJs(){
        //Toast.makeText(context, "点击了登录按钮！", Toast.LENGTH_SHORT).show();
        //initViews();
        uninitData();
    }

    private Handler handler = new Handler();
    private Runnable myRunnable = new Runnable() {
        public void run() {
            //txt.setText(str);
            Toast.makeText(context, "刷卡成功！", Toast.LENGTH_SHORT).show();
            webview1.loadUrl("javascript:javascardjsss()");
        }
    };
    private Runnable myRunnable1 = new Runnable() {
        public void run() {
            //txt.setText(str);
            Toast.makeText(context, "刷卡失败，请重试！", Toast.LENGTH_SHORT).show();
        }
    };
    private Runnable myRunnable2 = new Runnable() {
        public void run() {
            //txt.setText(str);
            webview1.loadUrl("javascript:javaunscardjsss()");
        }
    };

    private void initData() {
        msrDevice = (MSRDevice) POSTerminal.getInstance(context).getDevice("cloudpos.device.msr");
        try {
            //str = "正在打开磁条卡阅读器，请稍后...\n";
            //handler.post(myRunnable);
            msrDevice.open();
            //str += "磁条卡阅读器已成功打开，请刷卡...\n";
            //handler.post(myRunnable);
            try {
                msrDevice.listenForSwipe(new OperationListener() {//等待刷卡

                    @Override
                    public void handleResult(OperationResult result) {
                        // TODO Auto-generated method stub
                        try {
                            msrDevice.close();
                            //str += "磁条卡阅读器已成功关闭...\n";
                            handler.post(myRunnable);
                        } catch (DeviceException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            //str += "磁条卡阅读器关闭失败...\n";
                            //handler.post(myRunnable);
                        }
                    }
                }, 100000);//time

            } catch (DeviceException de) {
                //str += "读取磁道信息失败...\n";
                handler.post(myRunnable1);
            }
        } catch (DeviceException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
           // str += "磁条卡阅读器打开失败...\n";
            handler.post(myRunnable1);
        }
    }

    private void uninitData() {
        msrDevice = (MSRDevice) POSTerminal.getInstance(context).getDevice("cloudpos.device.msr");
        try {
            //str = "正在打开磁条卡阅读器，请稍后...\n";
            //handler.post(myRunnable);
            msrDevice.close();
            handler.post(myRunnable2);
            //str += "磁条卡阅读器已成功打开，请刷卡...\n";
            //handler.post(myRunnable);

        } catch (DeviceException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            // str += "磁条卡阅读器打开失败...\n";
            //handler.post(myRunnable1);
        }
    }
/*    OnClickListener msrClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                str = "正在打开磁条卡阅读器，请稍后...\n";
                handler.post(myRunnable);
                msrDevice.open();
                str += "磁条卡阅读器已成功打开，请刷卡...\n";
                handler.post(myRunnable);
                try {
                    msrDevice.listenForSwipe(new OperationListener() {

                        @Override
                        public void handleResult(OperationResult result) {
                            // TODO Auto-generated method stub
                            if (result.getResultCode() == result.SUCCESS) {
                                MSROperationResult msrOperationResult = (MSROperationResult) result;
                                MSRTrackData tarckData = msrOperationResult.getMSRTrackData();
                                if (tarckData.getTrackError(0) == MSRTrackData.NO_ERROR) {
                                    if (tarckData.getTrackData(0) != null) {
                                        String track1Data = new String(tarckData.getTrackData(0));
                                        str += "第一磁道信息:" + track1Data + "\n";
                                        handler.post(myRunnable);
                                        Log.i("", "第一磁道信息:" + track1Data);
                                    }
                                } else {
                                    if (tarckData.getTrackData(0) == null) {
                                        str += "第一磁道信息:\n";
                                        handler.post(myRunnable);
                                        Log.i("", "第一磁道信息:");
                                    }
                                }
                                if (tarckData.getTrackError(1) == MSRTrackData.NO_ERROR) {
                                    if (tarckData.getTrackData(1) != null) {
                                        String track2Data = new String(tarckData.getTrackData(1));
                                        str += "第二磁道信息:" + track2Data + "\n";
                                        handler.post(myRunnable);
                                        Log.i("", "第二磁道信息:" + track2Data);
                                    }
                                } else {
                                    if (tarckData.getTrackData(1) == null) {
                                        str += "第二磁道信息:\n";
                                        handler.post(myRunnable);
                                        Log.i("", "第二磁道信息:");
                                    }
                                }
                                if (tarckData.getTrackError(2) == MSRTrackData.NO_ERROR) {
                                    if (tarckData.getTrackData(2) != null) {
                                        String track3Data = new String(tarckData.getTrackData(2));
                                        str += "第三磁道信息:" + track3Data + "\n";
                                        handler.post(myRunnable);
                                        Log.i("", "第三磁道信息:" + track3Data);
                                    }
                                } else {
                                    if (tarckData.getTrackData(2) == null) {
                                        str += "第三磁道信息:\n";
                                        handler.post(myRunnable);
                                        Log.i("", "第三磁道信息:");
                                    }
                                }
                            } else if (result.getResultCode() == result.ERR_TIMEOUT) {
                                str += "读取磁道信息超时..\n";
                                handler.post(myRunnable);
                            } else {
                                str += "读取磁道信息失败...\n";
                                handler.post(myRunnable);
                            }

                            try {
                                msrDevice.close();
                                str += "磁条卡阅读器已成功关闭...\n";
                                handler.post(myRunnable);
                            } catch (DeviceException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                str += "磁条卡阅读器关闭失败...\n";
                                handler.post(myRunnable);
                            }
                        }
                    }, 10000);

                } catch (DeviceException de) {
                    str += "读取磁道信息失败...\n";
                    handler.post(myRunnable);
                }
            } catch (DeviceException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                str += "磁条卡阅读器打开失败...\n";
                handler.post(myRunnable);
            }
        }

    };*/
    }