package com.audio.miliao.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.audio.miliao.http.cmd.WXOauth;
import com.audio.miliao.theApp;
import com.audio.miliao.util.UIUtil;
import com.audio.miliao.util.WXUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * 接收微信消息
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
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
        if (resp.errCode == BaseResp.ErrCode.ERR_OK)
        {
            //用户同意
            StringBuilder sb = new StringBuilder();
            sb.append("code : " + resp.code + "\n");
            sb.append("state : " + resp.state + "\n");
            sb.append("lang : " + resp.lang + "\n");
            sb.append("country : " + resp.country + "\n");

            UIUtil.showToastShort(theApp.CONTEXT, sb.toString());

            WXOauth wxOauth = new WXOauth(null, WXUtil.generateWXOauthURL(resp.code), null);
            wxOauth.send();
        }
    }

    @Override
    public void onReq(BaseReq baseReq)
    {
    }

    @Override
    public void onResp(BaseResp baseResp)
    {
        switch (baseResp.errCode) {
        case BaseResp.ErrCode.ERR_OK:
            Toast.makeText(this, "发送成功", Toast.LENGTH_LONG).show();
            finish();
            break;
        case BaseResp.ErrCode.ERR_USER_CANCEL:
            Toast.makeText(this, "分享取消", Toast.LENGTH_LONG).show();
            finish();
            break;
        case BaseResp.ErrCode.ERR_AUTH_DENIED:
            Toast.makeText(this, "分享被拒绝", Toast.LENGTH_LONG).show();
            finish();
            break;
        default:
            Toast.makeText(this, "分享返回", Toast.LENGTH_LONG).show();
            break;
        }
    }
}

