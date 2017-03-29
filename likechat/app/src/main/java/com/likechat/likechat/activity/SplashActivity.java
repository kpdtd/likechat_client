package com.likechat.likechat.activity;

import android.content.Intent;
import android.os.Bundle;

import com.likechat.likechat.R;
import com.likechat.likechat.handler.WeakHandler;

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
                    Intent intentMain = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intentMain);

                    finish();
                }
                catch (Exception e)
                {
                }
            }
        }, 1000);
    }
}
