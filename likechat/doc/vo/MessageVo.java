package com.fun.likechat.vo;

import java.util.Date;
import java.util.List;
/**
 * 在调用导航“消息”进入私密消息界面是，返回此vo的列表
 *   一般会返回1~3个MessageVo此时chat为空，在消息界面显示message内容即可
 *   
 *   在聊天界面，或点击消息进入聊天界面后会返回 唯一 1个messageVo
 *		此时chat里面包含N个机器人消息。隔几秒显示一条即可
 */
public class MessageVo {
	private Integer id;
	private Integer actorId;
	private String nickName;
	private String icon;//头像url
	private String message;
	private Date mdate;//这个时间最好客户端根据返回消息的时间做人性化转换【如，今天13：20分     9月1日  13：20分】
	private List<String> chat;//仅在聊天界面中有值，并逐条显示对话内容；
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getActorId() {
		return actorId;
	}
	public void setActorId(Integer actorId) {
		this.actorId = actorId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<String> getChat() {
		return chat;
	}
	public void setChat(List<String> chat) {
		this.chat = chat;
	}
	public Date getMdate() {
		return mdate;
	}
	public void setMdate(Date mdate) {
		this.mdate = mdate;
	}
	
	
}
