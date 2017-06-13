package com.audio.miliao.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.FetchAccountInfo;
import com.audio.miliao.vo.AccountVo;

import java.util.Date;

/**
 * 会员中心
 */
public class VipActivity extends BaseActivity
{
    private CheckBox m_chkSilver;
    private CheckBox m_chkGold;
    private CheckBox m_chkDiamond;
    private CheckBox m_chkExtreme;
    private TextView m_txtVipLevel;
    private TextView m_txtVipRemainTime;

    private AccountVo m_accountVo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);

        try
        {
            initUI();
            //updateData();
            FetchAccountInfo fetchAccountInfo = new FetchAccountInfo(handler(), null);
            fetchAccountInfo.send();
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
            m_chkGold = (CheckBox) findViewById(R.id.chk_level_gold);
            m_chkDiamond = (CheckBox) findViewById(R.id.chk_level_diamond);
            m_chkExtreme = (CheckBox) findViewById(R.id.chk_level_extreme);
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
            if (m_accountVo == null)
            {
                return;
            }

            m_txtVipLevel.setText(getString(R.string.txt_vip_member_level) + m_accountVo.getGrade());
            String str = String.format(getString(R.string.txt_vip_remain_time), calcRemainTime());
            m_txtVipRemainTime.setText(str);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
        case HttpUtil.RequestCode.FETCH_ACCOUNT_INFO:
            FetchAccountInfo fetchAccountInfo = (FetchAccountInfo) msg.obj;
            if (FetchAccountInfo.isSucceed(fetchAccountInfo))
            {
                m_accountVo = fetchAccountInfo.rspAccountVo;
                updateData();
            }
            break;
        }
    }

    /**
     * 计算vip账户剩余时间
     * @return
     */
    private long calcRemainTime()
    {
        Date date = m_accountVo.getVipActiveTime();
        Date today = new Date();

        long diff = date.getTime() - today.getTime();
        diff = (diff >= 0 ? diff : 0);
        long diffDay = diff / 24 * 60 * 60 * 1000;

        return diffDay;
    }
}
