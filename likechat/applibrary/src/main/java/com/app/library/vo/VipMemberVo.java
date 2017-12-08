package com.app.library.vo;


import com.app.library.entity.GsonObj;

import java.util.List;

public class VipMemberVo extends GsonObj<VipMemberVo>
{
    private Integer isvip;//是否是vip会员：0-否  1-是',
    private String grade;//会员等级:  返回 （ 白银会员   黄金会员   钻石会员）其中一项
    private String vipActiveTime;//剩余时间，直接在客户端显示
    private List<GoodsVo> goods;//字段示例见下方

    /**
     * 是否是vip会员：0-否  1-是
     */

    public Integer getIsvip()
    {
        return isvip;
    }

    public void setIsvip(Integer isvip)
    {
        this.isvip = isvip;
    }

    /**
     * 会员等级
     * @return （ 白银会员   黄金会员   钻石会员）其中一项
     */
    public String getGrade()
    {
        return grade;
    }

    public void setGrade(String grade)
    {
        this.grade = grade;
    }

    public String getVipActiveTime()
    {
        return vipActiveTime;
    }

    public void setVipActiveTime(String vipActiveTime)
    {
        this.vipActiveTime = vipActiveTime;
    }

    public List<GoodsVo> getGoods()
    {
        return goods;
    }

    public void setGoods(List<GoodsVo> goods)
    {
        this.goods = goods;
    }
}

