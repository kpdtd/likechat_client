package com.audio.miliao.vo;

import com.audio.miliao.entity.GsonObj;

import java.util.List;

/**
 * 返回
 *
 * @author suntao
 */
public class AccountBalanceVo extends GsonObj<AccountBalanceVo>
{
    private Integer money;//余额，嗨币
    private List<GoodsVo> goods;

    public Integer getMoney()
    {
        return money;
    }

    public void setMoney(Integer money)
    {
        this.money = money;
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

