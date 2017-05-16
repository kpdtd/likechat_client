package com.uikit.loader.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.session.SessionCustomization;
import com.netease.nim.uikit.session.actions.BaseAction;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.uikit.loader.LoaderApp;
import com.uikit.loader.entity.Account;
import com.uikit.loader.entity.LoaderAppData;
import com.uikit.loader.session.action.AVChatAction;
import com.uikit.loader.session.action.FileAction;
import com.uikit.loader.session.action.GuessAction;
import com.uikit.loader.session.action.SnapChatAction;
import com.uikit.loader.util.YunXinUtil;

import java.util.ArrayList;

/**
 * 云信操作入口
 */
public class YXService
{
    private Context mContext;

    public YXService(Context context)
    {
        mContext = context;
    }

    /**
     *
     * @param account 对方的id
     */
    public void chat(String account)
    {
        try
        {
            //启动单聊界面
            NimUIKit.setAccount(LoaderAppData.getYunXinAccount());
            SessionCustomization customization = customP2PChatOptions();
            //NimUIKit.startP2PSession(this, "test003");
            // 启动单聊
            NimUIKit.startChatting(mContext, account, SessionTypeEnum.P2P, customization, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void login(Account account, RequestCallback<LoginInfo> callback)
    {
        try
        {
            // String strToken = MD5.getStringMD5("123456");
            String strAccount = account.getAccount();
            String strToken = account.getToken();
            LoginInfo info = new LoginInfo(strAccount, strToken, YunXinUtil.APP_KEY); // config...
//            RequestCallback<LoginInfo> callback =
//                    new RequestCallback<LoginInfo>()
//                    {
//                        @Override
//                        public void onSuccess(LoginInfo loginInfo)
//                        {
//                            LoaderApp.showToast("onSuccess");
//
//                            // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
//                            NimUIKit.setAccount(loginInfo.getAccount());
//                            AppData.setYunXinAccount(loginInfo.getAccount());
//                            AppData.setYunXinToken(loginInfo.getToken());
//
//                            //onLoginResult(true);
//                        }
//
//                        @Override
//                        public void onFailed(int i)
//                        {
//                            LoaderApp.showToast("onFailed " + i);
//                        }
//
//                        @Override
//                        public void onException(Throwable throwable)
//                        {
//                            LoaderApp.showToast("onException " + throwable.toString());
//                        }
//                    };
            NIMClient.getService(AuthService.class).login(info).setCallback(callback);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LoaderApp.showToast("Exception " + e.toString());
        }
    }

    private SessionCustomization customP2PChatOptions()
    {
        // 设置单聊界面定制
        SessionCustomization sessionCustomization = getP2pCustomization();
        NimUIKit.setCommonP2PSessionCustomization(sessionCustomization);
        return sessionCustomization;
    }

    private static SessionCustomization getP2pCustomization()
    {
        SessionCustomization sessionCustomization = null;
        if (sessionCustomization == null)
        {
            sessionCustomization = initBaseP2P();

            // 定制ActionBar右边的按钮，可以加多个
            ArrayList<SessionCustomization.OptionsButton> buttons = new ArrayList<>();
//            OptionsButton cloudMsgButton = new OptionsButton() {
//                @Override
//                public void onClick(Context context, View view, String sessionId)
//                {
//                    //MessageHistoryActivity.start(context, sessionId, SessionTypeEnum.P2P); // 漫游消息查询
//                }
//            };
//            cloudMsgButton.iconId = R.drawable.nim_ic_messge_history;
//
//            buttons.add(cloudMsgButton);
            sessionCustomization.buttons = buttons;
        }
        return sessionCustomization;
    }

    private static SessionCustomization initBaseP2P()
    {
        SessionCustomization sessionCustomization = new SessionCustomization()
        {
            // 由于需要Activity Result， 所以重载该函数。
            @Override
            public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data)
            {
//                if (requestCode == NormalTeamInfoActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK)
//                {
//                    String result = data.getStringExtra(NormalTeamInfoActivity.RESULT_EXTRA_REASON);
//                    if (result == null)
//                    {
//                        return;
//                    }
//                    if (result.equals(NormalTeamInfoActivity.RESULT_EXTRA_REASON_CREATE))
//                    {
//                        String tid = data.getStringExtra(NormalTeamInfoActivity.RESULT_EXTRA_DATA);
//                        if (TextUtils.isEmpty(tid))
//                        {
//                            return;
//                        }
//
//                        startTeamSession(activity, tid);
//                        activity.finish();
//                    }
//                }
            }

            @Override
            public MsgAttachment createStickerAttachment(String category, String item)
            {
                //return new StickerAttachment(category, item);
                return null;
            }
        };

        // 背景
        sessionCustomization.backgroundColor = Color.BLUE;
        sessionCustomization.backgroundUri = "file:///android_asset/xx/bk.jpg";
        sessionCustomization.backgroundUri = "file:///sdcard/Pictures/bk.png";
        sessionCustomization.backgroundUri = "android.resource://com.netease.nim.demo/drawable/bk";

        // 定制加号点开后可以包含的操作，默认已经有图片，视频等消息了，如果要去掉默认的操作，请修改MessageFragment的getActionList函数
        ArrayList<BaseAction> actions = new ArrayList<>();
        actions.add(new AVChatAction(AVChatType.AUDIO));
        actions.add(new AVChatAction(AVChatType.VIDEO));
        actions.add(new SnapChatAction());
        actions.add(new GuessAction());
        actions.add(new FileAction());
        sessionCustomization.actions = actions;
        sessionCustomization.withSticker = true;

        return sessionCustomization;
    }
}
