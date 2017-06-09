package com.audio.miliao;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.audio.miliao.entity.AppData;
import com.audio.miliao.util.UIUtil;
import com.audio.miliao.vo.ActorPageVo;
import com.uikit.loader.LoaderApp;

public class theApp extends Application
{
    public static Context CONTEXT = null;
    public static Handler sm_handler = new Handler();

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        try
        {
            CONTEXT = this;
            init(this);
        }
        catch (Exception e)
        {
        }
    }

    public static void init(Context context)
    {
        try
        {
            //YunXinUtil.init();
            LoaderApp.init(context);

            if (AppData.isLogin())
            {
                saveCurUser();
                // Debug
                AppData.setCurUserId(30);
                AppData.setOpenId("8A59375AF608856146CDC7CD48FE2319");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void saveCurUser()
    {
        try
        {
            if (AppData.getCurUser() == null)
            {
                ActorPageVo actor = new ActorPageVo();
                actor.setId(12345678);
                actor.setNickname("我是大管家");
                actor.setIcon("avatar1.jpg");
                actor.setSignature("管家就是要有管家的样子，别拿管家不当干部");
                actor.setIntroduction("我是大管家，我要管好整个家族");
                actor.setAge("23");
                actor.setProvince("四川");
                actor.setCity("成都");
                actor.setFans("10043");
                actor.setAttention("205");
                AppData.setCurUser(actor);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void showToast(final String strToast)
    {
        sm_handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                UIUtil.showToastLong(CONTEXT, strToast);
            }
        });
    }
}
