package com.fun.likechat.vo;

import java.util.*;

public class GoodsVo {
	private Integer id;
	private String name;//商品名称  如：10金币
	private String subname;//商品子名称：预留
	private String pic;//商品图片
	private String displayPrice;//商品显示价格--如：1元：根据身份，等级等，每个人显示价格不一致。后台控制
	private Integer realPrice;//新增：这里是参与扣款的实际金额。--单位分
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubname() {
		return subname;
	}
	public void setSubname(String subname) {
		this.subname = subname;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getDisplayPrice() {
		return displayPrice;
	}
	public void setDisplayPrice(String displayPrice) {
		this.displayPrice = displayPrice;
	}
	public Integer getRealPrice() {
		return realPrice;
	}
	public void setRealPrice(Integer realPrice) {
		this.realPrice = realPrice;
	}
}

