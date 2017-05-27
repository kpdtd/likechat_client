package com.audio.miliao.util;


import android.content.Intent;

import com.audio.miliao.theApp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

/**
 * 微信登录相关工具类
 */
public class WXUtil
{
    private final static String APP_ID = "wx9af1549f05e98da1";
    // 商户号
    private final static String MCH_ID = "1480109622";
    private final static String APP_SECRET = "c9e2fba886f2590a09514280b6b5c5ee";

    private static IWXAPI api;

    public static IWXAPI api()
    {
        return api;
    }

    private static void init()
    {
        if (api == null)
        {
            api = WXAPIFactory.createWXAPI(theApp.CONTEXT, APP_ID, false);
            api.registerApp(APP_ID);
        }
    }

    public static void login()
    {
        try
        {
            init();

            // send oauth request
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_likechat";
            api.sendReq(req);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            theApp.showToast(e.toString());
        }
    }

    public static void handleIntent(Intent intent, IWXAPIEventHandler wxHandler)
    {
        init();
        api.handleIntent(intent, wxHandler);
    }

    /**
     * 生成微信Oauth的url地址
     *
     * @param code
     * @return
     */
    public static String generateWXOauthURL(String code)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=");
        sb.append(APP_ID);
        sb.append("&secret=");
        sb.append(APP_SECRET);
        sb.append("&code=");
        sb.append(code);
        sb.append("&grant_type=authorization_code");
        return sb.toString();
    }

    public static String gengerateWXPayCreateOrderParams()
    {
        JSONObject json = new JSONObject();
        try
        {
            json.put("appid", APP_ID);
            json.put("mch_id", MCH_ID);
            json.put("device_info", "WEB"); // 非必须
            json.put("nonce_str", "" + System.currentTimeMillis()); // 随机字符串
            json.put("body", ""); // 商品描述
            json.put("out_trade_no", ""); // 商户订单号
            json.put("total_fee", ""); // 总金额
            json.put("spbill_create_ip", ""); // 终端IP
            json.put("notify_url", ""); // 通知地址
            json.put("trade_type", ""); // 交易类型

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return json.toString();
    }
}
