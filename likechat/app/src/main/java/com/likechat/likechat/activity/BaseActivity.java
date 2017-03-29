package com.likechat.likechat.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.likechat.likechat.handler.WeakHandler;

public class BaseActivity extends AppCompatActivity implements WeakHandler.MessageHandler
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    /**
     * 子类重写该函数来处理handler消息
     *
     * @param msg
     */
    public void handleMessage(Message msg)
    {

    }
}
