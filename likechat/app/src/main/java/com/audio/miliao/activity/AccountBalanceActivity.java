package com.audio.miliao.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.FetchAccountBalance;
import com.audio.miliao.vo.AccountBalanceVo;
import com.audio.miliao.vo.GoodsVo;

import java.util.ArrayList;
import java.util.List;

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
    private TextView m_txtAccountBalance;

    private AccountBalanceVo m_accountBalanceVo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_balance);

        initUI();
        //updateData();
        FetchAccountBalance fetchAccountBalance = new FetchAccountBalance(handler(), null);
        fetchAccountBalance.send();
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
            m_txtAccountBalance = (TextView) findViewById(R.id.txt_account_balance);

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
            if (m_accountBalanceVo == null)
            {
                return;
            }

            m_txtAccountBalance.setText(String.valueOf(m_accountBalanceVo.getMoney()));

            List<View> goodsViews = new ArrayList<>();
            goodsViews.add(m_chk10);
            goodsViews.add(m_chk50);
            goodsViews.add(m_chk98);
            goodsViews.add(m_chk598);
            goodsViews.add(m_chk1598);
            goodsViews.add(m_chkInput);
            int i = 0;
            for(GoodsVo goodsVo : m_accountBalanceVo.getGoods())
            {
                TextView view = (TextView) goodsViews.get(i);
                String strText = goodsVo.getName() + "\n" + goodsVo.getDisplayPrice();
                view.setText(strText);
                i++;
            }
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

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        case HttpUtil.RequestCode.FETCH_ACCOUNT_BALANCE:
            FetchAccountBalance fetchAccountBalance = (FetchAccountBalance) msg.obj;
            if (FetchAccountBalance.isSucceed(fetchAccountBalance))
            {
                m_accountBalanceVo = fetchAccountBalance.rspAccountBalanceVo;
                updateData();
            }
            break;
        }
    }
}
