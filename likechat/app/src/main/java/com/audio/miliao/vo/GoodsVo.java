package com.audio.miliao.vo;

import com.audio.miliao.entity.GsonObj;

public class GoodsVo extends GsonObj<GoodsVo>
{
    private Integer id;
    private String name;//商品名称  如：10金币
    private String subname;//商品子名称：预留
    private String pic;//商品图片
    private String displayPrice;//商品显示价格：根据身份，等级等，每个人显示价格不一致。后台控制

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSubname()
    {
        return subname;
    }

    public void setSubname(String subname)
    {
        this.subname = subname;
    }

    public String getPic()
    {
        return pic;
    }

    public void setPic(String pic)
    {
        this.pic = pic;
    }

    public String getDisplayPrice()
    {
        return displayPrice;
    }

    public void setDisplayPrice(String displayPrice)
    {
        this.displayPrice = displayPrice;
    }
}
