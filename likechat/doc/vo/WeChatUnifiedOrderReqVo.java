package com.fun.likechat.vo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 微信统一下单返回vo
 * @author 
 *
 */
public class WeChatUnifiedOrderReqVo {
	private String out_trade_no;//客户端生成的订单号，商品订单号24位，随机不重复 规则：YYYYMMDDHHMMSS+10位随机数
	private Integer goods_no;//商品id
	
	public WeChatUnifiedOrderReqVo() {
		try {
			DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			out_trade_no =  sdf.format(new Date()) + getRandomData(10);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
    }
	
	 /*
     * 返回长度为【strLength】的随机数，在前面补0
     */
	private String getRandomData(int strLength) {
		Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);
        // 返回固定的长度的随机数
        return fixLenthString.substring(2, strLength + 2);
	}
	

	
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public Integer getGoods_no() {
		return goods_no;
	}
	public void setGoods_no(Integer goods_no) {
		this.goods_no = goods_no;
	}
}
