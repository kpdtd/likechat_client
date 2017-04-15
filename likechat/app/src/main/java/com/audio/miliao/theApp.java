package com.audio.miliao;

import android.app.Application;
import android.content.Context;

import com.audio.miliao.entity.AppData;
import com.audio.miliao.entity.User;

public class theApp extends Application
{
    public static Context CONTEXT = null;

    @Override
    public void onCreate()
    {
        super.onCreate();

        try
        {
            CONTEXT = this;

            if (AppData.isLogin())
            {
                saveCurUser();
            }
        }
        catch (Exception e)
        {
        }
    }

    public static void saveCurUser()
    {
        try
        {
            if (AppData.getCurUser() == null)
            {
                User user = new User();
                user.id = "12345678";
                user.name = "我是大管家";
                user.avatar = "avatar1.jpg";
                user.sign = "管家就是要有管家的样子，别拿管家不当干部";
                user.intro = "我是大管家，我要管好整个家族";
                user.age = 23;
                user.city = "成都";
                user.fans = 10043;
                user.follow = 325;
                AppData.saveCurUser(user);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
