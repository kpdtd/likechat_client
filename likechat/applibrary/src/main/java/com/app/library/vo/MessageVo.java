package com.app.library.vo;

import com.app.library.entity.GsonObj;
import com.app.library.greendao.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * 在调用导航“消息”进入私密消息界面是，返回此vo的列表
 * 一般会返回1~3个MessageVo此时chat为空，在消息界面显示message内容即可
 * <p>
 * 在聊天界面，或点击消息进入聊天界面后会返回 唯一 1个messageVo
 * 此时chat里面包含N个机器人消息。隔几秒显示一条即可
 */
@Entity
public class MessageVo extends GsonObj<MessageVo>
{
    // 如果需要设置autoincrement = true,必须是Long,不能是int或者long
    @Id()
    private Long id;
    private Integer actorId;
    private String nickName;
    private String icon;//头像url
    private String message;
    private Date mdate;//这个时间最好客户端根据返回消息的时间做人性化转换【如，今天13：20分     9月1日  13：20分】
    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String> chat;//仅在聊天界面中有值，并逐条显示对话内容；

    @Generated(hash = 2141508235)
    public MessageVo(Long id, Integer actorId, String nickName, String icon,
            String message, Date mdate, List<String> chat) {
        this.id = id;
        this.actorId = actorId;
        this.nickName = nickName;
        this.icon = icon;
        this.message = message;
        this.mdate = mdate;
        this.chat = chat;
    }

    @Generated(hash = 855908687)
    public MessageVo() {
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getActorId()
    {
        return actorId;
    }

    public void setActorId(Integer actorId)
    {
        this.actorId = actorId;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public List<String> getChat()
    {
        return chat;
    }

    public void setChat(List<String> chat)
    {
        this.chat = chat;
    }

    public Date getMdate()
    {
        return mdate;
    }

    public void setMdate(Date mdate)
    {
        this.mdate = mdate;
    }

}
