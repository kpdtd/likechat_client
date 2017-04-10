package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.audio.miliao.R;
import com.audio.miliao.entity.AppData;
import com.audio.miliao.theApp;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
    }

    private void initUI()
    {
        try
        {
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
                            onLoginSucceed();
                            break;
                        case R.id.txt_qq_login:
                            onLoginSucceed();
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onLoginSucceed()
    {
        try
        {
            AppData.saveIsLogin(true);
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
