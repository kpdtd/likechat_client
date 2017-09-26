package com.audio.miliao.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.app.library.util.LogUtil;
import com.audio.miliao.handler.WeakHandler;

public class BaseFragment extends Fragment implements WeakHandler.MessageHandler
{
    private WeakHandler mHandler = new WeakHandler(this);

    @Override
    public void handleMessage(Message msg)
    {

    }

    public Handler handler()
    {
        return mHandler;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        log("onViewCreated");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        log("onResume");
    }

    @Override
    public void onStop()
    {
        super.onStop();
        log("onStop");
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        log("onDestroyView");
    }

    public void log(String strLog)
    {
        try
        {
            LogUtil.d(getClass().getSimpleName() + " : " + strLog);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
