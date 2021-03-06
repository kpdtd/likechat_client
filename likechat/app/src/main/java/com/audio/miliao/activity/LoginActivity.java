package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.audio.miliao.R;
import com.audio.miliao.entity.AppData;
import com.audio.miliao.event.LoginEvent;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.WXPayCreateOrder;
import com.audio.miliao.pay.alipay.PayResult;
import com.audio.miliao.theApp;
import com.audio.miliao.util.QQUtil;
import com.audio.miliao.util.WXUtil;
import com.audio.miliao.util.YunXinUtil;
import com.uikit.loader.LoaderApp;
import com.uikit.loader.entity.Account;
import com.uikit.loader.entity.LoaderAppData;

import java.util.Map;

import de.greenrobot.event.EventBus;


/**
 * 登录
 */
public class LoginActivity extends BaseActivity
{
    private static final int CODE_ALIPAY_SDK_PAY_FLAG = 3;

    private EditText mEdittext;
    //private YXService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();

        //mService = new YXService(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * EventBus 在主线程的响应事件
     *
     * @param event 登录结果
     */
    public void onEventMainThread(LoginEvent event)
    {
        if (event.getIsSucceed())
        {
            // 登录成功
            onLoginSucceed();
        }
        else
        {
            // 登录失败
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        QQUtil.onActivityResult(requestCode, resultCode, data);
        //theApp.showToast("onActivityResult requestCode:" + requestCode + ";resultCode:" + resultCode);
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        case CODE_ALIPAY_SDK_PAY_FLAG:

            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000"))
            {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                //Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
                theApp.showToast("支付成功");
            }
            else
            {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                //Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
                theApp.showToast("支付失败");
            }
            break;
        case HttpUtil.RequestCode.WX_PAY_CREATE_ORDER:
            WXPayCreateOrder createOrder = (WXPayCreateOrder) msg.obj;
            if (WXPayCreateOrder.isSucceed(createOrder))
            {
                theApp.showToast("创建订单成功");
            }
            else
            {
                theApp.showToast("创建订单失败");
            }
            break;
        }
    }

    private void initUI()
    {
        try
        {
            mEdittext = (EditText) findViewById(R.id.txt_view);

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        switch (v.getId())
                        {
                        case R.id.txt_weixin_login:
                            onWXLogin();
                            break;
                        case R.id.txt_qq_login:
                            onQQLogin();
                            break;
                        case R.id.btn_yunxin_login:
                            YunXinUtil.login(LoaderAppData.getYunXinAccount(), LoaderAppData.getYunXinToken());
                            break;
                        case R.id.img_back:
                            finish();
                            break;
                        case R.id.btn_login_debug1:
                            LoaderAppData.setYunXinAccount("liu1501134");
                            LoaderAppData.setYunXinToken("e10adc3949ba59abbe56e057f20f883e");
                            LoaderApp.setCurAccount(new Account("liu1501134", "e10adc3949ba59abbe56e057f20f883e"));
                            YunXinUtil.login(LoaderAppData.getYunXinAccount(), LoaderAppData.getYunXinToken());
                            break;
                        case R.id.btn_login_debug2:
                            LoaderAppData.setYunXinAccount("18178619319");
                            LoaderAppData.setYunXinToken("e10adc3949ba59abbe56e057f20f883e");
                            LoaderApp.setCurAccount(new Account("18178619319", "e10adc3949ba59abbe56e057f20f883e"));
                            YunXinUtil.login(LoaderAppData.getYunXinAccount(), LoaderAppData.getYunXinToken());
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            findViewById(R.id.txt_weixin_login).setOnClickListener(clickListener);
            findViewById(R.id.txt_qq_login).setOnClickListener(clickListener);
            findViewById(R.id.btn_yunxin_login).setOnClickListener(clickListener);
            findViewById(R.id.img_back).setOnClickListener(clickListener);
            findViewById(R.id.btn_login_debug1).setOnClickListener(clickListener);
            findViewById(R.id.btn_login_debug2).setOnClickListener(clickListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onWXLogin()
    {
        WXUtil.login();
    }

    private void onQQLogin()
    {
        QQUtil.login(this);
    }

    private void onLoginSucceed()
    {
        try
        {
            // 云信的account和token都是openId
            LoaderAppData.setYunXinAccount(AppData.getUserInfo().getOpenId().toLowerCase());
            LoaderAppData.setYunXinToken(AppData.getUserInfo().getOpenId());
            LoaderApp.setCurAccount(new Account(LoaderAppData.getYunXinAccount(), LoaderAppData.getToken()));

            YunXinUtil.login(LoaderAppData.getYunXinAccount(), LoaderAppData.getYunXinToken());

            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);

            finish();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

//    private void onAlipay()
//    {
//        /**
//         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
//         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
//         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
//         *
//         * orderInfo的获取必须来自服务端；
//         */
//        boolean rsa2 = (Constant.RSA2_PRIVATE.length() > 0);
//        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(Constant.APP_ID, rsa2, null);
//        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
//
//        String privateKey = rsa2 ? Constant.RSA2_PRIVATE : Constant.RSA_PRIVATE;
//        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
//        final String orderInfo = orderParam + "&" + sign;
//
//        Runnable payRunnable = new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                // 使用沙箱环境
//                //EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
//                PayTask alipay = new PayTask(LoginActivity.this);
//                Map<String, String> result = alipay.payV2(orderInfo, true);
//                Log.i("msp", result.toString());
//
//                Message msg = new Message();
//                msg.what = CODE_ALIPAY_SDK_PAY_FLAG;
//                msg.obj = result;
//                handler().sendMessage(msg);
//            }
//        };
//
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();
//    }

//    private void onWxPay()
//    {
//        theApp.showToast("获取订单中...");
//        WXPayCreateOrder createOrder = new WXPayCreateOrder(handler(),  null);
//        createOrder.send();
//    }
}
