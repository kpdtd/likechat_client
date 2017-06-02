package com.audio.miliao.vo;

import com.audio.miliao.entity.GsonObj;

import java.util.List;
/**
 * <p>
 * antor的主页vo
 * </p>
 * 修改记录:
 * 		(从这里添加，没有则删除此项)
 */
public class ActorPageVo extends GsonObj<ActorPageVo>
{
	private Integer id;
	private String idcard;//'固定8位，不重复，无规则的数字。用于标识唯一用户（类似于房间号）'
	private String icon;//'头像存放相对地址'
	private String nickname;// '昵称'
	private Integer sex;//1男2女
	private String age;//年龄，直接填数 db is int（birthday）
	private String province;//省
	private String city;//城市
	private String fans;//'粉丝数（冗余字段，应该是实时计算）'
	private String attention;//'关注数（冗余字段，应该是实时计算）'
	private String signature;//个性签名
	private String introduction;//自我介绍
	
	private String price;//资费价格（整数）转成 --》 1.5币/分
	private String callTime;//通话时长
	
	private Boolean isAttention;//是否已经关注，如果未登陆，点击关注应该进入登陆页面。如果已经登陆，当此属性为true，图标应显示红色
	
	
	
	private String videoUrl;//音频地址
	private List<String> picList;//主播相册地址列表

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFans() {
		return fans;
	}

	public void setFans(String fans) {
		this.fans = fans;
	}

	public String getAttention() {
		return attention;
	}

	public void setAttention(String attention) {
		this.attention = attention;
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

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
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

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public List<String> getPicList() {
		return picList;
	}

	public void setPicList(List<String> picList) {
		this.picList = picList;
	}

	public String getCallTime() {
		return callTime;
	}

	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}

	public Boolean getIsAttention() {
		return isAttention;
	}

	public void setIsAttention(Boolean isAttention) {
		this.isAttention = isAttention;
	}
	
}

