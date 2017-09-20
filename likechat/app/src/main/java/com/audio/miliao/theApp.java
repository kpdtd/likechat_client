package com.audio.miliao;

import android.accounts.Account;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.app.library.util.ImageLoaderUtil;
import com.app.library.util.PreferUtil;
import com.app.library.util.UIUtil;
import com.app.library.vo.ActorPageVo;
import com.audio.miliao.entity.AppData;

public class theApp extends Application
{
    public static Context CONTEXT = null;
    public static Handler sm_handler = new Handler();

    public static final Account TEST3 = new Account("liu1501134", "e10adc3949ba59abbe56e057f20f883e");
    public static final Account TEST4 = new Account("18178619319", "e10adc3949ba59abbe56e057f20f883e");

    private static Account mCurAccount = TEST3;

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
        initUtil();
        //LoaderApp.init(this);
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
            //YunXinUtil.login(AppData.getYunXinAccount(), AppData.getYunXinToken());
        }

    }

    private void initUtil()
    {
        PreferUtil.init(CONTEXT);
        ImageLoaderUtil.init(CONTEXT);
    }

//    public boolean inMainProcess(Context context)
//    {
//        String packageName = context.getPackageName();
//        String processName = SystemUtil.getProcessName(context);
//        return packageName.equals(processName);
//    }


//    /**
//     * 通知消息过滤器（如果过滤则该消息不存储不上报）
//     */
//    private void registerIMMessageFilter()
//    {
//        NIMClient.getService(MsgService.class).registerIMMessageFilter(new IMMessageFilter()
//        {
//            @Override
//            public boolean shouldIgnore(IMMessage message)
//            {
//                if (message.getAttachment() != null)
//                {
//                    if (message.getAttachment() instanceof UpdateTeamAttachment)
//                    {
//                        UpdateTeamAttachment attachment = (UpdateTeamAttachment) message.getAttachment();
//                        for (Map.Entry<TeamFieldEnum, Object> field : attachment.getUpdatedFields().entrySet())
//                        {
//                            if (field.getKey() == TeamFieldEnum.ICON)
//                            {
//                                return true;
//                            }
//                        }
//                    }
//                    else if (message.getAttachment() instanceof AVChatAttachment)
//                    {
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
//    }

//    /**
//     * 注册网络通话来电
//     *
//     * @param register
//     */
////    private void registerAVChatIncomingCallObserver(boolean register)
////    {
////        AVChatManager.getInstance().observeIncomingCall(new Observer<AVChatData>()
////        {
////            @Override
////            public void onEvent(AVChatData data)
////            {
////                String extra = data.getExtra();
////                Log.e("Extra", "Extra Message->" + extra);
////                if (PhoneCallStateObserver.getInstance().getPhoneCallState() != PhoneCallStateObserver.PhoneCallStateEnum.IDLE
////                        || AVChatProfile.getInstance().isAVChatting()
////                        || AVChatManager.getInstance().getCurrentChatId() != 0)
////                {
////                    LogUtil.i("Likechat", "reject incoming call data =" + data.toString() + " as local phone is not idle");
////                    AVChatManager.getInstance().sendControlCommand(data.getChatId(), AVChatControlCommand.BUSY, null);
////                    return;
////                }
////                // 有网络来电打开AVChatActivity
////                AVChatProfile.getInstance().setAVChatting(true);
////                AVChatActivity.launch(theApp.CONTEXT, data, AVChatActivity.FROM_BROADCASTRECEIVER);
////            }
////        }, register);
////    }

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
    public static Account getCurAccount()
    {
        return mCurAccount;
    }

    public static void setCurAccount(Account account)
    {
        mCurAccount = account;
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
