package com.likechat.likechat;

import android.app.Application;
import android.content.Context;

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
        }
        catch (Exception e)
        {
        }
    }
}
