package com.audio.miliao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.app.library.util.Checker;
import com.app.library.util.ImageLoaderUtil;
import com.app.library.util.RandomUtil;
import com.app.library.vo.ActorPageVo;
import com.app.library.vo.ChatMsg;
import com.app.library.vo.MessageVo;
import com.audio.miliao.R;
import com.audio.miliao.adapter.ChatAdapter;
import com.audio.miliao.entity.AppData;
import com.audio.miliao.event.BuyVipResultEvent;
import com.audio.miliao.http.BaseReqRsp;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.FetchAccountBalance;
import com.audio.miliao.http.cmd.FetchMessage;
import com.audio.miliao.http.cmd.FetchVipMember;
import com.audio.miliao.util.DBUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天——文字
 */
public class ChatTextActivity extends BaseActivity
{
    private ActorPageVo m_actorPageVo;
    private ListView m_list;
    private ChatAdapter m_adapter;
    private EditText m_edtMessage;
    private MessageVo m_messageVo;
    private boolean m_isVip = false;

    private List<ChatMsg> m_chatMessages = new ArrayList<>();

    public static void show(Activity activity, ActorPageVo actorPageVo)
    {
        Intent intent = new Intent(activity, ChatTextActivity.class);
        intent.putExtra("actor_page_vo", actorPageVo);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_text);
        try
        {
            m_actorPageVo = (ActorPageVo) getIntent().getSerializableExtra("actor_page_vo");
            m_messageVo = DBUtil.queryMessageVoByActorId(m_actorPageVo.getId());
            m_chatMessages = DBUtil.queryChatMessageListByActorId(m_actorPageVo.getId());

            initUI();
            updateData();

            checkVip(new BaseReqRsp.ReqListenerImpl()
            {
                @Override
                public void onSucceed(Object baseReqRsp)
                {
                    FetchVipMember fetchVipMember = (FetchVipMember) baseReqRsp;
                    m_isVip = fetchVipMember.isVip();
                    if (!m_isVip)
                    {
                        // 如果用户没有发送过消息，进入聊天后就不主动取消息
                        // 发送过消息后，进入聊天就主动获取
                        if (Checker.isNotEmpty(m_chatMessages))
                        {
                            FetchMessage fetchMessage = new FetchMessage(handler(), m_actorPageVo.getId(), null);
                            fetchMessage.send();
                        }
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initUI()
    {
        try
        {
            m_list = (ListView) findViewById(R.id.list);
            m_edtMessage = (EditText) findViewById(R.id.edt_message);

            m_list.setOnScrollListener(ImageLoaderUtil.getPauseListener());

            View.OnClickListener clickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        switch (v.getId())
                        {
                        case R.id.img_back:
                            finish();
                            break;
                        case R.id.img_user_info:
                            Intent intentUserInfo = new Intent(ChatTextActivity.this, UserInfoActivity.class);
                            intentUserInfo.putExtra("actor_page", m_actorPageVo);
                            startActivity(intentUserInfo);
                            break;
                        case R.id.img_phone:
                            FetchAccountBalance fetchAccountBalance = new FetchAccountBalance(handler(), null);
                            fetchAccountBalance.send();
                            break;
                        case R.id.img_send:
                            // 发送消息时先获取当前用户是否VIP
//                            FetchVipMember fetchVipMember = new FetchVipMember(handler(), "send_message");
//                            fetchVipMember.send();
                            checkVip(new BaseReqRsp.ReqListenerImpl()
                            {
                                @Override
                                public void onSucceed(Object baseReqRsp)
                                {
                                    FetchVipMember fetchVipMember = (FetchVipMember) baseReqRsp;
                                    m_isVip = fetchVipMember.isVip();
                                    if (m_isVip)
                                    {
                                        // 已经是vip会员
                                        sendMessage();
                                    }
                                    else
                                    {
                                        // 还不是会员可以免费发一条信息
                                        // 发送第二条时就需要弹出购买会员的界面
                                        if (Checker.isEmpty(m_chatMessages))
                                        {
                                            sendMessage();
                                            fetchMessage();
                                        }
                                        else
                                        {
                                            // 还不是vip会员
                                            SimpleVipActivity.show(ChatTextActivity.this);
                                        }
                                    }
                                }
                            });
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            findViewById(R.id.img_back).setOnClickListener(clickListener);
            findViewById(R.id.img_user_info).setOnClickListener(clickListener);
            findViewById(R.id.img_phone).setOnClickListener(clickListener);
            findViewById(R.id.img_send).setOnClickListener(clickListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateData()
    {
        try
        {
            if (m_actorPageVo == null)
            {
                return;
            }

            TextView txtTitle = (TextView) findViewById(R.id.txt_title);
            txtTitle.setText(m_actorPageVo.getNickname());

            if (m_adapter == null)
            {
                m_adapter = new ChatAdapter(this, m_chatMessages);
                m_list.setAdapter(m_adapter);
            }
            else
            {
                m_adapter.updateData(m_chatMessages);
                m_adapter.notifyDataSetChanged();

                m_list.setSelection(m_chatMessages.size() - 1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 间隔6-12时间显示一条信息
     *
     */
    private void updateDataRandom()
    {
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                if (Checker.isNotEmpty(m_messageVo.getChat()))
                {
                    try
                    {

                        int random = RandomUtil.nextInt(6, 13);
                        //theApp.showToast("random:" + random);
                        Thread.sleep(random * 1000);

                        for (String message : m_messageVo.getChat())
                        {
                            ChatMsg chatMessage = new ChatMsg();
                            chatMessage.setText(message);
                            chatMessage.setActorId(m_actorPageVo.getId());
                            chatMessage.setSenderId(m_actorPageVo.getId());
                            chatMessage.setSenderAvatar(m_actorPageVo.getIcon());
                            m_chatMessages.add(chatMessage);
                            DBUtil.insertOrReplace(chatMessage);

                            handler().post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    updateData();
                                }
                            });

                            random = RandomUtil.nextInt(6, 13);
                            //theApp.showToast("random:" + random);
                            Thread.sleep(random * 1000);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };

        new Thread(runnable).start();
    }

    /**
     * 购买VIP结果
     * @param event
     */
    public void onEventMainThread(final BuyVipResultEvent event)
    {
        if (event.getPayResult() == 0)
        {
            m_isVip = true;
        }
    }

    private void checkVip(BaseReqRsp.ReqListener listener)
    {
        // 发送消息时先获取当前用户是否VIP
        FetchVipMember fetchVipMember = new FetchVipMember(null, null);
        fetchVipMember.send(listener);
    }

    private void fetchMessage()
    {
        FetchMessage fetchMessage = new FetchMessage(handler(), m_actorPageVo.getId(), null);
        fetchMessage.send();
    }

    private void sendMessage()
    {
        String strText = m_edtMessage.getText().toString();
        if (Checker.isNotEmpty(strText))
        {
            ChatMsg chatMsg = new ChatMsg();
            chatMsg.setText(strText);
            chatMsg.setActorId(m_actorPageVo.getId());
            chatMsg.setSenderId(AppData.getCurUserId());
            chatMsg.setSenderAvatar(AppData.getCurUser().getIcon());
            m_chatMessages.add(chatMsg);
            DBUtil.insertOrReplace(chatMsg);

            handler().post(new Runnable()
            {
                @Override
                public void run()
                {
                    updateData();
                    m_edtMessage.setText("");
                }
            });
        }
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        case HttpUtil.RequestCode.FETCH_VIP_MEMBER:
            FetchVipMember fetchVipMember = (FetchVipMember) msg.obj;
            String callTag = (String) fetchVipMember.rspCallBackTag;
            if (FetchVipMember.isSucceed(fetchVipMember) && callTag.equals("send_message"))
            {
                // 0 非会员; 1 会员
//                int isVip = (fetchVipMember.rspVipMember.getIsvip() != null ? fetchVipMember.rspVipMember.getIsvip() : 0);
//                if (fetchVipMember.rspVipMember != null & isVip == 1)
                m_isVip = fetchVipMember.isVip();
                if (m_isVip)
                {
                    // 已经是vip会员
                    sendMessage();
                }
                else
                {
                    if (Checker.isEmpty(m_chatMessages))
                    {
                        sendMessage();
                        fetchMessage();
                    }
                    else
                    {
                        // 还不是vip会员
                        SimpleVipActivity.show(ChatTextActivity.this);
                    }
                }
            }
            break;
        case HttpUtil.RequestCode.FETCH_ACCOUNT_BALANCE:
            FetchAccountBalance fetchAccountBalance = (FetchAccountBalance) msg.obj;
            if (FetchAccountBalance.isSucceed(fetchAccountBalance))
            {
                int money = (fetchAccountBalance.rspAccountBalanceVo.getMoney() != null ? fetchAccountBalance.rspAccountBalanceVo.getMoney() : 0);
                if (money <= 0)
                {
                    SimpleBalanceActivity.show(ChatTextActivity.this);
                }
                else
                {
//                    if (m_actorVo != null)
//                    {
//                        AVChatActivity.launch(UserInfoActivity.this, m_actorVo.getToken(), AVChatType.AUDIO.getValue(), AVChatActivity.FROM_INTERNAL, m_actorPagerVo);
//                    }
//                    else
//                    {
//                        AVChatActivity.launch(UserInfoActivity.this, m_sessionId, AVChatType.AUDIO.getValue(), AVChatActivity.FROM_INTERNAL, m_actorPagerVo);
//                    }

                    ChatVoiceCallOutActivity.show(ChatTextActivity.this, m_actorPageVo);
                }
            }
            break;
        case HttpUtil.RequestCode.FETCH_MESSAGE:
            FetchMessage fetchMessage = (FetchMessage) msg.obj;
            if (FetchMessage.isSucceed(fetchMessage))
            {
                if (fetchMessage.rspMessageVo != null)
                {
                    m_messageVo = fetchMessage.rspMessageVo;
                    updateDataRandom();
                }
            }
            break;
        }
    }
}
