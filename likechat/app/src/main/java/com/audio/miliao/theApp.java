package com.audio.miliao;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.audio.miliao.entity.AppData;
import com.audio.miliao.receiver.PhoneCallStateObserver;
import com.audio.miliao.util.LogUtil;
import com.audio.miliao.util.YunXinUtil;
import com.netease.nim.uikit.miliao.util.ImageLoaderUtil;
import com.netease.nim.uikit.miliao.util.UIUtil;
import com.netease.nim.uikit.miliao.vo.ActorPageVo;
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
import com.uikit.loader.LoaderApp;
import com.uikit.loader.avchat.AVChatActivity;
import com.uikit.loader.avchat.AVChatProfile;
import com.uikit.loader.entity.LoaderAppData;
import com.uikit.loader.util.sys.SystemUtil;

import java.util.Map;

public class theApp extends Application
{
    public static Context CONTEXT = null;
    public static Handler sm_handler = new Handler();

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        CONTEXT = this;
        LoaderApp.init(this);
//        NIMClient.init(this, YunXinUtil.loginInfo(), YunXinUtil.options(this));
//        // 初始化云信
//        if (inMainProcess(this))
//        {
//            //YunXinUtil.init();
//            NimUIKit.init(this);
//
//            // 会话窗口的定制初始化。
//            SessionHelper.init();
//
//            // 注册通知消息过滤器
//            registerIMMessageFilter();
//
//            // 初始化消息提醒
//            NIMClient.toggleNotification(true);
//
//            // 注册网络通话来电
//            registerAVChatIncomingCallObserver(true);
//        }

        if (AppData.isLogin())
        {
            saveCurUser();

            // Debug
//            LoaderAppData.setCurUserId(30);
//            LoaderAppData.setOpenId("8A59375AF608856146CDC7CD48FE2319");

            //onYunXinLogin("liu1501134", "e10adc3949ba59abbe56e057f20f883e");
            //onYunXinLogin("18178619319", "e10adc3949ba59abbe56e057f20f883e");

            //onYunXinLogin(LoaderAppData.getYunXinAccount(), LoaderAppData.getYunXinToken());
            YunXinUtil.login(LoaderAppData.getYunXinAccount(), LoaderAppData.getYunXinToken());
        }

        ImageLoaderUtil.init(CONTEXT);
    }

    public boolean inMainProcess(Context context)
    {
        String packageName = context.getPackageName();
        String processName = SystemUtil.getProcessName(context);
        return packageName.equals(processName);
    }


    /**
     * 通知消息过滤器（如果过滤则该消息不存储不上报）
     */
    private void registerIMMessageFilter()
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
    private void registerAVChatIncomingCallObserver(boolean register)
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
                    LogUtil.i("Likechat", "reject incoming call data =" + data.toString() + " as local phone is not idle");
                    AVChatManager.getInstance().sendControlCommand(data.getChatId(), AVChatControlCommand.BUSY, null);
                    return;
                }
                // 有网络来电打开AVChatActivity
                AVChatProfile.getInstance().setAVChatting(true);
                AVChatActivity.launch(theApp.CONTEXT, data, AVChatActivity.FROM_BROADCASTRECEIVER);
            }
        }, register);
    }

    public static void saveCurUser()
    {
        try
        {
            if (AppData.getCurUser() == null)
            {
                ActorPageVo actor = new ActorPageVo();
                actor.setId(12345678);
                actor.setNickname("我是大管家");
                actor.setIcon("avatar1.jpg");
                actor.setSignature("管家就是要有管家的样子，别拿管家不当干部");
                actor.setIntroduction("我是大管家，我要管好整个家族");
                actor.setAge("23");
                actor.setProvince("四川");
                actor.setCity("成都");
                actor.setFans("10043");
                actor.setAttention("205");
                AppData.setCurUser(actor);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

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
