package com.audio.miliao;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.audio.miliao.entity.AppData;
import com.audio.miliao.entity.Actor;
import com.audio.miliao.util.UIUtil;
import com.audio.miliao.util.YunXinUtil;

public class theApp extends Application
{
    public static Context CONTEXT = null;
    public static Handler sm_handler = new Handler();

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
            YunXinUtil.init();

            if (AppData.isLogin())
            {
                saveCurUser();
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
                Actor actor = new Actor();
                actor.id = "12345678";
                actor.name = "我是大管家";
                actor.avatar = "avatar1.jpg";
                actor.sign = "管家就是要有管家的样子，别拿管家不当干部";
                actor.intro = "我是大管家，我要管好整个家族";
                actor.age = 23;
                actor.city = "成都";
                actor.fans = 10043;
                actor.follow = 325;
                AppData.saveCurUser(actor);
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
