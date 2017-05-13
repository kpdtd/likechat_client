package com.audio.miliao.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.audio.miliao.http.cmd.Login;
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
        theApp.showToast("handleIntent : " + resp.errCode);
        if (resp.errCode == BaseResp.ErrCode.ERR_OK)
        {
            fetchAndLogin(this, resp);
        }
        else if (resp.errCode == BaseResp.ErrCode.ERR_AUTH_DENIED ||
                resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL)
        {
            finish();
        }
    }

    @Override
    public void onReq(BaseReq baseReq)
    {
    }

    @Override
    public void onResp(BaseResp baseResp)
    {
        switch (baseResp.errCode)
        {
        case BaseResp.ErrCode.ERR_OK:
            theApp.showToast("发送成功");
            finish();
            break;
        case BaseResp.ErrCode.ERR_USER_CANCEL:
            theApp.showToast("分享取消");
            finish();
            break;
        case BaseResp.ErrCode.ERR_AUTH_DENIED:
            theApp.showToast("分享被拒绝");
            finish();
            break;
        default:
            theApp.showToast("分享返回");
            break;
        }
    }

    /**
     * 获取用户信息并登录
     */
    private void fetchAndLogin(final Context context, final SendAuth.Resp resp)
    {
        try
        {
            Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    //用户同意
                    StringBuilder sb = new StringBuilder();
                    sb.append("code : " + resp.code + "\n");
                    sb.append("state : " + resp.state + "\n");
                    sb.append("lang : " + resp.lang + "\n");
                    sb.append("country : " + resp.country + "\n");

                    UIUtil.showToastShort(theApp.CONTEXT, sb.toString());

                    WXOauth wxOauth = new WXOauth(null, WXUtil.generateWXOauthURL(resp.code), null);
                    wxOauth.sendSync();
                    if (WXOauth.isSucceed(wxOauth))
                    {
//                WXFetchUserinfo fetchUserinfo = new WXFetchUserinfo(null, wxOauth.rspAccessToken, wxOauth.rspOpenId, null);
//                fetchUserinfo.sendSync();
//
//                if (WXFetchUserinfo.isSucceed(fetchUserinfo))
//                {
//                }
                        Login login = new Login(null, wxOauth.rspOpenId, Login.TYPE_WEIXIN, wxOauth.rspAccessToken, wxOauth.rspRefreshToken, null);
                        login.sendSync();
                        if (Login.isSucceed(login))
                        {
                            setResult(RESULT_OK);
                            finish();
                            return;
                        }
                    }
                }
            };

            new Thread(runnable).start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

