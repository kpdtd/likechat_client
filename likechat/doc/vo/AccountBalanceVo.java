package com.fun.likechat.vo;

import java.util.*;

/**
 * 返回
 * @author suntao
 *
 */
public class AccountBalanceVo {
	private Integer money;//余额，嗨币
	private List<GoodsVo> goods;
	/**
	 * name:10币
	 * subname：10元（建议使用这个字段）
	 * 或者
	 * displayPrice 10元 
	 */
	
	
	public Integer getMoney() {
		return money;
	}
	public void setMoney(Integer money) {
		this.money = money;
	}
	public List<GoodsVo> getGoods() {
		return goods;
	}
	public void setGoods(List<GoodsVo> goods) {
		this.goods = goods;
	}
	
}

