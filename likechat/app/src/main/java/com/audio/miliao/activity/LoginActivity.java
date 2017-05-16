package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.audio.miliao.R;
import com.audio.miliao.entity.UserInfo;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.Login;
import com.audio.miliao.theApp;
import com.audio.miliao.util.QQUtil;
import com.audio.miliao.util.WXUtil;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.uikit.loader.LoaderApp;
import com.uikit.loader.entity.LoaderAppData;
import com.uikit.loader.service.YXService;


/**
 * 登录
 */
public class LoginActivity extends BaseActivity
{
    private static final int CODE_QQ_LOGIN = 0;
    private static final int CODE_WEIXIN_LOGIN = 1;

    private EditText mEdittext;
    private YXService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();

        mService = new YXService(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        QQUtil.onActivityResult(requestCode, resultCode, data);
        theApp.showToast("onActivityResult requestCode:" + requestCode + ";resultCode:" + resultCode);
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        case CODE_QQ_LOGIN:
            UserInfo userInfo = (UserInfo) msg.obj;
            mEdittext.setText(userInfo.toJsonString());
            Login login = new Login(handler(), userInfo, null);
            login.send();
            break;
        case CODE_WEIXIN_LOGIN:
            break;
        case HttpUtil.RequestCode.LOGIN:
            Login login1 = (Login) msg.obj;
            if (Login.isSucceed(login1))
            {
                theApp.showToast("login secceed");
                onLoginSucceed();
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
        mService.login(LoaderApp.getCurAccount(), new RequestCallback<LoginInfo>()
        {
            @Override
            public void onSuccess(LoginInfo loginInfo)
            {
                theApp.showToast("YunXin onSuccess");
                // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                NimUIKit.setAccount(loginInfo.getAccount());
                LoaderAppData.setYunXinAccount(loginInfo.getAccount());
                LoaderAppData.setYunXinToken(loginInfo.getToken());

                String strAccount;
                if (loginInfo.getAccount().equals(LoaderApp.TEST3.getAccount()))
                {
                    strAccount = LoaderApp.TEST4.getAccount();
                }
                else
                {
                    strAccount = LoaderApp.TEST3.getAccount();
                }
                mService.chat(strAccount);
            }

            @Override
            public void onFailed(int i)
            {
                theApp.showToast("YunXin onFailed");
            }

            @Override
            public void onException(Throwable throwable)
            {
                theApp.showToast("YunXin onException");
            }
        });
    }

    private void onLoginSucceed()
    {
        try
        {
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
