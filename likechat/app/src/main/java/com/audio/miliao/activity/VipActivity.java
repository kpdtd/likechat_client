package com.audio.miliao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.audio.miliao.R;

/**
 * 会员中心
 */
public class VipActivity extends BaseActivity
{
    private CheckBox m_chkSilver;
    private CheckBox m_chkGold;
    private CheckBox m_chkDiamond;
    private CheckBox m_chkExtreme;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);

        try
        {
            initUI();
            updateData();
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
}
