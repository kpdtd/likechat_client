package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.audio.miliao.R;
import com.audio.miliao.entity.AppData;
import com.audio.miliao.entity.UserInfo;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.Login;
import com.audio.miliao.theApp;
import com.audio.miliao.util.MD5;
import com.audio.miliao.util.QQUtil;
import com.audio.miliao.util.WXUtil;
import com.audio.miliao.util.YunXinUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;


/**
 * 登录
 */
public class LoginActivity extends BaseActivity
{
    private static final int CODE_QQ_LOGIN = 0;
    private static final int CODE_QQ_FETCH_USERINFO = 1;

    private EditText mEdittext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        QQUtil.onActivityResult(requestCode, resultCode, data);
        theApp.showToast("onActivityResult " + resultCode);
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        case CODE_QQ_LOGIN:
            UserInfo userInfo = (UserInfo) msg.obj;
            mEdittext.setText(userInfo.toJsonString());
            Login login = new Login(handler(), Login.TYPE_QQ, userInfo, null);
            login.send();
            break;
        case HttpUtil.RequestCode.LOGIN:
            Login login1 = (Login) msg.obj;
            if (Login.isSucceed(login1))
            {
                theApp.showToast("login secceed");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                theApp.showToast("login failed");
            }
            break;
        }
    }

    private void initUI()
    {
        try
        {
            mEdittext = (EditText) findViewById(R.id.txt_view);

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        switch (v.getId())
                        {
                        case R.id.txt_weixin_login:
                            onWXLogin();
                            break;
                        case R.id.txt_qq_login:
                            onQQLogin();
                            break;
                        case R.id.btn_yunxin_login:
                            onYunXinLogin();
                            break;
                        case R.id.img_back:
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

            findViewById(R.id.txt_weixin_login).setOnClickListener(clickListener);
            findViewById(R.id.txt_qq_login).setOnClickListener(clickListener);
            findViewById(R.id.btn_yunxin_login).setOnClickListener(clickListener);
            findViewById(R.id.img_back).setOnClickListener(clickListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onWXLogin()
    {
        WXUtil.login();
    }

    private void onQQLogin()
    {
        QQUtil.login(this, CODE_QQ_LOGIN, handler());
    }

    private void onYunXinLogin()
    {
        try
        {
            String strToken = MD5.getStringMD5("123456");
            LoginInfo info = new LoginInfo("liu1501134", strToken, YunXinUtil.APP_KEY); // config...
            RequestCallback<LoginInfo> callback =
                    new RequestCallback<LoginInfo>()
                    {
                        @Override
                        public void onSuccess(LoginInfo loginInfo)
                        {
                            theApp.showToast("onSuccess");

                            // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                            AppData.setYunXinAccount(loginInfo.getAccount());
                            AppData.setYunXinToken(loginInfo.getToken());
                        }

                        @Override
                        public void onFailed(int i)
                        {
                            theApp.showToast("onFailed " + i);
                        }

                        @Override
                        public void onException(Throwable throwable)
                        {
                            theApp.showToast("onException " + throwable.toString());
                        }
                    };
            NIMClient.getService(AuthService.class).login(info).setCallback(callback);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            theApp.showToast("Exception " + e.toString());
        }
    }

    private void onLoginSucceed()
    {
        try
        {
            theApp.saveCurUser();

            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);

            finish();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
