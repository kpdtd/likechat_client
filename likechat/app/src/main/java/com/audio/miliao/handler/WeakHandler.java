package com.audio.miliao.handler;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 消息处理器
 * <p>
 * 使用WeakReference 防止内存无法被释放
 */
public class WeakHandler extends Handler
{
	WeakReference<MessageHandler> m_handler;

	public WeakHandler(final MessageHandler activity)
	{
		m_handler = new WeakReference<MessageHandler>(activity);
	}

	@Override
	public void handleMessage(Message msg)
	{
		super.handleMessage(msg);
		try
		{
			MessageHandler handler = m_handler.get();
			if (handler != null)
			{
				handler.handleMessage(msg);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 处理消息回调
	 */
	public static interface MessageHandler
	{
		public void handleMessage(Message msg);
	}
}
