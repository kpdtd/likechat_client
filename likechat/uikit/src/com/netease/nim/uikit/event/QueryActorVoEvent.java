package com.netease.nim.uikit.event;

/**
 * 查询主播信息事件
 * 在uikit向app获取ActorVo
 */
public class QueryActorVoEvent
{
    public String yunxinId;
    public QueryActorVoEvent(String yunxinId)
    {
        this.yunxinId = yunxinId;
    }

    public String getYunxinId()
    {
        return yunxinId;
    }
}
