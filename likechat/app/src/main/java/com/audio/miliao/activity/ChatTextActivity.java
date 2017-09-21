package com.audio.miliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.app.library.util.ImageLoaderUtil;
import com.app.library.vo.ActorPageVo;
import com.audio.miliao.R;
import com.audio.miliao.adapter.ChatAdapter;
import com.audio.miliao.entity.ChatMessage;
import com.audio.miliao.http.HttpUtil;
import com.audio.miliao.http.cmd.FetchAccountBalance;
import com.audio.miliao.http.cmd.FetchVipMember;
import com.audio.miliao.util.DebugUtil;

import java.util.List;

/**
 * 聊天——文字
 */
public class ChatTextActivity extends BaseActivity
{
    private ActorPageVo m_actorPageVo;
    private ListView m_list;
    private ChatAdapter m_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_text);
        try
        {
            m_actorPageVo = (ActorPageVo) getIntent().getSerializableExtra("user");

            initUI();
            updateData();
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

            m_list.setOnScrollListener(ImageLoaderUtil.getPauseListener());
//            m_list.setOnScrollListener(new AbsListView.OnScrollListener()
//            {
//                @Override
//                public void onScrollStateChanged(AbsListView view, int scrollState)
//                {
//                    try
//                    {
//                        switch (scrollState)
//                        {
//                        //停止滚动
//                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                            m_adapter.setScrolling(false);
//                            m_adapter.notifyDataSetChanged();
//                            break;
//                        //滚动做出了抛的动作
//                        case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
//                            m_adapter.setScrolling(true);
//                            break;
//                        //正在滚动
//                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//                            m_adapter.setScrolling(true);
//                            break;
//                        }
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
//                {
//
//                }
//            });

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
                            FetchVipMember fetchVipMember = new FetchVipMember(handler(), null);
                            fetchVipMember.send();
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

            List<ChatMessage> chatMessages = DebugUtil.getChatMessage();
            if (m_adapter == null)
            {
                m_adapter = new ChatAdapter(this, chatMessages);
                m_list.setAdapter(m_adapter);
            }
            else
            {
                m_adapter.updateData(chatMessages);
                m_adapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
        case HttpUtil.RequestCode.FETCH_VIP_MEMBER:
            FetchVipMember fetchVipMember = (FetchVipMember) msg.obj;
            if (FetchVipMember.isSucceed(fetchVipMember))
            {
                // 0 非会员; 1 会员
                int isVip = (fetchVipMember.rspVipMember.getIsvip() != null ? fetchVipMember.rspVipMember.getIsvip() : 0);
                if (fetchVipMember.rspVipMember != null & isVip == 1)
                {
                    // 已经是vip会员
                }
                else
                {
                    // 还不是vip会员
                    Intent intentMobile = new Intent(ChatTextActivity.this, SimpleVipActivity.class);
                    startActivity(intentMobile);
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
                    Intent intent = new Intent(ChatTextActivity.this, SimpleBalanceActivity.class);
                    startActivity(intent);
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
                    Intent intentCallout = new Intent(ChatTextActivity.this, ChatVoiceCallOutActivity.class);
                    intentCallout.putExtra("actor_page", m_actorPageVo);
                    startActivity(intentCallout);
                }
            }
            break;
        }
    }
}
