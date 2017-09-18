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
import com.audio.miliao.http.cmd.FetchAccountBalance;
import com.audio.miliao.listener.PayListener;
import com.audio.miliao.theApp;
import com.audio.miliao.util.AlipayUtil;
import com.audio.miliao.util.WXUtil;
import com.netease.nim.uikit.miliao.vo.AccountBalanceVo;
import com.netease.nim.uikit.miliao.vo.GoodsVo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 账户余额
 */
public class BalanceActivity extends BaseActivity
{
    private static final int MSG_ALIPAY = 1000;

    private CheckBox m_chk10;
    private CheckBox m_chk50;
    private CheckBox m_chk98;
    private CheckBox m_chk598;
    private CheckBox m_chk1598;
    private CheckBox m_chkInput;
    private RadioButton m_rdoAlipay;
    private RadioButton m_rdoWeixin;
    private TextView m_txtPayNow;
    private TextView m_txtAccountBalance;

    private AccountBalanceVo m_accountBalanceVo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        initUI();
        //updateData();
        FetchAccountBalance fetchAccountBalance = new FetchAccountBalance(handler(), null);
        fetchAccountBalance.send();

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
            m_chk10 = (CheckBox) findViewById(R.id.chk_10);
            m_chk50 = (CheckBox) findViewById(R.id.chk_50);
            m_chk98 = (CheckBox) findViewById(R.id.chk_98);
            m_chk598 = (CheckBox) findViewById(R.id.chk_598);
            m_chk1598 = (CheckBox) findViewById(R.id.chk_1598);
            m_chkInput = (CheckBox) findViewById(R.id.chk_input);
            m_rdoAlipay = (RadioButton) findViewById(R.id.rdo_alipay);
            m_rdoWeixin = (RadioButton) findViewById(R.id.rdo_weixin);
            m_txtPayNow = (TextView) findViewById(R.id.txt_pay_now);
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

            m_chk10.setOnClickListener(clickListener);
            m_chk50.setOnClickListener(clickListener);
            m_chk98.setOnClickListener(clickListener);
            m_chk598.setOnClickListener(clickListener);
            m_chk1598.setOnClickListener(clickListener);
            m_chkInput.setOnClickListener(clickListener);
            findViewById(R.id.img_back).setOnClickListener(clickListener);
            m_txtPayNow.setOnClickListener(clickListener);

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

            int money = (m_accountBalanceVo.getMoney() != null ? m_accountBalanceVo.getMoney() : 0);
            m_txtAccountBalance.setText(String.valueOf(money));

            List<View> goodsViews = new ArrayList<>();
            goodsViews.add(m_chk10);
            goodsViews.add(m_chk50);
            goodsViews.add(m_chk98);
            goodsViews.add(m_chk598);
            goodsViews.add(m_chk1598);
            goodsViews.add(m_chkInput);
            int i = 0;
            for (GoodsVo goodsVo : m_accountBalanceVo.getGoods())
            {
                TextView view = (TextView) goodsViews.get(i);
                String strText = goodsVo.getName() + "\n" + goodsVo.getDisplayPrice();
                view.setText(strText);
                view.setTag(goodsVo);
                i++;
            }

            setCheck(m_chk10);
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
            m_txtPayNow.setTag(checkBox.getTag());
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
            //String goodsId = ""; // 购买hi币，goodsId 根据价格生成
            m_txtPayNow.setEnabled(false);
            AlipayUtil.pay(this, "1", goodsVo.getId(), goodsVo, new PayListener()
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

        FetchAccountBalance fetchAccountBalance = new FetchAccountBalance(handler(), null);
        fetchAccountBalance.send();
        theApp.showToast("支付成功");
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
