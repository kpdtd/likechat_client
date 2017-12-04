package com.audio.miliao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.app.library.util.AppChecker;
import com.app.library.util.Checker;
import com.app.library.util.ImageLoaderUtil;
import com.app.library.vo.GoodsVo;
import com.audio.miliao.R;
import com.audio.miliao.entity.AppData;
import com.audio.miliao.event.AutoSayHelloEvent;
import com.audio.miliao.event.WXPayResultEvent;
import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.cmd.FetchGoods;
import com.audio.miliao.listener.PayListener;
import com.audio.miliao.theApp;
import com.audio.miliao.util.AlipayUtil;
import com.audio.miliao.util.WXUtil;

import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 支付红包界面
 */
public class PayRedPacketActivity extends HandleNotificationActivity
{
    private ImageView m_imgAvatar1;
    private ImageView m_imgAvatar2;
    private ImageView m_imgAvatar3;
    private ImageView m_imgAvatar4;
    private ImageView m_imgAvatar5;
    private ImageView m_imgAvatar6;
    private List<String> m_listAvatarUrl;
    private GoodsVo m_goodsVo;

    public static void show(Activity activity, String[] avatarUrls)
    {
        Intent intent = new Intent(activity, PayRedPacketActivity.class);
        intent.putExtra("avatar_urls", avatarUrls);
        activity.startActivity(intent);
        // 打开activity没有动画，看起来像Dialog一样
        activity.overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_red_packet);
        try
        {
            String[] avatarUrls = (String[]) getIntent().getSerializableExtra("avatar_urls");
            m_listAvatarUrl = Arrays.asList(avatarUrls);

            initUI();
            updateData();

            findViewById(R.id.rdo_alipay).performClick();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
//        super.onBackPressed();
//        // 设置关闭没有动画
//        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    private void initUI()
    {
        try
        {
            m_imgAvatar1 = (ImageView) findViewById(R.id.img_anchor1);
            m_imgAvatar2 = (ImageView) findViewById(R.id.img_anchor2);
            m_imgAvatar3 = (ImageView) findViewById(R.id.img_anchor3);
            m_imgAvatar4 = (ImageView) findViewById(R.id.img_anchor4);
            m_imgAvatar5 = (ImageView) findViewById(R.id.img_anchor5);
            m_imgAvatar6 = (ImageView) findViewById(R.id.img_anchor6);

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    switch (v.getId())
                    {
                    case R.id.img_close:
                        finishWithoutTransition();
                        break;
                    case R.id.btn_pay_red_packet:
                        RadioGroup group = (RadioGroup) findViewById(R.id.rgp_pay_red_packet);
                        if (group.getCheckedRadioButtonId() == R.id.rdo_alipay)
                        {
                            // 支付宝支付
                            AlipayUtil.pay(PayRedPacketActivity.this, "3", m_goodsVo.getId(), m_goodsVo, new PayListener()
                            {
                                @Override
                                public void onSucceed()
                                {
                                    onPaySucceed();
                                }

                                @Override
                                public void onFailed(String error)
                                {
                                    theApp.showToast("支付失败");
                                }
                            });
                        }
                        else
                        {
                            if (!AppChecker.isWechatInstalled(getApplicationContext()))
                            {
                                theApp.showToast(getString(R.string.toast_wx_not_installed));
                                return;
                            }

                            // 微信支付
                            // 微信支付的返回结果需要通过eventbus异步返回，listener返回的结果不准确
                            WXUtil.pay(m_goodsVo);
                        }
                        break;
                    }
                }
            };

            findViewById(R.id.img_close).setOnClickListener(clickListener);
            findViewById(R.id.btn_pay_red_packet).setOnClickListener(clickListener);

            FetchGoods fetchGoods = new FetchGoods(null, FetchGoods.RED_PACKET, null);
            fetchGoods.send(new BaseReqRsp.ReqListener()
            {
                @Override
                public void onSucceed(Object baseReqRsp)
                {
                    FetchGoods fetchGoods1 = (FetchGoods) baseReqRsp;
                    if (Checker.isNotEmpty(fetchGoods1.rspGoodsVoList))
                    {
                        m_goodsVo = fetchGoods1.rspGoodsVoList.get(0);
                    }
                }

                @Override
                public void onError(int errorCode)
                {
                    m_goodsVo = new GoodsVo();
                    m_goodsVo.setId(0);
                    m_goodsVo.setRealPrice(50 * 100);
                }
            });
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
            ImageLoaderUtil.displayListAvatarImage(m_imgAvatar1, getAvatarUrl(0));
            ImageLoaderUtil.displayListAvatarImage(m_imgAvatar2, getAvatarUrl(1));
            ImageLoaderUtil.displayListAvatarImage(m_imgAvatar3, getAvatarUrl(2));
            ImageLoaderUtil.displayListAvatarImage(m_imgAvatar4, getAvatarUrl(3));
            ImageLoaderUtil.displayListAvatarImage(m_imgAvatar5, getAvatarUrl(4));
            ImageLoaderUtil.displayListAvatarImage(m_imgAvatar6, getAvatarUrl(5));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String getAvatarUrl(int index)
    {
        if (m_listAvatarUrl == null || m_listAvatarUrl.size() <= index)
        {
            return "";
        }

        return m_listAvatarUrl.get(index);
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
            theApp.showToast("支付失败");
            break;
        }
    }

    private void onPaySucceed()
    {
        AppData.setPayRedPacketTime(System.currentTimeMillis());
        theApp.showToast("支付成功");
        finishWithoutTransition();
    }

    /**
     * 没有关闭动画
     */
    private void finishWithoutTransition()
    {
        finish();
        // 设置关闭没有动画
        overridePendingTransition(0, 0);
        EventBus.getDefault().post(new AutoSayHelloEvent());
    }
}
