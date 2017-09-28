package com.app.library.vo;

import com.app.library.entity.GsonObj;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 用于聊天对话中区分消息的发送者
 */
@Entity
public class ChatMsg extends GsonObj<ChatMsg>
{
    // 如果需要设置autoincrement = true,必须是Long,不能是int或者long
    @Id(autoincrement = true)
    private Long id;
    /** 消息聊天对象 */
    private int actorId;
    private String text;
    /** 消息发送者Id */
    private int senderId;
    /** 消息发送者头像 */
    private String senderAvatar;

    @Generated(hash = 78964690)
    public ChatMsg(Long id, int actorId, String text, int senderId,
            String senderAvatar) {
        this.id = id;
        this.actorId = actorId;
        this.text = text;
        this.senderId = senderId;
        this.senderAvatar = senderAvatar;
    }
    @Generated(hash = 1355502543)
    public ChatMsg() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getActorId() {
        return this.actorId;
    }
    public void setActorId(int actorId) {
        this.actorId = actorId;
    }
    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public int getSenderId() {
        return this.senderId;
    }
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }
    public String getSenderAvatar() {
        return this.senderAvatar;
    }
    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }
}
