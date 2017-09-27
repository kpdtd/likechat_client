package com.audio.miliao.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.app.library.R;
import com.audio.miliao.activity.MainActivity;


/**
 * 生成通知
 */
public class NotificationUtil
{
    public static void notify(Context context)
    {
        //创建一个NotificationManager的引用
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);

        /**
        * 添加声音
        * notification.defaults |=Notification.DEFAULT_SOUND;
        * 或者使用以下几种方式
        * notification.sound = Uri.parse("file:///sdcard/notification/ringer.mp3");
        * notification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
        * 如果想要让声音持续重复直到用户对通知做出反应，则可以在notification的flags字段增加"FLAG_INSISTENT"
        * 如果notification的defaults字段包括了"DEFAULT_SOUND"属性，则这个属性将覆盖sound字段中定义的声音
        */

        /**
        * 添加振动
        * notification.defaults |= Notification.DEFAULT_VIBRATE;
        * 或者可以定义自己的振动模式：
        * long[] vibrate = {0,100,200,300}; //0毫秒后开始振动，振动100毫秒后停止，再过200毫秒后再次振动300毫秒
        * notification.vibrate = vibrate;
        * long数组可以定义成想要的任何长度
        * 如果notification的defaults字段包括了"DEFAULT_VIBRATE",则这个属性将覆盖vibrate字段中定义的振动
        */

        /**
        * 添加LED灯提醒
        * notification.defaults |= Notification.DEFAULT_LIGHTS;
        * 或者可以自己的LED提醒模式:
        * notification.ledARGB = 0xff00ff00;
        * notification.ledOnMS = 300; //亮的时间
        * notification.ledOffMS = 1000; //灭的时间
        * notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        */

        /**
        * 更多的特征属性
        * notification.flags |= FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
        * notification.flags |= FLAG_INSISTENT; //重复发出声音，直到用户响应此通知
        * notification.flags |= FLAG_ONGOING_EVENT; //将此通知放到通知栏的"Ongoing"即"正在运行"组中
        * notification.flags |= FLAG_NO_CLEAR; //表明在点击了通知栏中的"清除通知"后，此通知不清除，
        * //经常与FLAG_ONGOING_EVENT一起使用
        * notification.number = 1; //number字段表示此通知代表的当前事件数量，它将覆盖在状态栏图标的顶部
        * //如果要使用此字段，必须从1开始
        * notification.iconLevel = ; //
        */

        //设置通知的事件消息
        //点击该通知后要跳转的Activity
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("come_from", "notification");
        notificationIntent.setData(Uri.parse("custom://"+System.currentTimeMillis()));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Notification.Builder builder = new Notification.Builder(context)
                .setAutoCancel(true)
                .setContentTitle("title") // 通知栏标题
                .setContentText("content text") // 通知栏内容
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis());
        Notification notification = builder.build();

        //把Notification传递给 NotificationManager
        mNotificationManager.notify(0, notification);
    }
}
