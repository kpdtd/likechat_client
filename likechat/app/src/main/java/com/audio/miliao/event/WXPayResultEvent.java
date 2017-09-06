package com.audio.miliao.event;

/**
 * 微信支付结果
 */
public class WXPayResultEvent
{
    private int mPayResult = 0;

    /**
     * 微信支付结果
     * @param payResult 0：成功；其他都是失败
     */
    public WXPayResultEvent(int payResult)
    {
        mPayResult = payResult;
    }

    /**
     * 微信支付结果
     * @return 0 成功；其他都是失败
     */
    public int getPayResult()
    {
        return mPayResult;
    }
}
