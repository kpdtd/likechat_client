package com.app.library.vo;

import com.app.library.entity.GsonObj;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 记录Message已读未读
 * 不把这个状态放在MessageVo中是为了更新数据方便
 */
@Entity
public class MessageStateVo extends GsonObj<MessageStateVo>
{
    // 如果需要设置autoincrement = true,必须是Long,不能是int或者long
    @Id()
    private Long id;
    private Long messageId;
    private boolean isRead;
    @Generated(hash = 1620101498)
    public MessageStateVo(Long id, Long messageId, boolean isRead) {
        this.id = id;
        this.messageId = messageId;
        this.isRead = isRead;
    }
    @Generated(hash = 1248162493)
    public MessageStateVo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getMessageId() {
        return this.messageId;
    }
    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
    public boolean getIsRead() {
        return this.isRead;
    }
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
