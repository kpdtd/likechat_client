package com.uikit.loader;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatControlCommand;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.model.IMMessageFilter;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;
import com.uikit.loader.avchat.AVChatActivity;
import com.uikit.loader.avchat.AVChatProfile;
import com.uikit.loader.avchat.receiver.PhoneCallStateObserver;
import com.uikit.loader.entity.Account;
import com.uikit.loader.session.SessionHelper;
import com.uikit.loader.util.UIUtil;
import com.uikit.loader.util.YunXinUtil;
import com.uikit.loader.util.sys.SystemUtil;

import java.util.Map;

/**
 * Created by liujiye on 17/5/6.
 */
public class LoaderApp extends Application
{
    private static final String TAG = "YunDemo App";

    public static Context CONTEXT;
    // likechat账号
    //public static final Account TEST3 = new Account("test003", "1c641f3af395c4734afe3786ba818d63");
    //public static final Account TEST4 = new Account("test004", "e4c34b0e582ae5f59e1d417cb87a824f");
    // 云信账号, token使用123456的MD5值
    public static final Account TEST3 = new Account("liu1501134", "e10adc3949ba59abbe56e057f20f883e");
    public static final Account TEST4 = new Account("18178619319", "e10adc3949ba59abbe56e057f20f883e");

    private static Account mCurAccount = TEST3;

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    /**
     * 初始化<br/>
     * 在主线程调用
     */
    public static void init(Context context)
    {
        CONTEXT = context;
        NIMClient.init(context, YunXinUtil.loginInfo(), YunXinUtil.options(context));
        if (inMainProcess(context))
        {
            YunXinUtil.init();
            NimUIKit.init(context);

            // 会话窗口的定制初始化。
            SessionHelper.init();

            // 注册通知消息过滤器
            registerIMMessageFilter();

            // 初始化消息提醒
            NIMClient.toggleNotification(true);

            // 注册网络通话来电
            registerAVChatIncomingCallObserver(true);
        }
    }

    public static Account getCurAccount()
    {
        return mCurAccount;
    }

    public static void setCurAccount(Account account)
    {
        mCurAccount = account;
    }

    public static boolean inMainProcess(Context context)
    {
        if (context == null)
        {
            return false;
        }
        String packageName = context.getPackageName();
        String processName = SystemUtil.getProcessName(context);
        return packageName.equals(processName);
    }

    /**
     * 通知消息过滤器（如果过滤则该消息不存储不上报）
     */
    private static void registerIMMessageFilter()
    {
        NIMClient.getService(MsgService.class).registerIMMessageFilter(new IMMessageFilter()
        {
            @Override
            public boolean shouldIgnore(IMMessage message)
            {
                if (message.getAttachment() != null)
                {
                    if (message.getAttachment() instanceof UpdateTeamAttachment)
                    {
                        UpdateTeamAttachment attachment = (UpdateTeamAttachment) message.getAttachment();
                        for (Map.Entry<TeamFieldEnum, Object> field : attachment.getUpdatedFields().entrySet())
                        {
                            if (field.getKey() == TeamFieldEnum.ICON)
                            {
                                return true;
                            }
                        }
                    }
                    else if (message.getAttachment() instanceof AVChatAttachment)
                    {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * 注册网络通话来电
     *
     * @param register
     */
    private static void registerAVChatIncomingCallObserver(boolean register)
    {
        AVChatManager.getInstance().observeIncomingCall(new Observer<AVChatData>()
        {
            @Override
            public void onEvent(AVChatData data)
            {
                String extra = data.getExtra();
                Log.e("Extra", "Extra Message->" + extra);
                if (PhoneCallStateObserver.getInstance().getPhoneCallState() != PhoneCallStateObserver.PhoneCallStateEnum.IDLE
                        || AVChatProfile.getInstance().isAVChatting()
                        || AVChatManager.getInstance().getCurrentChatId() != 0)
                {
                    LogUtil.i(TAG, "reject incoming call data =" + data.toString() + " as local phone is not idle");
                    AVChatManager.getInstance().sendControlCommand(data.getChatId(), AVChatControlCommand.BUSY, null);
                    return;
                }
                // 有网络来电打开AVChatActivity
                AVChatProfile.getInstance().setAVChatting(true);
                AVChatActivity.launch(LoaderApp.CONTEXT, data, AVChatActivity.FROM_BROADCASTRECEIVER);
            }
        }, register);
    }


    public static Handler sm_handler = new Handler();

    public static void showToast(final String strToast)
    {
        sm_handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                UIUtil.showToastLong(CONTEXT, strToast);
            }
        });
    }
}
