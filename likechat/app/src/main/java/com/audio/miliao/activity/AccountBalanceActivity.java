package com.audio.miliao.activity;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.audio.miliao.R;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.CreateAlipayOrder;
import com.audio.miliao.http.cmd.CreateWXPayOrder;
import com.audio.miliao.http.cmd.FetchAccountBalance;
import com.audio.miliao.pay.alipay.AlipayReq;
import com.audio.miliao.pay.alipay.PayResult;
import com.audio.miliao.theApp;
import com.audio.miliao.util.AlipayUtil;
import com.audio.miliao.util.WXUtil;
import com.audio.miliao.vo.AccountBalanceVo;
import com.audio.miliao.vo.GoodsVo;
import com.audio.miliao.vo.PayInfoVo;
import com.audio.miliao.vo.WeChatUnifiedOrderReqVo;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 账户余额
 */
public class AccountBalanceActivity extends BaseActivity
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
    private WeChatUnifiedOrderReqVo m_weChatUnifiedOrderReqVo;

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
            for (GoodsVo goodsVo : m_accountBalanceVo.getGoods())
            {
                TextView view = (TextView) goodsViews.get(i);
                String strText = goodsVo.getName() + "\n" + goodsVo.getDisplayPrice();
                view.setText(strText);
                view.setTag(goodsVo);
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
            String goodsCode = ""; // 购买hi币，goodsCode 根据价格生成
            PayInfoVo payInfoVo = AlipayUtil.createPayInfoVo("1", goodsCode, Integer.valueOf(goodsVo.getDisplayPrice()));
            CreateAlipayOrder createAlipayOrder = new CreateAlipayOrder(handler(), payInfoVo, null);
            createAlipayOrder.send();
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
            theApp.showToast("获取订单中...");
            m_weChatUnifiedOrderReqVo = new WeChatUnifiedOrderReqVo();
            m_weChatUnifiedOrderReqVo.setGoods_no(goodsVo.getId());
            CreateWXPayOrder createOrder = new CreateWXPayOrder(handler(), m_weChatUnifiedOrderReqVo, null);
            createOrder.send();
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
        case HttpUtil.RequestCode.WX_PAY_CREATE_ORDER:
            CreateWXPayOrder createOrder = (CreateWXPayOrder) msg.obj;
            if (CreateWXPayOrder.isSucceed(createOrder))
            {
                theApp.showToast("创建订单成功");
                PayReq payReq = WXUtil.genWxPayReq(createOrder.rspOrderResult);
                WXUtil.api().sendReq(payReq);
            }
            else
            {
                theApp.showToast("创建订单失败");
            }
            break;

        case HttpUtil.RequestCode.CREATE_ALIPAY_ORDER:
            CreateAlipayOrder createAlipayOrder = (CreateAlipayOrder) msg.obj;
            if (CreateAlipayOrder.isSucceed(createAlipayOrder))
            {
                AlipayReq alipayReq = new AlipayReq();
                PayInfoVo payInfoVo = createAlipayOrder.reqPayInfoVo;
                float money = payInfoVo.getMoney() * 0.01f;
                alipayReq.total_amount = String.valueOf(money);
                alipayReq.subject = "hi"; // 购买hi币
                alipayReq.body = "hi"; // 购买hi币
                alipayReq.out_trade_no = payInfoVo.getOutTradeNo();
                AlipayUtil.pay(this, handler(), MSG_ALIPAY, alipayReq);
            }
            break;
        case MSG_ALIPAY:
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000"))
            {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                //Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
                theApp.showToast("支付成功");
            }
            else
            {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                //Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
                theApp.showToast("支付失败");
            }
            break;
        }
    }
}
