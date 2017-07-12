package com.audio.miliao.util;


import android.content.Intent;

import com.audio.miliao.http.cmd.CreateWXPayOrder;
import com.audio.miliao.listener.PayListener;
import com.audio.miliao.theApp;
import com.audio.miliao.vo.GoodsVo;
import com.audio.miliao.vo.WeChatUnifiedOrderReqVo;
import com.audio.miliao.vo.WeChatUnifiedOrderReturnVo;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

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
        try
        {
            if (null == api)
                init();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return api;
    }

    public static String app_id()
    {
        return APP_ID;
    }

    public static String mch_id()
    {
        return MCH_ID;
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

    /**
     * 支付
     *
     * @param goodsVo 商品信息
     * @param payListener 支付监听
     * @return
     */
    public static void pay(final GoodsVo goodsVo, final PayListener payListener)
    {
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    WeChatUnifiedOrderReqVo weChatUnifiedOrderReqVo = new WeChatUnifiedOrderReqVo();
                    weChatUnifiedOrderReqVo.setGoods_no(goodsVo.getRealPrice());
                    CreateWXPayOrder createOrder = new CreateWXPayOrder(null, weChatUnifiedOrderReqVo, null);
                    createOrder.sendSync();
                    if (CreateWXPayOrder.isSucceed(createOrder))
                    {
                        PayReq payReq = WXUtil.genWxPayReq(createOrder.rspOrderResult);
                        WXUtil.api().sendReq(payReq);
                        if (payListener != null)
                        {
                            payListener.onSucceed();
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    if (payListener != null)
                    {
                        payListener.onFailed("");
                    }
                }
            }
        };

        new Thread(runnable).start();
    }

    private static PayReq genWxPayReq(WeChatUnifiedOrderReturnVo wxOrderReturn)
    {
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        PayReq payReq = new PayReq();
        payReq.appId = wxOrderReturn.getAppid();
        payReq.partnerId = wxOrderReturn.getPartnerid();
        payReq.prepayId = wxOrderReturn.getPrepay_id();
        payReq.nonceStr = wxOrderReturn.getNoncestr();
        payReq.timeStamp = wxOrderReturn.getTimestamp();
        payReq.sign = wxOrderReturn.getSign();
        payReq.packageValue = wxOrderReturn.getPackageValue(); // 固定值
        payReq.extData = wxOrderReturn.getExtData(); // 可选择

        return payReq;
    }
}
