package com.audio.miliao.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.EditText;

import com.audio.miliao.R;
import com.audio.miliao.activity.BaseActivity;
import com.audio.miliao.entity.UserInfo;
import com.audio.miliao.http.cmd.Login;
import com.audio.miliao.http.cmd.WXFetchUserinfo;
import com.audio.miliao.http.cmd.WXOauth;
import com.audio.miliao.theApp;
import com.audio.miliao.util.WXUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * 接收微信消息
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler
{
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_entrty);
        mEditText = (EditText) findViewById(R.id.edt_log);
        WXUtil.handleIntent(getIntent(), this);
        handleIntent(getIntent());
    }

    @Override
    public void handleMessage(Message msg)
    {
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
            //finish();
        }
    }

    @Override
    public void onReq(BaseReq baseReq)
    {
    }

    @Override
    public void onResp(BaseResp baseResp)
    {
//        switch (baseResp.errCode)
//        {
//        case BaseResp.ErrCode.ERR_OK:
//            theApp.showToast("发送成功");
//            finish();
//            break;
//        case BaseResp.ErrCode.ERR_USER_CANCEL:
//            theApp.showToast("分享取消");
//            finish();
//            break;
//        case BaseResp.ErrCode.ERR_AUTH_DENIED:
//            theApp.showToast("分享被拒绝");
//            finish();
//            break;
//        default:
//            theApp.showToast("分享返回");
//            break;
//        }
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
                    sb.append("openId : " + resp.openId + "\n");
                    theApp.showToast(sb.toString());

                    WXOauth wxOauth = new WXOauth(null, WXUtil.generateWXOauthURL(resp.code), null);
                    wxOauth.sendSync();
                    log(wxOauth.rspResult);

                    if (WXOauth.isSucceed(wxOauth))
                    {
                        WXFetchUserinfo fetchUserinfo = new WXFetchUserinfo(null, wxOauth.rspAccessToken, wxOauth.rspOpenId, null);
                        fetchUserinfo.sendSync();

                        log(fetchUserinfo.rspResult);

                        if (WXFetchUserinfo.isSucceed(fetchUserinfo))
                        {
                            final UserInfo userInfo = new UserInfo();
                            userInfo.openId = wxOauth.rspOpenId;
                            userInfo.accessToken = wxOauth.rspAccessToken;
                            userInfo.refreshToken = wxOauth.rspRefreshToken;
                            userInfo.expiresIn = wxOauth.rspExpiresIn;
                            userInfo.nickname = fetchUserinfo.rspNickname;
                            userInfo.gender = fetchUserinfo.rspGender;
                            userInfo.avatar = fetchUserinfo.rspAvatar;
                            userInfo.province = fetchUserinfo.rspProvince;
                            userInfo.city = fetchUserinfo.rspCity;
                            userInfo.type = "weixin";
                            Login login = new Login(null, userInfo, null);
                            login.sendSync();
                            if (Login.isSucceed(login))
                            {
                                setResult(RESULT_OK);
                                finish();
                                return;
                            }

                            log(userInfo.toJsonString());
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

    private void log(final String strLog)
    {
        handler().post(new Runnable()
        {
            @Override
            public void run()
            {
                mEditText.setText(mEditText.getText().toString() + "\n" + strLog);
            }
        });
    }
}

