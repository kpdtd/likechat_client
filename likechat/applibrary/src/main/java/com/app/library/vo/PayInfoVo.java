package com.app.library.vo;


import com.app.library.entity.GsonObj;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PayInfoVo extends GsonObj<PayInfoVo>
{
    public PayInfoVo()
    {
        try
        {
            DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            outTradeNo = sdf.format(new Date()) + getRandomData(10);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private Integer actorId;//用户id（主播和用户同一张表。）
    private String openId;//用户的oponId
    private String payType;//支付类型 1- 微信  2-支付宝。（请准确填入）
    private String goodsType;//1-购买嗨币  2-购买会员（请准确填入） 3-红包
    private Integer goodsId;//商品id：
    private Integer money;//实际支付金额
    private String outTradeNo;//（构造函数中有生成的算法，可参考）商户网站唯一订单号-24【yyyyMMddHHmmss+10位随机数】 我方生成的订单号,同一个订单多次请求记为重复订单；
    private String tradeNo;//支付平台订单号，有则填入
    private String payer;//支付账户，有则填入
    private String payee;//收款账号，有则填入

    public String getOpenId()
    {
        return openId;
    }

    public void setOpenId(String openId)
    {
        this.openId = openId;
    }

    public String getPayType()
    {
        return payType;
    }

    public void setPayType(String payType)
    {
        this.payType = payType;
    }

    public String getGoodsType()
    {
        return goodsType;
    }

    public void setGoodsType(String goodsType)
    {
        this.goodsType = goodsType;
    }

    public Integer getMoney()
    {
        return money;
    }

    public void setMoney(Integer money)
    {
        this.money = money;
    }

    public String getOutTradeNo()
    {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo)
    {
        this.outTradeNo = outTradeNo;
    }

    public String getTradeNo()
    {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo)
    {
        this.tradeNo = tradeNo;
    }

    public Integer getActorId()
    {
        return actorId;
    }

    public void setActorId(Integer actorId)
    {
        this.actorId = actorId;
    }

    public String getPayer()
    {
        return payer;
    }

    public void setPayer(String payer)
    {
        this.payer = payer;
    }

    public String getPayee()
    {
        return payee;
    }

    public void setPayee(String payee)
    {
        this.payee = payee;
    }

    public Integer getGoodsId()
    {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId)
    {
        this.goodsId = goodsId;
    }


    /*
    * 返回长度为【strLength】的随机数，在前面补0
    */
    private String getRandomData(int strLength)
    {
        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);
        // 返回固定的长度的随机数
        return fixLenthString.substring(2, strLength + 2);
    }
}
