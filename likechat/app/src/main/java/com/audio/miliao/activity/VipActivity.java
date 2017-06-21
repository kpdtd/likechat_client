package com.audio.miliao.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.FetchVipMember;
import com.audio.miliao.vo.GoodsVo;
import com.audio.miliao.vo.VipMemberVo;

/**
 * 会员中心
 */
public class VipActivity extends BaseActivity
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
    private TextView m_txtVipLevel;
    private TextView m_txtVipRemainTime;

    //private AccountVo m_accountVo;
    private VipMemberVo m_vipMemberVo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);

        try
        {
            initUI();
            //updateData();

            FetchVipMember fetchVipMember = new FetchVipMember(handler(), null);
            fetchVipMember.send();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
            m_txtVipLevel = (TextView) findViewById(R.id.txt_vip_member_level);
            m_txtVipRemainTime = (TextView) findViewById(R.id.txt_vip_remain_time);

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
                        case R.id.img_back:
                            finish();
                            break;
                        case R.id.txt_pay_now:
                            finish();
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            findViewById(R.id.img_back).setOnClickListener(clickListener);
            findViewById(R.id.txt_pay_now).setOnClickListener(clickListener);
            findViewById(R.id.lay_level_silver).setOnClickListener(clickListener);
            findViewById(R.id.lay_level_gold).setOnClickListener(clickListener);
            findViewById(R.id.lay_level_diamond).setOnClickListener(clickListener);
            findViewById(R.id.lay_level_extreme).setOnClickListener(clickListener);
            findViewById(R.id.chk_level_silver).setOnClickListener(clickListener);
            findViewById(R.id.chk_level_gold).setOnClickListener(clickListener);
            findViewById(R.id.chk_level_diamond).setOnClickListener(clickListener);
            findViewById(R.id.chk_level_extreme).setOnClickListener(clickListener);

            findViewById(R.id.lay_level_silver).performClick();
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

            m_txtVipLevel.setText(getString(R.string.txt_vip_member_level) + m_vipMemberVo.getGrade());
            String str = String.format(getString(R.string.txt_vip_remain_time), m_vipMemberVo.getVipActiveTime());
            m_txtVipRemainTime.setText(str);

            setGoodsInfo(m_txtNameSilver, m_txtSubnameSilver, m_vipMemberVo.getGoods().get(0));
            setGoodsInfo(m_txtNameGold, m_txtSubnameGold, m_vipMemberVo.getGoods().get(1));
            setGoodsInfo(m_txtNameDiamond, m_txtSubnameDiamond, m_vipMemberVo.getGoods().get(2));
            setGoodsInfo(m_txtNameExtreme, m_txtSubnameExtreme, m_vipMemberVo.getGoods().get(3));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setGoodsInfo(TextView txtName, TextView txtSubname, GoodsVo goods)
    {
        txtName.setText(goods.getName());
        txtSubname.setText(goods.getSubname());
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
