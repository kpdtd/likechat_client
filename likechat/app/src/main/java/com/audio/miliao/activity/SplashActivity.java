package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;

import com.audio.miliao.R;
import com.audio.miliao.entity.AppData;
import com.audio.miliao.handler.WeakHandler;

public class SplashActivity extends BaseActivity
{
    private WeakHandler m_handler = new WeakHandler(SplashActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        m_handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if (AppData.isLogin())
                    {
                        Intent intentMain = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intentMain);
                    }
                    else
                    {
                        Intent intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intentLogin);
                    }

                    finish();
                }
                catch (Exception e)
                {
                }
            }
        }, 1000);
    }
}
