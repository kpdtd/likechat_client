package com.netease.nim.uikit.event;

import com.netease.nim.uikit.miliao.vo.ActorPageVo;

/**
 * 查询主播信息结果事件
 * app向uikit返回actorVo
 */
public class QueryActorVoResultEvent
{
    private ActorPageVo actorPageVo;
    public QueryActorVoResultEvent(ActorPageVo actorPageVo)
    {
        this.actorPageVo = actorPageVo;
    }

    public ActorPageVo getActorPageVo()
    {
        return actorPageVo;
    }
}
