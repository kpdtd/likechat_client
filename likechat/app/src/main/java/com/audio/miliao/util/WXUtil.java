package com.audio.miliao.util;


import com.audio.miliao.theApp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信登录相关工具类
 */
public class WXUtil
{
    private final static String APP_ID = "wxb5954a6bfc557eea";
    private final static String APP_SECRET = "882c7229a818cb249df4efb0d48c1997";

    private static IWXAPI api;

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
}
