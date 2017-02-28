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


import com.example.xuyiwei.myapplication.HtmlContent;

import com.unionpay.cloudpos.AlgorithmConstants;
import com.unionpay.cloudpos.DeviceException;
import com.unionpay.cloudpos.OperationListener;
import com.unionpay.cloudpos.OperationResult;
import com.unionpay.cloudpos.POSTerminal;
import com.unionpay.cloudpos.pinpad.KeyInfo;
import com.unionpay.cloudpos.pinpad.PINPadDevice;
import com.unionpay.cloudpos.pinpad.PINPadOperationResult;
import com.unionpay.cloudpos.printer.Format;
import com.unionpay.cloudpos.printer.PrinterDevice;
import com.unionpay.cloudpos.printer.PrinterDeviceSpec;
import com.example.xuyiwei.myapplication.util.CommonUtil;
import com.example.xuyiwei.myapplication.util.LogCat;

/**
 * Created by Beckham on 2016/10/17.
 */
public class WebHost extends Activity{
    public Context context;
    private String str = "";
    private TextView txt;
    //密码键盘测试
    private Button btnPinPad;
    private PINPadDevice pinpadDevice;
    private WebView webview1;

    public WebHost(Context context,WebView webview){
        this.context = context;
        webview1 = webview;
    }

    @JavascriptInterface
    public void acllJs(){
        //Toast.makeText(context, "点击了登录按钮！", Toast.LENGTH_SHORT).show();
        //initViews();
        initData();
    }
    private void initViews() {
        //键盘
        //btnPinPad = (Button) findViewById(R.id.btnPinPad);
    }

    private Handler handler = new Handler();
    private Runnable myRunnable = new Runnable() {
        public void run() {
            //txt.setText(str);
            Toast.makeText(context, "密码输入成功！", Toast.LENGTH_SHORT).show();

            webview1.loadUrl("javascript:javacalljs()");
        }
    };

    private void initData() {
        //键盘
        //设备对象通过POSTerminal的对应方法获得，其中，"cloudpos.device.pinpad"是标识PIN输入设备的字符串，由具体的实现定义。
        //设备管理器POSTerminal 是获得每个设备的入口。 获取该对象不能使用new方法，可以通过getInstance方法获得。

        pinpadDevice = (PINPadDevice) POSTerminal.getInstance(context).getDevice("cloudpos.device.pinpad");
        //btnPinPad.setOnClickListener(pinpadClickListener);
        try {
                //str = "正在打开密码键盘，请稍后...\n";
                //handler.post(myRunnable);
                pinpadDevice.open();
                //str += "密码键盘打开成功，请输入密码...\n";
                //handler.post(myRunnable);
                try {
                    KeyInfo keyInfo = new KeyInfo(PINPadDevice.KEY_TYPE_MK_SK, 0, 0, AlgorithmConstants.ALG_3DES);
                    String pan = "1234567890123456";
                    pinpadDevice.listenForPinBlock(keyInfo, pan, true, new OperationListener() {

                        @Override
                        public void handleResult(OperationResult result) {
                            // TODO Auto-generated method stub
                            if (result.getResultCode() == result.SUCCESS) {
                                PINPadOperationResult operationResult = (PINPadOperationResult) result;
                                //String pinblock = CommonUtil.bytes2HexStr(operationResult.getEncryptedPINBlock());
                                //str += "pinblock:" + pinblock + "\n";
                                //handler.post(myRunnable);
                            }
                            try {
                                pinpadDevice.close();
                                //str += "密码键盘关闭成功...\n";
                                handler.post(myRunnable);
                            } catch (DeviceException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                //str += "密码键盘关闭失败...\n";
                                handler.post(myRunnable);
                            }
                        }
                    }, 60 * 1000);
                } catch (DeviceException de) {
                    de.printStackTrace();
                    //str += "获取pinblock失败...\n";
                    handler.post(myRunnable);
                }
            } catch (DeviceException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                //str = "密码键盘打开失败...\n";
                handler.post(myRunnable);
            }
    }



//    OnClickListener pinpadClickListener = new OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            try {
//                str = "正在打开密码键盘，请稍后...\n";
//                handler.post(myRunnable);
//                pinpadDevice.open();
//                str += "密码键盘打开成功，请输入密码...\n";
//                handler.post(myRunnable);
//                try {
//                    KeyInfo keyInfo = new KeyInfo(PINPadDevice.KEY_TYPE_MK_SK, 0, 0, AlgorithmConstants.ALG_3DES);
//                    String pan = "1234567890123456";
//                    pinpadDevice.listenForPinBlock(keyInfo, pan, true, new OperationListener() {
//
//                        @Override
//                        public void handleResult(OperationResult result) {
//                            // TODO Auto-generated method stub
//                            if (result.getResultCode() == result.SUCCESS) {
//                                PINPadOperationResult operationResult = (PINPadOperationResult) result;
//                                String pinblock = CommonUtil.bytes2HexStr(operationResult.getEncryptedPINBlock());
//                                str += "pinblock:" + pinblock + "\n";
//                                handler.post(myRunnable);
//                            } else if (result.getResultCode() == result.ERR_TIMEOUT) {
//                                str += "获取pinblock超时...\n";
//                                handler.post(myRunnable);
//                            } else {
//                                str += "获取pinblock失败...\n";
//                                handler.post(myRunnable);
//                            }
//                            try {
//                                pinpadDevice.close();
//                                str += "密码键盘关闭成功...\n";
//                                handler.post(myRunnable);
//                            } catch (DeviceException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                                str += "密码键盘关闭失败...\n";
//                                handler.post(myRunnable);
//                            }
//                        }
//                    }, 60 * 1000);
//                } catch (DeviceException de) {
//                    de.printStackTrace();
//                    str += "获取pinblock失败...\n";
//                    handler.post(myRunnable);
//                }
//            } catch (DeviceException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//                str = "密码键盘打开失败...\n";
//                handler.post(myRunnable);
//            }
//
//        }
//
//    };

   /* OnClickListener pinpadClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                pinpadDevice.open();
                pinpadDevice.close();
            } catch (DeviceException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();

            }

        }

    };*/
}
