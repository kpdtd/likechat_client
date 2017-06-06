package com.audio.miliao.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

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
}
