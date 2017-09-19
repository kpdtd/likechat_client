package com.audio.miliao.pay.alipay;

import com.app.library.entity.GsonObj;

/**
 * Created by liujiye-pc on 2017/7/11.
 * 支付宝支付请求
 */
public class AlipayReq extends GsonObj<AlipayReq>
{
    public String timeout_express = "30m";
    public String product_code = "QUICK_MSECURITY_PAY";
    public String total_amount;
    public String subject;
    public String body;
    public String out_trade_no;
}
