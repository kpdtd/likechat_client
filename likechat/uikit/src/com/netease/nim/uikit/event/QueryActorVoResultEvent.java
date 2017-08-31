package com.netease.nim.uikit.event;

import com.netease.nim.uikit.miliao.vo.ActorVo;

/**
 * 查询主播信息结果事件
 * app向uikit返回actorVo
 */
public class QueryActorVoResultEvent
{
    private ActorVo actorVo;
    public QueryActorVoResultEvent(ActorVo actorVo)
    {
        this.actorVo = actorVo;
    }

    public ActorVo getActorVo()
    {
        return actorVo;
    }
}
