package com.audio.miliao.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.audio.miliao.entity.AppData;
import com.audio.miliao.pay.alipay.AlipayReq;
import com.audio.miliao.pay.alipay.Constant;
import com.audio.miliao.pay.alipay.OrderInfoUtil2_0;
import com.audio.miliao.vo.PayInfoVo;

import java.util.Map;

/**
 * 支付宝工具类
 */
public class AlipayUtil
{
    /**
     * 支付宝支付
     * @param alipayReq 支付宝支付请求
     */
    public static void pay(final Activity activity, final Handler handler, final int notifyCode,AlipayReq alipayReq)
    {
        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (Constant.RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(Constant.APP_ID, rsa2, alipayReq);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? Constant.RSA2_PRIVATE : Constant.RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                // 使用沙箱环境
                //EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = notifyCode;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     *
     * @param goodsType 1-购买嗨币  2-购买会员
     * @param goodsCode 商品号：如果type=1是嗨币，即金额*10（1：10）  如果type=2 则给出商品号或商品id。商品号是协商定义出来，商品不变，商品号不变。
     * @param money 钱(分)
     * @return
     */
    public static PayInfoVo createPayInfoVo(String goodsType, String goodsCode, Integer money)
    {
        PayInfoVo payInfoVo = new PayInfoVo();
        payInfoVo.setActorId(AppData.getCurUserId());
        payInfoVo.setOpenId(AppData.getOpenId());
        payInfoVo.setPayType("2");
        payInfoVo.setGoodsType(goodsType);
        if (goodsType.equals("1"))
        {
            payInfoVo.setGoodsCode(String.valueOf(money * 10));
        }
        else
        {
            payInfoVo.setGoodsCode(goodsCode);
        }
        payInfoVo.setMoney(money);
        payInfoVo.setOutTradeNo(OrderInfoUtil2_0.getOutTradeNo());
        payInfoVo.setTradeNo("");

        return payInfoVo;
    }
}
