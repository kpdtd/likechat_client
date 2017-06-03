package com.audio.miliao.event;

import com.audio.miliao.vo.ActorVo;
import com.audio.miliao.vo.BannerVo;
import com.audio.miliao.vo.TagVo;

import java.util.List;

/**
 * 获取主界面内容结果Event
 */
public class FetchHomeContentEvent
{
    private List<TagVo> tagVos;
    private List<BannerVo> bannerVos;
    private List<ActorVo> actorVos;
    private boolean isSucceed = false;

    public FetchHomeContentEvent(List<TagVo> tagVos,
                                 List<BannerVo> bannerVos,
                                 List<ActorVo> actorVos)
    {
        this.tagVos = tagVos;
        this.bannerVos = bannerVos;
        this.actorVos = actorVos;
    }

    public List<TagVo> getTagVos()
    {
        return tagVos;
    }

    public List<BannerVo> getBannerVos()
    {
        return bannerVos;
    }

    public List<ActorVo> getActorVos()
    {
        return actorVos;
    }

    public void setIsSucceed(boolean isSucceed)
    {
        this.isSucceed = isSucceed;
    }

    public boolean isSucceed()
    {
        return isSucceed;
    }
}
