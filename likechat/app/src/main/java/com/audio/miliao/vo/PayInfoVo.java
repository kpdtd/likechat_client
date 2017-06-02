package com.audio.miliao.vo;

import com.audio.miliao.entity.GsonObj;

public class PayInfoVo extends GsonObj<PayInfoVo>
{
	private Integer actorId;//用户id（主播和用户同一张表。）
	private String openId;//用户的oponId
	private String payType;//支付类型 1- 微信  2-支付宝
	private String goodsType;//1-购买嗨币  2-购买会员
	private String goodsCode;//商品号：如果type=1是嗨币，即金额*10（1：10）  如果type=2 则给出商品号或商品id。商品号是协商定义出来，商品不变，商品号不变。
	private Integer money;//金额
	private String outTradeNo;//商户网站唯一订单号-64,我方生成的订单号,同一个订单多次请求记为重复订单；
	private String tradeNo;//支付平台订单号
	private String payer;//支付账户
	private String payee;//收款账号
//	private String sige;//预留的签名信息
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public Integer getMoney() {
		return money;
	}
	public void setMoney(Integer money) {
		this.money = money;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public Integer getActorId() {
		return actorId;
	}
	public void setActorId(Integer actorId) {
		this.actorId = actorId;
	}
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	
	
}
