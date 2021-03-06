package com.netease.nim.uikit.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.OnlineStateChangeListener;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.cache.FriendDataCache;
import com.netease.nim.uikit.event.QueryActorVoEvent;
import com.netease.nim.uikit.event.QueryActorVoResultEvent;
import com.netease.nim.uikit.miliao.vo.ActorPageVo;
import com.netease.nim.uikit.model.ToolBarOptions;
import com.netease.nim.uikit.session.SessionCustomization;
import com.netease.nim.uikit.session.constant.Extras;
import com.netease.nim.uikit.session.fragment.MessageFragment;
import com.netease.nim.uikit.uinfo.UserInfoHelper;
import com.netease.nim.uikit.uinfo.UserInfoObservable;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;


/**
 * 点对点聊天界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class P2PMessageActivity extends BaseMessageActivity {

    private boolean isResume = false;
    private TextView mTitleTv;
    private ActorPageVo mActorPageVo;

    public static void start(Context context, String contactId, SessionCustomization customization, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, P2PMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();

        // 单聊特例话数据，包括个人信息，
        requestUserInfo();
        displayOnlineState();
        registerObservers(true);
        registerOnlineStateChangeListener(true);
        EventBus.getDefault().register(this);

        EventBus.getDefault().post(new QueryActorVoEvent(sessionId));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        registerOnlineStateChangeListener(false);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = false;
    }

    /**
     * EventBus 在主线程的响应事件
     *
     * @param event uikit发来的请求查询ActorVo的事件
     */
    public void onEventMainThread(QueryActorVoResultEvent event)
    {
        mActorPageVo = event.getActorPageVo();

        updateData();
    }

    private void initUI()
    {
        mTitleTv = (TextView) findViewById(R.id.txt_title);

        View.OnClickListener clickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (v.getId() == R.id.img_back)
                {
                    finish();
                }
                else if (v.getId() == R.id.img_voice_chat)
                {
                    if (getCustomization() != null && getCustomization().buttons != null)
                    {
                        v.setTag(mActorPageVo);
                        getCustomization().buttons.get(0).onClick(P2PMessageActivity.this, v, sessionId);
                    }
                }
                else if (v.getId() == R.id.img_user_info)
                {
                    try
                    {
                        Intent intent = new Intent("com.audio.miliao.action_user_info");
                        intent.putExtra("sessionId", sessionId);
                        startActivity(intent);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };

        findViewById(R.id.img_back).setOnClickListener(clickListener);
        findViewById(R.id.img_voice_chat).setOnClickListener(clickListener);
        findViewById(R.id.img_user_info).setOnClickListener(clickListener);
    }

    private void requestUserInfo() {
        String strTitle = UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P);
        setTitle(strTitle);
    }

    @Override
    public void setTitle(CharSequence title)
    {
        super.setTitle(title);
        if (mTitleTv != null)
        {
            mTitleTv.setText(title);
        }
    }

    private void registerObservers(boolean register) {
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
    }

    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }
    };

    private UserInfoObservable.UserInfoObserver uinfoObserver;

    OnlineStateChangeListener onlineStateChangeListener = new OnlineStateChangeListener() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            // 更新 toolbar
            if (accounts.contains(sessionId)) {
                // 按照交互来展示
                displayOnlineState();
            }
        }
    };

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKit.enableOnlineState()) {
            return;
        }
        if (register) {
            NimUIKit.addOnlineStateChangeListeners(onlineStateChangeListener);
        } else {
            NimUIKit.removeOnlineStateChangeListeners(onlineStateChangeListener);
        }
    }

    private void displayOnlineState() {
        if (!NimUIKit.enableOnlineState()) {
            return;
        }
        String detailContent = NimUIKit.getOnlineStateContentProvider().getDetailDisplay(sessionId);
        setSubTitle(detailContent);
    }

    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = new UserInfoObservable.UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (accounts.contains(sessionId)) {
                        requestUserInfo();
                    }
                }
            };
        }

        UserInfoHelper.registerObserver(uinfoObserver);
    }

    private void unregisterUserInfoObserver() {
        if (uinfoObserver != null) {
            UserInfoHelper.unregisterObserver(uinfoObserver);
        }
    }

    /**
     * 命令消息接收观察者
     */
    Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
                return;
            }
            showCommandMessage(message);
        }
    };

    protected void showCommandMessage(CustomNotification message) {
        if (!isResume) {
            return;
        }

        String content = message.getContent();
        try {
            JSONObject json = JSON.parseObject(content);
            int id = json.getIntValue("id");
            if (id == 1) {
                // 正在输入
                //Toast.makeText(P2PMessageActivity.this, "对方正在输入...", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(P2PMessageActivity.this, "command: " + content, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }
    }

    @Override
    protected MessageFragment fragment() {
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_message_activity;
    }

    @Override
    protected void initToolBar() {
        ToolBarOptions options = new ToolBarOptions();
        setToolBar(R.id.toolbar, options);
    }

    private void updateData()
    {
        if (mActorPageVo == null)
        {
            return;
        }

        setTitle(mActorPageVo.getNickname());
    }
}
