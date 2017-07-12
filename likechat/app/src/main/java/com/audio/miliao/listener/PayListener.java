package com.audio.miliao.listener;

/**
 * 支付进度监听器
 */
public interface PayListener
{
    public void onSucceed();
    public void onFailed(String error);
}
