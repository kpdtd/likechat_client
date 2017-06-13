package com.audio.miliao.vo;

import com.audio.miliao.entity.GsonObj;

/**
 * <p>
 * 微信和qq登录注册接口。
 *    app成功在为微信或qq登录后，获取用户相关信息，包装成此vo对象传递给服务器
 * </p>
 * 修改记录:
 * 		(从这里添加，没有则删除此项)
 */
public class UserRegisterVo extends GsonObj<UserRegisterVo>
{
	private String openId;
	private String loginType;//qq  weixin
	private String nickname;// '昵称'
	private String icon;//头像访问地址
	private String signature;//个性签名
	private String province;//个性签名
	private String city;//个性签名
	private String sex;//直接填入：男    女
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLogin_type(String loginType) {
		this.loginType = loginType;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
}

