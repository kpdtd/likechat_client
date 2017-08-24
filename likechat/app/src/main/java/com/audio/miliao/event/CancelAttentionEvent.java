package com.audio.miliao.event;

import com.netease.nim.uikit.miliao.vo.ActorVo;

/**
 * EventBus发送的取消关注某人的消息
 */
public class CancelAttentionEvent extends BaseEvent
{
    private ActorVo actorVo;

    public CancelAttentionEvent(ActorVo actorVo)
    {
        this.actorVo = actorVo;
    }

    public ActorVo getActorVo()
    {
        return actorVo;
    }
}
