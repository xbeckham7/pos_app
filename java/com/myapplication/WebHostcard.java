package com.example.xuyiwei.myapplication;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
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
import com.unionpay.cloudpos.emv.EMVTermConfig;
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
import com.example.xuyiwei.myapplication.util.LogCat;
import com.example.xuyiwei.myapplication.util.CommonUtil;
import com.example.xuyiwei.myapplication.util.CommonUtil;
import com.example.xuyiwei.myapplication.util.LogCat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Beckham on 2016/10/18.
 */
public class WebHostcard extends Activity{
    public static final String URL_START_UP = "https://202.101.25.188:8188/CloudPosPayment/StartupServlet";
    private Button btnEmvCardNo;
    private EMVDevice emvDevice;
    public Context context;
    private WebView webview1;
    private String str = "";


    public WebHostcard(Context context,WebView webview){
        this.context = context;
        webview1 = webview;
    }

    @JavascriptInterface
    public void acrdJs(){
        //Toast.makeText(context, "点击了登录按钮！", Toast.LENGTH_SHORT).show();
        //initViews();
        initData();
    }
    public void unacrdJs(){
        //Toast.makeText(context, "点击了登录按钮！", Toast.LENGTH_SHORT).show();
        //initViews();
        uninitData();
    }

    private Handler handler = new Handler();
    private Runnable myRunnable = new Runnable() {
        public void run() {
            //txt.setText(str);
            //Toast.makeText(context, "刷卡成功！", Toast.LENGTH_SHORT).show();
            webview1.loadUrl("javascript:javacardjss()");

        }
    };
    private Runnable myRunnable1 = new Runnable() {
        public void run() {
            //txt.setText(str);
            Toast.makeText(context, "刷卡超时！", Toast.LENGTH_SHORT).show();
            //webview1.loadUrl("javascript:javacardjss()");
        }
    };
    private Runnable myRunnable2 = new Runnable() {
        public void run() {
            //txt.setText(str);
            webview1.loadUrl("javascript:javauncardjss()");
        }
    };
    private Runnable myRunnable3 = new Runnable() {
        public void run() {
            //txt.setText(str);
            Toast.makeText(context, "刷卡成功！", Toast.LENGTH_SHORT).show();
            webview1.loadUrl("javascript:javacardidjss('"+str+"')");
            str = "";
        }
    };


    private void initData() {
        emvDevice = (EMVDevice) POSTerminal.getInstance(context).getDevice("cloudpos.device.emv");
        try {
            String strAidsJson= null;
            String strCapksJson= null;
            String strTerminalParamJson = null;
            try {
                String t = URL_START_UP();
                JSONObject jsonObject;
                jsonObject = new JSONObject(t);
                strAidsJson = jsonObject.getString("strAids");
                strCapksJson = jsonObject.getString("strCapks");
                String strTerminalArr = jsonObject.getString("strTerminalParam");
                JSONArray jsonArray = new JSONArray(strTerminalArr);
                if (jsonArray.length() != 0) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                    strTerminalParamJson = jsonObject2.toString();

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            emvDevice.open();
            prePrepareData(strAidsJson, strCapksJson, strTerminalParamJson);
            emvDevice.readCard(60000, true, true, readCardListener);

        } catch (DeviceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void uninitData() {
        emvDevice = (EMVDevice) POSTerminal.getInstance(context).getDevice("cloudpos.device.emv");
        try {
            emvDevice.stopReadCard();
            emvDevice.close();
            //str += "EMV设备关闭成功...\n";
            handler.post(myRunnable2);
        } catch (DeviceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //str += "EMV设备打开失败...\n";
            handler.post(myRunnable2);
        }
    }

    OperationListener readCardListener = new OperationListener() {

        @Override
        public void handleResult(OperationResult result) {
            // TODO Auto-generated method stub
            EMVCardReaderResult mCardReaderResult = (EMVCardReaderResult) result;
            if (mCardReaderResult.getResultCode() == OperationResult.SUCCESS) {
                final EMVTransData transData = new EMVTransData();
                if (mCardReaderResult.getChannelType() == EMVConstants.Channel_Type_IC) {
                    transData.setChannelType(EMVConstants.Channel_Type_IC);
                } else if (mCardReaderResult.getChannelType() == EMVConstants.Channel_Type_RF) {
                    transData.setChannelType(EMVConstants.Channel_Type_RF);
                }
                transData.setFlow(EMVTransData.TRANSDATA_FLOW_SIMPLE);//flow 流程类型，0x01：标准的授权过程；0x02：简易流程；0x03：qPBOC流程
                String strSysDate = CommonUtil.getSysDate();
                String strDate = strSysDate.substring(0, 8);
                String strTime = strSysDate.substring(8, 14);
                transData.setTransDate(strDate);
                transData.setTransType((byte) 0x00);
                transData.setTransTime(strTime);
                transData.setSupportEC(false); //不用电子现金
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        EMVTransListener mEMVTransListener = new EMVTransListener() {

                            @Override
                            public int onWaitAppSelect(List<String> appNameList, boolean isFirstSelect) {
                                // TODO Auto-generated method stub
                                return 0;
                            }

                            @Override
                            public void onTransResult(int code, String desc) {
                                // TODO Auto-generated method stub
                                if (code == EMVConstants.Process_Result_Approve) {
                                    //str += "交易成功...\n";
                                    //handler.post(myRunnable);
                                } else {
                                    //str += "交易拒绝...\n";
                                   // handler.post(myRunnable);
                                }
                                closeEmv();
                            }

                            @Override
                            public OnlineResult onOnlineProc() {
                                return null;
                            }

                            @Override
                            public int onConfirmEC() {
                                // TODO Auto-generated method stub
                                return 0;
                            }

                            @Override
                            public int onConfirmCardNo(String cardNO) {
                                str += cardNO ;
                                handler.post(myRunnable3);
                                return EMVConstants.App_Confirm_OK;
                            }

                            @Override
                            public int onCertVerfiy(String certType, String certValue) {
                                // TODO Auto-generated method stub
                                return 0;
                            }

                            @Override
                            public PINResult onCardHolderPwd(boolean arg0, int arg1) {
                                // TODO Auto-generated method stub
                                return null;
                            }

                        };

                        // TODO Auto-generated method stub
                        try {
                            emvDevice.process(transData, mEMVTransListener);
                        } catch (DeviceException e) {
                            // TODO Auto-generated catch block
                            closeEmv();
                            e.printStackTrace();
                        }

                    }
                });
                thread.start();
            } else {
                closeEmv();
            }
        }
    };
    /*OnClickListener emvCardNoClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            try {
                str = "正在打开EMV设备，请稍后...\n";
                handler.post(myRunnable);
                emvDevice.open();
                emvDevice.readCard(60000, true, true, readCardListener);
                str += "EMV设备已成功打开，请插卡或挥卡...\n";
                handler.post(myRunnable);
            } catch (DeviceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };*/
    private void closeEmv() {
        try {
            emvDevice.stopReadCard();
            emvDevice.close();
            //str += "EMV设备关闭成功...\n";
            handler.post(myRunnable);
        } catch (DeviceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //str += "EMV设备打开失败...\n";
            handler.post(myRunnable);
        }
    }

    public static String URL_START_UP() {
        String strResponse = "{mchntId:'"
                + "123456789000010"
                + "',"
                + "termId:'"
                + "12345610"
                + "',strAids:[{index:'1',val:'9f0607a0000000033010df0101009f08020140df1105d84000a800df1205d84004f800df130500100000009"
                + "f1b0400000000df150400000000df160199df170199df14039f3704df1801009f7b06000000100000df1906000000100000df2006000000100000df2106000000100000'}"
                + ",{index:'2',val:'9f0608a000000333010103df0101009f08020020df1105d840"
                + "00a800df1205d84004f800df130500100000009f1b0400000001df150400000000df160199df170199df14039f3704df1801019f7b06000000100000df1906000000100000df2006000000100000df2106000000100000'}"
                + ",{index:'3',val:'9f0607a0000000651010df0101009f08020200df1105fc6024a800df1205fc60acf800df130500100000009f1b0400000000df150400000000df160199df170199df14039f3704d"
                + "f1801009f7b06000000100000df1906000000100000df2006000000100000df2106000000100000'},{index:'4',val:'9f0607a0000000032010df0101009f08020140df1105d84000a800df1205d84004f800df130500100000009f1b0400000000df1504"
                + "00000000df160199df170199df14039f3704df1801009f7b06000000100000df1906000000100000df2006000000100000df2106000000100000'},{index:'5',val:'9f0607a0000000031010df0101009f08020140df1105d84000a800df1205d84004f80"
                + "0df130500100000009f1b0400000000df150400000000df160199df170199df14039f3704df1801009f7b06000000100000df1906000000100000df2006000000100000df2106000000100000'},{index:'6',val:'9f0608a000000333010106df0101009f"
                + "08020020df1105d84000a800df1205d84004f800df130500100000009f1b0400000001df150400000000df160199df170199df14039f3704df1801019f7b06000000"
                + "100000df1906000000100000df2006000000100000df2106000000100000'},{index:'7',val:'9f0608a000000333010101df0101009f08020020df1105d84000a"
                + "800df1205d84004f800df130500100000009f1b0400000001df150400000000df160199df170199df14039f3704df1801019f7b06000000100000df1906000000100000df2006000000100000df2106000000100000'},{index:'8',val:'9f0607a0000000"
                + "043060df0101009f08020002df1105fc5058a000df1205f85058f800df130504000000009f1b0400000000df150400000000df160199df170199df14039f3704df18"
                + "01019f7b06000000100000df1906000000100000df2006000000100000df2106000000100000'},{index:'9',val:'9f0608a000000333010102df0101009f08020020df1105d84000a800df1205d84004f800df130500100000009f1b0400000001df15040"
                + "0000000df160199df170199df14039f3704df1801019f7b06000000100000df1906000000100000df2006000000100000df2106000000100000'},{index:'10',val:'9f0607a0000000041010df0101009f08020002df1105fc5080a000df1205f85080f80"
                + "0df130504000000009f1b0400000000df150400000000df160199df170199df14039f3704df1801009f7b06000000100000df1906000000100000df2006000000100000df2106000000100000'}],strCapks:[{index:'1',val:'9f0605a0000000039f220"
                + "101df05083230313231323331df060101df070101df028180c696034213d7d8546984579d1d0f0ea519cff8deffc429354cf3a871a6f7183f1228da5c7470c055387"
                + "100cb935a712c4e2864df5d64ba93fe7e63e71f25b1e5f5298575ebe1c63aa617706917911dc2a75ac28b251c7ef40f2365912490b939bca2124a30a28f54402c34a"
                + "eca331ab67e1e79b285dd5771b5d9ff79ea630b75df040103df0314d34a6a776011c7e7ce3aec5f03ad2f8cfc5503cc'},{index:'2',val:'9f0605a0000000049f"
                + "220103df05083230313231323331df060101df070101df028180c2490747fe17eb0584c88d47b1602704150adc88c5b998bd59ce043edebf0ffee3093ac7956ad3b6"
                + "ad4554c6de19a178d6da295be15d5220645e3c8131666fa4be5b84fe131ea44b039307638b9e74a8c42564f892a64df1cb15712b736e3374f1bbb6819371602d8970"
                + "e97b900793c7c2a89a4a1649a59be680574dd0b60145df040103df03145addf21d09278661141179cbeff272ea384b13bb'},{index:'3',val:'9f0605a00000033"
                + "39f220109df05083230313231323331df060101df070101df0281b0eb374dfc5a96b71d2863875eda2eafb96b1b439d3ece0b1826a2672eeefa7990286776f8bd989"
                + "a15141a75c384dfc14fef9243aab32707659be9e4797a247c2f0b6d99372f384af62fe23bc54bcdc57a9acd1d5585c303f201ef4e8b806afb809db1a3db1cd112ac8"
                + "84f164a67b99c7d6e5a8a6df1d3cae6d7ed3d5be725b2de4ade23fa679bf4eb15a93d8a6e29c7ffa1a70de2e54f593d908a3bf9ebbd760bbfdc8db8b54497e6c5be0"
                + "e4a4dac29e5df040103df0314a075306eab0045baf72cdd33b3b678779de1f527'},{index:'4',val:'9f0605a0000003339f220104df05083230313431323331df"
                + "060101df070101df0281f8bc853e6b5365e89e7ee9317c94b02d0abb0dbd91c05a224a2554aa29ed9fcb9d86eb9ccbb322a57811f86188aac7351c72bd9ef196c5a0"
                + "1acef7a4eb0d2ad63d9e6ac2e7836547cb1595c68bcbafd0f6728760f3a7ca7b97301b7e0220184efc4f653008d93ce098c0d93b45201096d1adff4cf1f9fc02af75"
                + "9da27cd6dfd6d789b099f16f378b6100334e63f3d35f3251a5ec78693731f5233519cdb380f5ab8c0f02728e91d469abd0eae0d93b1cc66ce127b29c7d77441a49d0"
                + "9fca5d6d9762fc74c31bb506c8bae3c79ad6c2578775b95956b5370d1d0519e37906b384736233251e8f09ad79dfbe2c6abfadac8e4d8624318c27daf1df040103df"
                + "0314f527081cf371dd7e1fd4fa414a665036e0f5e6e5'},{index:'5',val:'9f0605a0000000659f220109df05083230313431323331df060101df070101df02818"
                + "0b72a8fef5b27f2b550398fdcc256f714bad497ff56094b7408328cb626aa6f0e6a9df8388eb9887bc930170bcc1213e90fc070d52c8dcd0ff9e10fad36801fe93fc"
                + "998a721705091f18bc7c98241cadc15a2b9da7fb963142c0ab640d5d0135e77ebae95af1b4fefadcf9c012366bdda0455c1564a68810d7127676d493890bddf04010"
                + "3df03144410c6d51c2f83adfd92528fa6e38a32df048d0a'},{index:'6',val:'9f0605a0000000659f220110df05083230313231323331df060101df070101df02"
                + "819099b63464ee0b4957e4fd23bf923d12b61469b8fff8814346b2ed6a780f8988ea9cf0433bc1e655f05efa66d0c98098f25b659d7a25b8478a36e489760d071f54"
                + "cdf7416948ed733d816349da2aadda227ee45936203cbf628cd033aaba5e5a6e4ae37fbacb4611b4113ed427529c636f6c3304f8abdd6d9ad660516ae87f7f2ddf1d"
                + "2fa44c164727e56bbc9ba23c0285df040103df0314c75e5210cbe6e8f0594a0f1911b07418cadb5bab'},{index:'7',val:'9f0605a0000003339f22010adf05083"
                + "230313431323331df060101df070101df028180b2ab1b6e9ac55a75adfd5bbc34490e53c4c3381f34e60e7fac21cc2b26dd34462b64a6fae2495ed1dd383b8138bea"
                + "100ff9b7a111817e7b9869a9742b19e5c9dac56f8b8827f11b05a08eccf9e8d5e85b0f7cfa644eff3e9b796688f38e006deb21e101c01028903a06023ac5aab8635f"
                + "8e307a53ac742bdce6a283f585f48efdf040103df0314c88be6b2417c4f941c9371ea35a377158767e4e3'},{index:'8',val:'9f0605a0000000039f220108df05"
                + "083230313431323331df060101df070101df0281b0d9fd6ed75d51d0e30664bd157023eaa1ffa871e4da65672b863d255e81e137a51de4f72bcc9e44ace12127f87e"
                + "263d3af9dd9cf35ca4a7b01e907000ba85d24954c2fca3074825ddd4c0c8f186cb020f683e02f2dead3969133f06f7845166aceb57ca0fc2603445469811d293bfef"
                + "bafab57631b3dd91e796bf850a25012f1ae38f05aa5c4d6d03b1dc2e568612785938bbc9b3cd3a910c1da55a5a9218ace0f7a21287752682f15832a678d6e1ed0bdf"
                + "040103df031420d213126955de205adc2fd2822bd22de21cf9a8'},{index:'9',val:'9f0605a0000003339f220102df05083230313431323331df060101df07010"
                + "1df028190a3767abd1b6aa69d7f3fbf28c092de9ed1e658ba5f0909af7a1ccd907373b7210fdeb16287ba8e78e1529f443976fd27f991ec67d95e5f4e96b127cab23"
                + "96a94d6e45cda44ca4c4867570d6b07542f8d4bf9ff97975db9891515e66f525d2b3cbeb6d662bfb6c3f338e93b02142bfc44173a3764c56aadd202075b26dc2f9f7"
                + "d7ae74bd7d00fd05ee430032663d27a57df040103df031403bb335a8549a03b87ab089d006f60852e4b8060'},{index:'10',val:'9f0605a0000000659f220112d"
                + "f05083230313431323331df060101df070101df0281b0adf05cd4c5b490b087c3467b0f3043750438848461288bfefd6198dd576dc3ad7a7cfa07dba128c247a8eab"
                + "30dc3a30b02fcd7f1c8167965463626feff8ab1aa61a4b9aef09ee12b009842a1aba01adb4a2b170668781ec92b60f605fd12b2b2a6f1fe734be510f60dc5d189e40"
                + "1451b62b4e06851ec20ebff4522aacc2e9cdc89bc5d8cde5d633cfd77220ff6bbd4a9b441473cc3c6fefc8d13e57c3de97e1269fa19f655215b23563ed1d1860d868"
                + "1df040103df0314874b379b7f607dc1caf87a19e400b6a9e25163e8'},{index:'11',val:'9f0605a0000000039f220107df05083230313231323331df060101df0"
                + "70101df028190a89f25a56fa6da258c8ca8b40427d927b4a1eb4d7ea326bbb12f97ded70ae5e4480fc9c5e8a972177110a1cc318d06d2f8f5c4844ac5fa79a4dc470"
                + "bb11ed635699c17081b90f1b984f12e92c1c529276d8af8ec7f28492097d8cd5becea16fe4088f6cfab4a1b42328a1b996f9278b0b7e3311ca5ef856c2f888474b83"
                + "612a82e4e00d0cd4069a6783140433d50725fdf040103df0314b4bc56cc4e88324932cbc643d6898f6fe593b172'},{index:'12',val:'9f0605a0000000049f220"
                + "163df05083230313231323331df060101df070101df028190cf71f040528c9af2bf4341c639b7f31be1abff269633542cf22c03ab51570402c9cafc14437ae42f4e7"
                + "cad00c9811b536dff3792facb86a0c7fae5fa50ae6c42546c534ea3a11fbd2267f1cf9ac68874dc221ecb3f6334f9c0bb832c075c2961ca9bbb683bec2477d12344e"
                + "1b7d6dbe07b286fcf41a0f7f1f6f248a8c86398b7fa1c115111051dd01df3ed08985705fddf040103df03146e5ff80cd0a1cc2e3249b9c198d43427ce874013'},{i"
                + "ndex:'13',val:'9f0605a0000000049f220104df05083230313231323331df060101df070101df028190a6da428387a502d7ddfb7a74d3f412be762627197b25435"
                + "b7a81716a700157ddd06f7cc99d6ca28c2470527e2c03616b9c59217357c2674f583b3ba5c7dcf2838692d023e3562420b4615c439ca97c44dc9a249cfce7b3bfb22"
                + "f68228c3af13329aa4a613cf8dd853502373d62e49ab256d2bc17120e54aedced6d96a4287acc5c04677d4a5a320db8bee2f775e5fec5df040103df0314381a035da"
                + "58b482ee2af75f4c3f2ca469ba4aa6c'},{index:'14',val:'9f0605a0000000039f220109df05083230313631323331df060101df070101df0281f89d912248de0"
                + "a4e39c1a7dde3f6d2588992c1a4095afbd1824d1ba74847f2bc4926d2efd904b4b54954cd189a54c5d1179654f8f9b0d2ab5f0357eb642feda95d3912c6576945fab"
                + "897e7062caa44a4aa06b8fe6e3dba18af6ae3738e30429ee9be03427c9d64f695fa8cab4bfe376853ea34ad1d76bfcad15908c077ffe6dc5521ecef5d278a96e26f5"
                + "7359ffaeda19434b937f1ad999dc5c41eb11935b44c18100e857f431a4a5a6bb65114f174c2d7b59fdf237d6bb1dd0916e644d709ded56481477c75d95cdd6825461"
                + "5f7740ec07f330ac5d67bcd75bf23d28a140826c026dbde971a37cd3ef9b8df644ac385010501efc6509d7a41df040103df03141ff80a40173f52d7d27e0f26a146a"
                + "1c8ccb29046'},{index:'15',val:'9f0605a0000000039f220163df05083230313231323331df060101df070101df028190cf71f040528c9af2bf4341c639b7f31"
                + "be1abff269633542cf22c03ab51570402c9cafc14437ae42f4e7cad00c9811b536dff3792facb86a0c7fae5fa50ae6c42546c534ea3a11fbd2267f1cf9ac68874dc2"
                + "21ecb3f6334f9c0bb832c075c2961ca9bbb683bec2477d12344e1b7d6dbe07b286fcf41a0f7f1f6f248a8c86398b7fa1c115111051dd01df3ed08985705fddf04010"
                + "3df0314b2f6af1ddc393be17525d0ea7bf568bed5b71167'},{index:'16',val:'9f0605a0000000049f220106df05083230313631323331df060101df070101df0"
                + "281f8cb26fc830b43785b2bce37c81ed334622f9622f4c89aae641046b2353433883f307fb7c974162da72f7a4ec75d9d657336865b8d3023d3d645667625c9a07a6"
                + "b7a137cf0c64198ae38fc238006fb2603f41f4f3bb9da1347270f2f5d8c606e420958c5f7d50a71de30142f70de468889b5e3a08695b938a50fc980393a9cbce44ad"
                + "2d64f630bb33ad3f5f5fd495d31f37818c1d94071342e07f1bec2194f6035ba5ded3936500eb82dfda6e8afb655b1ef3d0d7ebf86b66dd9f29f6b1d324fe8b26ce38"
                + "ab2013dd13f611e7a594d675c4432350ea244cc34f3873cba06592987a1d7e852adc22ef5a2ee28132031e48f74037e3b34ab747fdf040103df0314f910a1504d5ff"
                + "b793d94f3b500765e1abcad72d9'},{index:'17',val:'9f0605a0000000049f220105df05083230313431323331df060101df070101df0281b0b8048abc30c90d9"
                + "76336543e3fd7091c8fe4800df820ed55e7e94813ed00555b573feca3d84af6131a651d66cff4284fb13b635edd0ee40176d8bf04b7fd1c7bacf9ac7327dfaa8aa72"
                + "d10db3b8e70b2ddd811cb4196525ea386acc33c0d9d4575916469c4e4f53e8e1c912cc618cb22dde7c3568e90022e6bba770202e4522a2dd623d180e215bd1d1507f"
                + "e3dc90ca310d27b3efccd8f83de3052cad1e48938c68d095aac91b5f37e28bb49ec7ed597df040103df0314ebfa0d5d06d8ce702da3eae890701d45e274c845'},{i"
                + "ndex:'18',val:'9f0605a0000003339f220101df05083230313431323331df060101df070101df028180bbe9066d2517511d239c7bfa77884144ae20c7372f51514"
                + "7e8ce6537c54c0a6a4d45f8ca4d290870cda59f1344ef71d17d3f35d92f3f06778d0d511ec2a7dc4ffeadf4fb1253ce37a7b2b5a3741227bef72524da7a2b7b1cb42"
                + "6bee27bc513b0cb11ab99bc1bc61df5ac6cc4d831d0848788cd74f6d543ad37c5a2b4c5d5a93bdf040103df0314e881e390675d44c2dd81234dce29c3f5ab2297a0'"
                + "},{index:'19',val:'9f0605a0000003339f220108df05083230323031323331df060101df070101df028190b61645edfd5498fb246444037a0fa18c0f101ebd8ef"
                + "a54573ce6e6a7fbf63ed21d66340852b0211cf5eef6a1cd989f66af21a8eb19dbd8dbc3706d135363a0d683d046304f5a836bc1bc632821afe7a2f75da3c50ac74c5"
                + "45a754562204137169663cfcc0b06e67e2109eba41bc67ff20cc8ac80d7b6ee1a95465b3b2657533ea56d92d539e5064360ea4850fed2d1bfdf040103df0314ee23b"
                + "616c95c02652ad18860e48787c079e8e85a'},{index:'20',val:'9f0605a0000003339f220103df05083230313431323331df060101df070101df0281b0b0627de"
                + "e87864f9c18c13b9a1f025448bf13c58380c91f4ceba9f9bcb214ff8414e9b59d6aba10f941c7331768f47b2127907d857fa39aaf8ce02045dd01619d689ee731c55"
                + "1159be7eb2d51a372ff56b556e5cb2fde36e23073a44ca215d6c26ca68847b388e39520e0026e62294b557d6470440ca0aefc9438c923aec9b2098d6d3a1af5e8b1d"
                + "e36f4b53040109d89b77cafaf70c26c601abdf59eec0fdc8a99089140cd2e817e335175b03b7aa33ddf040103df031487f0cd7c0e86f38f89a66f8c47071a8b88586"
                + "f26'},{index:'21',val:'9f0605a0000003339f22010bdf05083230313631323331df060101df070101df0281f8cf9fdf46b356378e9af311b0f981b21a1f22f25"
                + "0fb11f55c958709e3c7241918293483289eae688a094c02c344e2999f315a72841f489e24b1ba0056cfab3b479d0e826452375dcdbb67e97ec2aa66f4601d774feae"
                + "f775accc621bfeb65fb0053fc5f392aa5e1d4c41a4de9ffdfdf1327c4bb874f1f63a599ee3902fe95e729fd78d4234dc7e6cf1ababaa3f6db29b7f05d1d901d2e76a"
                + "606a8cbffffecbd918fa2d278bdb43b0434f5d45134be1c2781d157d501ff43e5f1c470967cd57ce53b64d82974c8275937c5d8502a1252a8a5d6088a259b694f986"
                + "48d9af2cb0efd9d943c69f896d49fa39702162acb5af29b90bade005bc157df040103df0314bd331f9996a490b33c13441066a09ad3feb5f66c'},{index:'22',va"
                + "l:'9f0605a0000000659f220114df05083230313631323331df060101df070101df0281f8aeed55b9ee00e1eceb045f61d2da9a66ab637b43fb5cdbdb22a2fbb25be"
                + "061e937e38244ee5132f530144a3f268907d8fd648863f5a96fed7e42089e93457adc0e1bc89c58a0db72675fbc47fee9ff33c16ade6d341936b06b6a6f5ef6f66a4"
                + "edd981df75da8399c3053f430eca342437c23af423a211ac9f58eaf09b0f837de9d86c7109db1646561aa5af0289af5514ac64bc2d9d36a179bb8a7971e2bfa03a9e"
                + "4b847fd3d63524d43a0e8003547b94a8a75e519df3177d0a60bc0b4bab1ea59a2cbb4d2d62354e926e9c7d3be4181e81ba60f8285a896d17da8c3242481b6c405769"
                + "a39d547c74ed9ff95a70a796046b5eff36682dc29df040103df0314c0d15f6cd957e491db56dcdd1ca87a03ebe06b7b'}],strTerminalParam:[{cnCode:'0156',"
                + "tid:'12345602',ifd:'12312312',curCode:'0156',cap:'2040C8',termType:'34',transCurExp:'20',adTermCap:'6000D01001',merName:'PBOC2.0TEST"
                + "',ecTermTransLimit:'006000',transProperty:'36'}],strTransCtrlParam:'',icons:["
                + "{code:'001',parentId:'000',positionId:'2'},{code:'002',parentId:'000',positionId:'3'},{code:'003',parentId:'000"
                + "',positionId:'4'},{code:'004',parentId:'000',positionId:'5'},{code:'006',parentId:'000',positionId:'11'},{code:'007',parentId:'000',"
                + "positionId:'8'},{code:'008',parentId:'000',positionId:'7'},{code:'010',parentId:'008',positionId:'1'},{code:'011',parentId:'008',pos"
                + "itionId:'2'},{code:'012',parentId:'008',positionId:'3'},{code:'013',parentId:'000',positionId:'10'},{code:'014',parentId:'013',posit"
                + "ionId:'2'},{code:'015',parentId:'006',positionId:'1'},{code:'016',parentId:'006',positionId:'2'},{code:'017',parentId:'006',position"
                + "Id:'3'},{code:'018',parentId:'013',positionId:'1'},{code:'019',parentId:'006',positionId:'4'},{code:'022',parentId:'015',positionId:"
                + "'1'},{code:'023',parentId:'015',positionId:'2'},{code:'027',parentId:'000',positionId:'9'},{code:'029',parentId:'000',positionId:'6'"
                + "},{code:'035',parentId:'004',positionId:'1'},{code:'036',parentId:'004',positionId:'2'},{code:'037',parentId:'004',positionId:'3'},{"
                + "code:'038',parentId:'004',positionId:'4'},{code:'039',parentId:'004',positionId:'5'},{code:'040',parentId:'007',positionId:'1'},{cod"
                + "e:'041',parentId:'007',positionId:'2'},{code:'042',parentId:'007',positionId:'3'},{code:'043',parentId:'007',positionId:'4'},{code:'"
                + "044',parentId:'027',positionId:'1'},{code:'048',parentId:'027',positionId:'3'},{code:'051',parentId:'027',positionId:'2'},{code:'062"
                + "',parentId:'017',positionId:'1'},{code:'063',parentId:'017',positionId:'2'},{code:'065',parentId:'000',positionId:'1'},{code:'066',p"
                + "arentId:'065',positionId:'1'},{code:'067',parentId:'065',positionId:'2'},{code:'068',parentId:'065',positionId:'3'}"

                + "]}";
        return strResponse;
    }
    public int prePrepareData(String strAids, String strCapks, String strTerminalParam) {
        if (TextUtils.isEmpty(strAids) || TextUtils.isEmpty(strCapks) || TextUtils.isEmpty(strTerminalParam)) {
            return -1;
        }

        Log.i("prePrepareData", "执行【IC参数（AID、CAPK等）初始化】操作 - prePrepareData");
        int rs = 0;

        try {
            JSONArray jsonAidArray = new JSONArray(strAids);
            int aidLen = jsonAidArray.length();
            try {
                emvDevice.clearAIDParam();
                rs = 1;
            } catch (DeviceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                rs = 0;
            }
            for (int i = 0; i < aidLen; i++) {
                String strAid = jsonAidArray.getJSONObject(i).getString("val");
                Log.i("prePrepareData", "AID[" + i + "]：" + strAid);
                byte[] bAid = CommonUtil.hexStr2Bytes(strAid);
                try {
                    emvDevice.setAIDParam(bAid);
                    rs = 1;
                } catch (DeviceException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    rs = 0;
                }
            }

            JSONArray jsonCapkArray = new JSONArray(strCapks);
            int capkLen = jsonCapkArray.length();
            try {
                emvDevice.clearCAPKParam();
                rs = 1;
            } catch (DeviceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                rs = 0;
            }
            for (int i = 0; i < capkLen; i++) {
                String strCapk = jsonCapkArray.getJSONObject(i).getString("val");
                //				 Log.i(TAG, "Capk[" + i + "]：" + strCapk);
                byte[] bCapk = CommonUtil.hexStr2Bytes(strCapk);
                try {
                    emvDevice.setCAPKParam(bCapk);
                    rs = 1;
                } catch (DeviceException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    rs = 0;
                }
            }

        } catch (JSONException e1) {

        }

        try {
            JSONObject jsonTermICParam = new JSONObject(strTerminalParam);
            EMVTermConfig mEMVTermConfig = new EMVTermConfig(); // IFD
            // TermTransProperty
            // StatusCheckSupport没找到相应的java来替代
            mEMVTermConfig.setCountryCode(jsonTermICParam.getString("cnCode"));
            mEMVTermConfig.setTermId(jsonTermICParam.getString("tid"));
            mEMVTermConfig.setTransCurrCode(jsonTermICParam.getString("curCode"));
            mEMVTermConfig.setCapability(jsonTermICParam.getString("cap"));
            String termType = jsonTermICParam.getString("termType");
            if (!TextUtils.isEmpty(termType) && CommonUtil.isNumeric(termType)) {
                mEMVTermConfig.setTermType(Integer.valueOf(termType));
            } else {
                mEMVTermConfig.setTermType(0x22);
            }
            String transCurExp = jsonTermICParam.getString("transCurExp");
            if (!TextUtils.isEmpty(transCurExp) && CommonUtil.isNumeric(transCurExp)) {
                mEMVTermConfig.setTransCurrExp(Integer.valueOf(transCurExp));
            } else {
                mEMVTermConfig.setTransCurrExp(0x02);
            }
            mEMVTermConfig.setExtCapability(jsonTermICParam.getString("adTermCap"));
            mEMVTermConfig.setMerchName(jsonTermICParam.getString("merName"));
            mEMVTermConfig.setMerchId("123456789000010");
            try {
                emvDevice.setTermConfig(mEMVTermConfig);
                rs = 1;
            } catch (DeviceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                rs = 0;
            }
            Log.i("prePrepareData", "执行【IC参数（AID、CAPK等）初始化】操作 , rs=" + rs);
        } catch (JSONException e) {
            rs = -2;
            Log.i("prePrepareData", "执行【IC卡终端参数初始化】操作异常 - prePrepareData");
        }

        return rs;
    }
}

