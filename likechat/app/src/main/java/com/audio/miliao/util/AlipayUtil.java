package com.audio.miliao.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.audio.miliao.entity.AppData;
import com.audio.miliao.http.cmd.CreateAlipayOrder;
import com.audio.miliao.listener.PayListener;
import com.audio.miliao.pay.alipay.AlipayReq;
import com.audio.miliao.pay.alipay.Constant;
import com.audio.miliao.pay.alipay.OrderInfoUtil2_0;
import com.audio.miliao.pay.alipay.PayResult;
import com.audio.miliao.vo.GoodsVo;
import com.audio.miliao.vo.PayInfoVo;

import java.util.Map;

/**
 * 支付宝工具类
 */
public class AlipayUtil
{
    /**
     *
     * @param goodsType 1-购买嗨币  2-购买会员
     * @param goodsId 商品号：如果type=1是嗨币，即金额*10（1：10）  如果type=2 则给出商品号或商品id。商品号是协商定义出来，商品不变，商品号不变。
     * @param goodsVo 商品信息
     * @param payListener 支付监听器
     * @return
     */
    public static void pay(final Activity activity, final String goodsType,
                           final String goodsId, final GoodsVo goodsVo, final PayListener payListener)
    {
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    int realPrice = goodsVo.getRealPrice();
                    PayInfoVo payInfoVo = AlipayUtil.createPayInfoVo(goodsType, goodsId, realPrice);
                    CreateAlipayOrder createAlipayOrder = new CreateAlipayOrder(null, payInfoVo, null);
                    createAlipayOrder.sendSync();

                    if (CreateAlipayOrder.isSucceed(createAlipayOrder))
                    {
                        AlipayReq alipayReq = new AlipayReq();
                        float money = payInfoVo.getMoney(); // 分
                        //float money = 0.01f;
                        alipayReq.total_amount = String.valueOf(money * 0.01); // 元
                        alipayReq.subject = goodsVo.getName(); // subject
                        alipayReq.body = goodsVo.getSubname(); // body
                        alipayReq.out_trade_no = payInfoVo.getOutTradeNo();

                        paySync(activity, alipayReq, payListener);
                    }
                }
                catch (Exception e)
                {
                    if (payListener != null)
                    {
                        payListener.onFailed("");
                    }
                }
            }
        };

        new Thread(runnable).start();
    }

    private static void paySync(Activity activity, AlipayReq alipayReq, PayListener payListener)
    {
        try
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


            // 使用沙箱环境
            //EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
            PayTask alipay = new PayTask(activity);
            Map<String, String> result = alipay.payV2(orderInfo, true);

            PayResult payResult = new PayResult(result);
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
                //theApp.showToast("支付成功");
                if (payListener != null)
                {
                    payListener.onSucceed();
                }
            }
            else
            {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                //Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
                //theApp.showToast("支付失败");
                if (payListener != null)
                {
                    payListener.onFailed("");
                }
            }
        }
        catch (Exception e)
        {
            if (payListener != null)
            {
                payListener.onFailed("");
            }
        }
    }

    /**
     * 支付宝支付
     * @param alipayReq 支付宝支付请求
     */
    private static void pay(final Activity activity, final Handler handler, final int notifyCode, AlipayReq alipayReq)
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
    private static PayInfoVo createPayInfoVo(String goodsType, String goodsCode, Integer money)
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
