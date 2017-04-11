package com.audio.miliao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.audio.miliao.R;

/**
 * 账户余额
 */
public class AccountBalanceActivity extends BaseActivity
{
    private CheckBox m_chk10;
    private CheckBox m_chk50;
    private CheckBox m_chk98;
    private CheckBox m_chk598;
    private CheckBox m_chk1598;
    private CheckBox m_chkInput;
    private RadioButton m_rdoAlipay;
    private RadioButton m_rdoWeixin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_balance);

        initUI();
        updateData();
    }

    private void initUI()
    {
        try
        {
            m_chk10 = (CheckBox) findViewById(R.id.chk_10);
            m_chk50 = (CheckBox) findViewById(R.id.chk_50);
            m_chk98 = (CheckBox) findViewById(R.id.chk_98);
            m_chk598 = (CheckBox) findViewById(R.id.chk_598);
            m_chk1598 = (CheckBox) findViewById(R.id.chk_1598);
            m_chkInput = (CheckBox) findViewById(R.id.chk_input);
            m_rdoAlipay = (RadioButton) findViewById(R.id.rdo_alipay);
            m_rdoWeixin = (RadioButton) findViewById(R.id.rdo_weixin);

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        switch (v.getId())
                        {
                        case R.id.chk_10:
                        case R.id.chk_50:
                        case R.id.chk_98:
                        case R.id.chk_598:
                        case R.id.chk_1598:
                        case R.id.chk_input:
                            setCheck((CheckBox) v);
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

            m_chk10.setOnClickListener(clickListener);
            m_chk50.setOnClickListener(clickListener);
            m_chk98.setOnClickListener(clickListener);
            m_chk598.setOnClickListener(clickListener);
            m_chk1598.setOnClickListener(clickListener);
            m_chkInput.setOnClickListener(clickListener);
            findViewById(R.id.img_back).setOnClickListener(clickListener);
            findViewById(R.id.txt_pay_now).setOnClickListener(clickListener);

            m_rdoAlipay.performClick();
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

    private void setCheck(CheckBox checkBox)
    {
        try
        {
            m_chk10.setChecked(false);
            m_chk50.setChecked(false);
            m_chk98.setChecked(false);
            m_chk598.setChecked(false);
            m_chk1598.setChecked(false);
            m_chkInput.setChecked(false);

            checkBox.setChecked(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
