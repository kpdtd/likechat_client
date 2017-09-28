package com.audio.miliao.activity;

import android.os.Bundle;

import com.audio.miliao.event.ShowMessageListEvent;

import de.greenrobot.event.EventBus;

/**
 * 处理点击通知查看消息列表的消息
 */
public class HandleNotificationActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * EventBus 在主线程的响应事件
     *
     * @param event 点击通知显示消息列表
     */
    public void onEventMainThread(ShowMessageListEvent event)
    {
        finish();
    }
}
