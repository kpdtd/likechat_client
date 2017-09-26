package com.audio.miliao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.library.vo.AccountBalanceVo;
import com.app.library.vo.GoodsVo;
import com.audio.miliao.R;
import com.audio.miliao.event.WXPayResultEvent;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.FetchAccountBalance;
import com.audio.miliao.listener.PayListener;
import com.audio.miliao.theApp;
import com.audio.miliao.util.AlipayUtil;
import com.audio.miliao.util.AppChecker;
import com.audio.miliao.util.WXUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 简单的账户余额
 */
public class SimpleBalanceActivity extends BaseActivity
{
    private TextView m_txtContactHer;
    private TextView m_txtChargeName1;
    private TextView m_txtChargeName2;
    private TextView m_txtChargeName3;
    private CheckBox m_chkChargeValue1;
    private CheckBox m_chkChargeValue2;
    private CheckBox m_chkChargeValue3;
    private RadioButton m_rdoAlipay;
    private RadioButton m_rdoWeixin;
    private TextView m_txtPayNow;

    private AccountBalanceVo m_accountBalanceVo;

    public static void show(Activity activity)
    {
        Intent intent = new Intent(activity, SimpleBalanceActivity.class);
        activity.startActivity(intent);
        // 打开activity没有动画，看起来像Dialog一样
        activity.overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_balance);

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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        // 设置关闭没有动画
        overridePendingTransition(0, 0);
    }

    private void initUI()
    {
        try
        {
            m_txtContactHer = (TextView) findViewById(R.id.txt_contact_her);

            m_txtChargeName1 = (TextView) findViewById(R.id.txt_charge_name1);
            m_txtChargeName2 = (TextView) findViewById(R.id.txt_charge_name2);
            m_txtChargeName3 = (TextView) findViewById(R.id.txt_charge_name3);
            m_chkChargeValue1 = (CheckBox) findViewById(R.id.txt_charge_value1);
            m_chkChargeValue2 = (CheckBox) findViewById(R.id.txt_charge_value2);
            m_chkChargeValue3 = (CheckBox) findViewById(R.id.txt_charge_value3);
            m_rdoAlipay = (RadioButton) findViewById(R.id.rdo_alipay);
            m_rdoWeixin = (RadioButton) findViewById(R.id.rdo_weixin);
            m_txtPayNow = (TextView) findViewById(R.id.txt_pay_now);

            // 加粗
            m_txtContactHer.getPaint().setFakeBoldText(true);

            m_rdoAlipay.setTextColor(getResources().getColorStateList(R.color.text_white_selector));
            m_rdoWeixin.setTextColor(getResources().getColorStateList(R.color.text_white_selector));

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        switch (v.getId())
                        {
                        case R.id.divider:
                            finish();
                            // 设置关闭没有动画
                            overridePendingTransition(0, 0);
                            break;
                        case R.id.lay_charge_item1:
                        case R.id.txt_charge_value1:
                            setCheck(m_chkChargeValue1);
                            break;
                        case R.id.lay_charge_item2:
                        case R.id.txt_charge_value2:
                            setCheck(m_chkChargeValue2);
                            break;
                        case R.id.lay_charge_item3:
                        case R.id.txt_charge_value3:
                            setCheck(m_chkChargeValue3);
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

            findViewById(R.id.lay_charge_item1).setOnClickListener(clickListener);
            findViewById(R.id.lay_charge_item2).setOnClickListener(clickListener);
            findViewById(R.id.lay_charge_item3).setOnClickListener(clickListener);
            findViewById(R.id.txt_charge_value1).setOnClickListener(clickListener);
            findViewById(R.id.txt_charge_value2).setOnClickListener(clickListener);
            findViewById(R.id.txt_charge_value3).setOnClickListener(clickListener);
            findViewById(R.id.divider).setOnClickListener(clickListener);
            m_txtPayNow.setOnClickListener(clickListener);

            //setCheck(m_chkChargeValue3);
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

            List<View> goodsViews = new ArrayList<>();
            goodsViews.add(findViewById(R.id.lay_charge_item1));
            goodsViews.add(findViewById(R.id.lay_charge_item2));
            goodsViews.add(findViewById(R.id.lay_charge_item3));

            int i = 0;
            //for (GoodsVo goodsVo : m_accountBalanceVo.getGoods())
            for (View view : goodsViews)
            {
                ViewGroup viewGroup = (ViewGroup) view;
                GoodsVo goodsVo = m_accountBalanceVo.getGoods().get(i);

                TextView textView = (TextView) viewGroup.getChildAt(0);
                CheckBox checkBox = (CheckBox) viewGroup.getChildAt(1);

                textView.setText(goodsVo.getName());
                checkBox.setText(goodsVo.getDisplayPrice());

                textView.setTag(goodsVo);
                checkBox.setTag(goodsVo);
                i++;
            }

            setCheck(m_chkChargeValue3);
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
            m_chkChargeValue1.setChecked(false);
            m_chkChargeValue2.setChecked(false);
            m_chkChargeValue3.setChecked(false);

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
            if (!AppChecker.isWechatInstalled(getApplicationContext()))
            {
                theApp.showToast(getString(R.string.toast_wx_not_installed));
                return;
            }

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

        finish();
        // 设置关闭没有动画
        overridePendingTransition(0, 0);
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
