package com.audio.miliao.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.app.library.util.AppChecker;
import com.audio.miliao.util.NotificationUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 通知服务，每隔一段时间弹出通知
 */
public class NotificationService extends Service
{
    /**
     * Return the communication channel to the service.  May return null if
     * clients can not bind to the service.  The returned
     * {@link IBinder} is usually for a complex interface
     * that has been <a href="{@docRoot}guide/components/aidl.html">described using
     * aidl</a>.
     * <p>
     * <p><em>Note that unlike other application components, calls on to the
     * IBinder interface returned here may not happen on the main thread
     * of the process</em>.  More information about the main thread can be found in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html">Processes and
     * Threads</a>.</p>
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    private Timer m_timer = new Timer();
    @Override
    public void onCreate()
    {
        super.onCreate();
        // 只要不在消息界面，隔几秒消息按钮就出现小圆点
        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                // 当app至于后台用户玩其他应用的时候，每隔2分钟推送一下激活用户
                if (!AppChecker.isRunningForeground(getApplicationContext()))
                {
                    NotificationUtil.notify(getApplicationContext());
                }
            }
        };
        // 每隔两分钟弹出notification
        m_timer.schedule(timerTask, 2 * 60 * 1000, 2 * 60 * 1000);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        m_timer.cancel();
    }
}
