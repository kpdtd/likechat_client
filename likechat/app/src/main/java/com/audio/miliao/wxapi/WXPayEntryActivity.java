package com.audio.miliao.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.audio.miliao.activity.BaseActivity;
import com.audio.miliao.event.WXPayResultEvent;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import de.greenrobot.event.EventBus;


/**
 * 微信支付回调activity
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent)
    {
        SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
        EventBus.getDefault().postSticky(new WXPayResultEvent(resp.errCode));
//        if (resp.errCode == BaseResp.ErrCode.ERR_OK)
//        {
//            // 支付成功了
//            finish();
//        }
//        else
//        {
//        }

        finish();
    }

    @Override
    public void onReq(BaseReq baseReq)
    {
    }

    @Override
    public void onResp(BaseResp baseResp)
    {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX)
        {
            finish();
        }
    }
}
