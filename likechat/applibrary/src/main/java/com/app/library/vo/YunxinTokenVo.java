package com.app.library.vo;


import com.app.library.entity.GsonObj;

public class YunxinTokenVo  extends GsonObj<YunxinTokenVo>
{
	private Integer id;//db id
	private String openid;
	private String accid;//云信用户id，目前是以openid给云信，云信可能返回一个不同于openid的值
	private String token;//我方生成传给云信，数值上等于openid
	//private java.util.Date createTime;
	
	public void setId(Integer value) {
		this.id = value;
	}
	
	public Integer getId() {
		return this.id;
	}
	public void setOpenid(String value) {
		this.openid = value;
	}
	
	public String getOpenid() {
		return this.openid;
	}
	public void setAccid(String value) {
		this.accid = value;
	}
	
	public String getAccid() {
		return this.accid;
	}
	public void setToken(String value) {
		this.token = value;
	}
	
	public String getToken() {
		return this.token;
	}
//	public void setCreateTime(java.util.Date value) {
//		this.createTime = value;
//	}
//
//	public java.util.Date getCreateTime() {
//		return this.createTime;
//	}
}

