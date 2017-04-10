package com.audio.miliao.entity;

import java.io.Serializable;

/**
 * 通话记录
 */
public class CallHistory implements Serializable
{
    /** 发起者 */
    public User from;
    /** 接收者 */
    public User to;
    /** 开始时间（毫秒） */
    public long startTime;
    /** 通话时间(秒) */
    public int talkTime;
}
