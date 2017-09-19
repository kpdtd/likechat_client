package com.app.library.vo;


import com.app.library.entity.GsonObj;

public class AccountVo extends GsonObj<AccountVo>
{
    private Integer userId;
    private Integer isvip;//是否是vip会员：0-否  1-是',
    private Integer grade;//用户等级，还未使用
    private String vipActiveTime;//vip有效时间，预留的过期提醒
    private Integer money;//余额，嗨币

    public void setUserId(Integer value)
    {
        this.userId = value;
    }

    public Integer getUserId()
    {
        return this.userId;
    }

    public void setIsvip(Integer value)
    {
        this.isvip = value;
    }

    public Integer getIsvip()
    {
        return this.isvip;
    }

    public void setGrade(Integer value)
    {
        this.grade = value;
    }

    public Integer getGrade()
    {
        return this.grade;
    }

    public void setVipActiveTime(String value)
    {
        this.vipActiveTime = value;
    }

    public String getVipActiveTime()
    {
        return this.vipActiveTime;
    }

    public void setMoney(Integer value)
    {
        this.money = value;
    }

    public Integer getMoney()
    {
        return this.money;
    }
}

