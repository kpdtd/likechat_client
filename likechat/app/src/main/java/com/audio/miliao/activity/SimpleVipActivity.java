package com.audio.miliao.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.event.WXPayResultEvent;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.FetchVipMember;
import com.audio.miliao.listener.PayListener;
import com.audio.miliao.theApp;
import com.audio.miliao.util.AlipayUtil;
import com.audio.miliao.util.WXUtil;
import com.netease.nim.uikit.miliao.vo.GoodsVo;
import com.netease.nim.uikit.miliao.vo.VipMemberVo;

import de.greenrobot.event.EventBus;

/**
 * 简单的购买会员
 */
public class SimpleVipActivity extends BaseActivity
{
    private CheckBox m_chkSilver;
    private TextView m_txtNameSilver;
    private TextView m_txtSubnameSilver;
    private CheckBox m_chkGold;
    private TextView m_txtNameGold;
    private TextView m_txtSubnameGold;
    private CheckBox m_chkDiamond;
    private TextView m_txtNameDiamond;
    private TextView m_txtSubnameDiamond;
    private CheckBox m_chkExtreme;
    private TextView m_txtNameExtreme;
    private TextView m_txtSubnameExtreme;
    private TextView m_txtPayNow;
    private RadioButton m_rdoAlipay;
    private RadioButton m_rdoWeixin;

    private VipMemberVo m_vipMemberVo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_vip);

        try
        {
            initUI();

            FetchVipMember fetchVipMember = new FetchVipMember(handler(), null);
            fetchVipMember.send();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initUI()
    {
        try
        {
            m_chkSilver = (CheckBox) findViewById(R.id.chk_level_silver);
            m_txtNameSilver = (TextView) findViewById(R.id.txt_name_level_silver);
            m_txtSubnameSilver = (TextView) findViewById(R.id.txt_subname_level_silver);
            m_chkGold = (CheckBox) findViewById(R.id.chk_level_gold);
            m_txtNameGold = (TextView) findViewById(R.id.txt_name_level_gold);
            m_txtSubnameGold = (TextView) findViewById(R.id.txt_subname_level_gold);
            m_chkDiamond = (CheckBox) findViewById(R.id.chk_level_diamond);
            m_txtNameDiamond = (TextView) findViewById(R.id.txt_name_level_diamond);
            m_txtSubnameDiamond = (TextView) findViewById(R.id.txt_subname_level_diamond);
            m_chkExtreme = (CheckBox) findViewById(R.id.chk_level_extreme);
            m_txtNameExtreme = (TextView) findViewById(R.id.txt_name_level_extreme);
            m_txtSubnameExtreme = (TextView) findViewById(R.id.txt_subname_level_extreme);
            //m_txtVipLevel = (TextView) findViewById(R.id.txt_vip_member_level);
            //m_txtVipRemainTime = (TextView) findViewById(R.id.txt_vip_remain_time);
            m_rdoAlipay = (RadioButton) findViewById(R.id.rdo_alipay);
            m_rdoWeixin = (RadioButton) findViewById(R.id.rdo_weixin);
            m_txtPayNow = (TextView) findViewById(R.id.txt_pay_now);

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        switch (v.getId())
                        {
                        case R.id.lay_level_silver:
                        case R.id.chk_level_silver:
                            setPriceCheck(m_chkSilver);
                            break;
                        case R.id.lay_level_gold:
                        case R.id.chk_level_gold:
                            setPriceCheck(m_chkGold);
                            break;
                        case R.id.lay_level_diamond:
                        case R.id.chk_level_diamond:
                            setPriceCheck(m_chkDiamond);
                            break;
                        case R.id.lay_level_extreme:
                        case R.id.chk_level_extreme:
                            setPriceCheck(m_chkExtreme);
                            break;
                        case R.id.divider:
                            finish();
                            break;
                        case R.id.txt_pay_now:
                            GoodsVo goodsVo = (GoodsVo) m_txtPayNow.getTag();
                            if (goodsVo != null)
                            {
                                if (m_rdoAlipay.isChecked())
                                {
                                    onAlipay(goodsVo);
                                }
                                else if (m_rdoWeixin.isChecked())
                                {
                                    onWxPay(goodsVo);
                                }
                            }
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            // 只显示前三个vip选项，第四个至尊皇冠不显示了
            findViewById(R.id.divider3).setVisibility(View.GONE);
            findViewById(R.id.lay_level_extreme).setVisibility(View.GONE);

            findViewById(R.id.divider).setOnClickListener(clickListener);
            findViewById(R.id.txt_pay_now).setOnClickListener(clickListener);
            findViewById(R.id.lay_level_silver).setOnClickListener(clickListener);
            findViewById(R.id.lay_level_gold).setOnClickListener(clickListener);
            findViewById(R.id.lay_level_diamond).setOnClickListener(clickListener);
            findViewById(R.id.lay_level_extreme).setOnClickListener(clickListener);
            findViewById(R.id.chk_level_silver).setOnClickListener(clickListener);
            findViewById(R.id.chk_level_gold).setOnClickListener(clickListener);
            findViewById(R.id.chk_level_diamond).setOnClickListener(clickListener);
            findViewById(R.id.chk_level_extreme).setOnClickListener(clickListener);

            //findViewById(R.id.lay_level_gold).performClick();
            findViewById(R.id.rdo_alipay).performClick();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateData()
    {
        try
        {
            if (m_vipMemberVo == null)
            {
                return;
            }

            setGoodsInfo(m_txtNameSilver, m_txtSubnameSilver, m_chkSilver, m_vipMemberVo.getGoods().get(0));
            setGoodsInfo(m_txtNameGold, m_txtSubnameGold, m_chkGold, m_vipMemberVo.getGoods().get(1));
            setGoodsInfo(m_txtNameDiamond, m_txtSubnameDiamond, m_chkDiamond, m_vipMemberVo.getGoods().get(2));
            setGoodsInfo(m_txtNameExtreme, m_txtSubnameExtreme, m_chkExtreme, m_vipMemberVo.getGoods().get(3));

            setPriceCheck(m_chkGold);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setGoodsInfo(TextView txtName, TextView txtSubname, CheckBox check, GoodsVo goods)
    {
        txtName.setText(goods.getName());
        txtSubname.setText(goods.getSubname());

        txtName.setTag(goods.getId());
        txtSubname.setTag(goods.getId());
        check.setTag(goods);
    }

    private void setPriceCheck(CheckBox check)
    {
        try
        {
            m_chkSilver.setChecked(false);
            m_chkGold.setChecked(false);
            m_chkDiamond.setChecked(false);
            m_chkExtreme.setChecked(false);

            check.setChecked(true);
            m_txtPayNow.setTag(check.getTag());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 支付宝支付
     *
     * @param goodsVo
     */
    private void onAlipay(GoodsVo goodsVo)
    {
        try
        {
            int goodsId = goodsVo.getId(); // 购买会员
            m_txtPayNow.setEnabled(false);
            AlipayUtil.pay(this, "2", goodsId, goodsVo, new PayListener()
            {
                @Override
                public void onSucceed()
                {
                    onPaySucceed();
                }

                @Override
                public void onFailed(String error)
                {
                    setPayEnabled(true);
                    theApp.showToast("支付失败");
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 微信支付
     *
     * @param goodsVo
     */
    public void onWxPay(GoodsVo goodsVo)
    {
        try
        {
            m_txtPayNow.setEnabled(false);
            // 微信支付的返回结果需要通过eventbus异步返回，listener返回的结果不准确
            WXUtil.pay(goodsVo, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setPayEnabled(final boolean enabled)
    {
        handler().post(new Runnable()
        {
            @Override
            public void run()
            {
                m_txtPayNow.setEnabled(enabled);
            }
        });
    }

    /**
     * 微信支付结果
     * @param event
     */
    public void onEventMainThread(final WXPayResultEvent event)
    {
        switch (event.getPayResult())
        {
        // 支付成功
        case 0:
            onPaySucceed();
            break;
        default:
            setPayEnabled(true);
            theApp.showToast("支付失败");
            break;
        }
    }

    private void onPaySucceed()
    {
        setPayEnabled(true);
        theApp.showToast("支付成功");
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        case HttpUtil.RequestCode.FETCH_VIP_MEMBER:
            FetchVipMember fetchVipMember = (FetchVipMember) msg.obj;
            if (FetchVipMember.isSucceed(fetchVipMember))
            {
                m_vipMemberVo = fetchVipMember.rspVipMember;
                updateData();
            }
            break;
        }
    }
}
