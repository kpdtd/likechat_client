package com.audio.miliao.event;

/**
 * Event基类
 */
public class BaseEvent
{
    private boolean isSucceed = false;

    public void setIsSucceed(boolean succeed)
    {
        isSucceed = succeed;
    }

    public boolean getIsSucceed()
    {
        return isSucceed;
    }
}
