package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;

import com.app.library.util.Checker;
import com.app.library.vo.UserRegisterVo;
import com.audio.miliao.R;
import com.audio.miliao.entity.AppData;
import com.audio.miliao.handler.WeakHandler;
import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.cmd.RegisterAndLogin;
import com.audio.miliao.theApp;

public class SplashActivity extends BaseActivity
{
    private WeakHandler m_handler = new WeakHandler(SplashActivity.this);
    /** 完成任务数量 */
    private int m_nCompleteTaskCount = 0;
    /**
     * 任务一：等待1秒<br/>
     * 任务二：客户端生成openID自动注册登录(可选)
     * */
    private int TASK_COUNT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Checker.isEmpty(AppData.getOpenId()))
        {
            TASK_COUNT = 2;
            autoRegisterAndLogin();
        }

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
//                if (Checker.isNotEmpty(AppData.getYunXinAccount()))
//                {
//                    Intent intentMain = new Intent(SplashActivity.this, MainActivity.class);
//                    startActivity(intentMain);
//                }
//                else
//                {
//                    Intent intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(intentLogin);
//                }

                Intent intentMain = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intentMain);
                finish();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 自动生成openId并且注册登录
     */
    private void autoRegisterAndLogin()
    {
        String openId = theApp.getUniqueID();
        UserRegisterVo userInfoVo = new UserRegisterVo();
        userInfoVo.setOpenId(openId);
        userInfoVo.setCity("");
        userInfoVo.setIcon("");
        userInfoVo.setNickname("");
        userInfoVo.setProvince("");
        userInfoVo.setSex("");
        userInfoVo.setLogin_type("auto");
        RegisterAndLogin registerAndLogin = new RegisterAndLogin(null, userInfoVo, null);
        registerAndLogin.send(new BaseReqRsp.ReqListener()
        {
            @Override
            public void onSucceed(Object baseReqRsp)
            {
                completeTask();
            }

            @Override
            public void onError(int errorCode)
            {
                theApp.showToast("errorCode:" + errorCode);
            }
        });
    }
}
