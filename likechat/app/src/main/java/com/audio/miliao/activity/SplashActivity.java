package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;

import com.audio.miliao.R;
import com.audio.miliao.handler.WeakHandler;
import com.audio.miliao.util.Checker;
import com.uikit.loader.entity.LoaderAppData;

public class SplashActivity extends BaseActivity
{
    private WeakHandler m_handler = new WeakHandler(SplashActivity.this);
    /** 完成任务数量 */
    private int m_nCompleteTaskCount = 0;
    /** 任务一：等待1秒 */
    private int TASK_COUNT = 1;

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
                    completeTask();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }

    private void completeTask()
    {
        try
        {
            m_nCompleteTaskCount++;
            if (m_nCompleteTaskCount >= TASK_COUNT)
            {
                //if (AppData.isLogin())
                if (Checker.isNotEmpty(LoaderAppData.getYunXinAccount()))
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
