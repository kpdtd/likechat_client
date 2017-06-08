package com.fun.likechat.vo;

/**
 * <p>
 * 我的信息vo ：相当于用户vo（UserVo，名字不改了）
 * </p>
 * 修改记录:
 * 		(从这里添加，没有则删除此项)
 */
public class ActorVo {
	private Integer id;
	private String idcard;//'固定8位，不重复，无规则的数字。用于标识唯一用户（类似于房间号）'
	private String nickname;// '昵称'
	private String icon;//'头像存放相对地址'
	private String signature;//个性签名
	private String age;//年龄，直接填数  db is int（birthday）
	private String token;//
	private Integer sex;//1男2女
	
	
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
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}

